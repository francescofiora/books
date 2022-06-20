package it.francescofiora.books.endtoend;

import static org.assertj.core.api.Assertions.assertThat;

import it.francescofiora.books.domain.Permission;
import it.francescofiora.books.domain.Role;
import it.francescofiora.books.domain.User;
import it.francescofiora.books.repository.PermissionRepository;
import it.francescofiora.books.repository.RoleRepository;
import it.francescofiora.books.repository.UserRepository;
import it.francescofiora.books.service.dto.RefPermissionDto;
import it.francescofiora.books.service.dto.RefRoleDto;
import it.francescofiora.books.util.UserUtils;
import it.francescofiora.books.web.util.HeaderUtil;
import java.net.URI;
import javax.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Abstract Test class for EndToEnd tests.
 */
public class AbstractTestEndToEnd {

  @LocalServerPort
  private int randomServerPort;

  @Autowired
  private TestRestTemplate restTemplate;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PermissionRepository permissionRepository;

  @Autowired
  private RoleRepository roleRepository;

  @BeforeEach
  @Transactional
  void setUp() {
    var list = userRepository.findAll();
    if (list.isEmpty()) {
      createUser(UserUtils.createUserRoleAdmin());
      createUser(UserUtils.createUserBookAdmin());
      createUser(UserUtils.createUserAdmin());
    }
  }

  private void createUser(User user) {
    for (var role : user.getRoles()) {
      createRole(role);
    }
    userRepository.save(user);
  }

  private Role createRole(Role role) {
    for (var permission : role.getPermissions()) {
      permissionRepository.save(permission);
    }
    return roleRepository.save(role);
  }

  private String getPath(String path) {
    return "http://localhost:" + randomServerPort + path;
  }

  protected RefRoleDto getRefRoleDto(String roleName) throws Exception {
    var roleExample = new Role();
    roleExample.setName(roleName);
    var optRole = roleRepository.findOne(Example.of(roleExample));
    var refRoleDto = new RefRoleDto();
    if (optRole.isPresent()) {
      refRoleDto.setId(optRole.get().getId());
    } else {
      Assertions.fail("Role " + roleName + " not found!");
    }
    return refRoleDto;
  }

  protected RefPermissionDto getPermissionDtoBookRead() throws Exception {
    var permissionExample = new Permission();
    permissionExample.setName(UserUtils.OP_BOOK_READ);
    var optPermission = permissionRepository.findOne(Example.of(permissionExample));
    var refPermDto = new RefPermissionDto();
    if (optPermission.isPresent()) {
      refPermDto.setId(optPermission.get().getId());
    } else {
      Assertions.fail("Permission OP_BOOK_READ not found!");
    }
    return refPermDto;
  }

  protected void testUnauthorized(String path) throws Exception {
    var result = unauthorizedGet(path, String.class);
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);

    result = unauthorizedGetWrongUser(path, String.class);
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
  }

  protected <T> ResponseEntity<T> unauthorizedGet(String path, Class<T> responseType)
      throws Exception {
    return restTemplate.getForEntity(new URI(getPath(path)), responseType);
  }

  protected <T> ResponseEntity<T> unauthorizedGetWrongUser(String path, Class<T> responseType)
      throws Exception {
    var headers = new HttpHeaders();
    headers.setBasicAuth("wrong_user", "wrong_password");
    var request = new HttpEntity<>(null, headers);
    return restTemplate.exchange(getPath(path), HttpMethod.GET, request, responseType);
  }

  protected <T> ResponseEntity<T> performGet(String username, String path, Class<T> responseType)
      throws Exception {
    var request = new HttpEntity<>(null, createHttpHeaders(username));
    return restTemplate.exchange(getPath(path), HttpMethod.GET, request, responseType);
  }

  protected <T> ResponseEntity<T> performGet(String username, String path, Pageable pageable,
      Class<T> responseType) throws Exception {
    var request = new HttpEntity<>(pageable, createHttpHeaders(username));
    return restTemplate.exchange(getPath(path), HttpMethod.GET, request, responseType);
  }

  protected ResponseEntity<Void> performDelete(String username, String path) throws Exception {
    var request = new HttpEntity<>(null, createHttpHeaders(username));
    return restTemplate.exchange(getPath(path), HttpMethod.DELETE, request, Void.class);
  }

  protected <T> ResponseEntity<Void> performPost(String username, String path, T body)
      throws Exception {
    var request = new HttpEntity<>(body, createHttpHeaders(username));
    return restTemplate.postForEntity(new URI(getPath(path)), request, Void.class);
  }

  protected <T> ResponseEntity<Void> performPut(String username, String path, T body)
      throws Exception {
    var request = new HttpEntity<>(body, createHttpHeaders(username));
    return restTemplate.exchange(getPath(path), HttpMethod.PUT, request, Void.class);
  }

  private void checkHeaders(HttpHeaders headers, String alert, String param) {
    assertThat(headers).containsKeys(HeaderUtil.X_ALERT, HeaderUtil.X_PARAMS);
    assertThat(headers.get(HeaderUtil.X_ALERT)).contains(alert);
    assertThat(headers.get(HeaderUtil.X_PARAMS)).contains(param);
  }

  protected <T> Long createAndReturnId(String username, String path, T body, String alert)
      throws Exception {
    var result = performPost(username, path, body);
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    assertThat(result.getHeaders()).containsKeys(HeaderUtil.X_ALERT, HttpHeaders.LOCATION,
        HeaderUtil.X_PARAMS);
    assertThat(result.getHeaders().get(HeaderUtil.X_ALERT)).contains(alert);
    assertThat(result.getHeaders().get(HeaderUtil.X_PARAMS)).isNotEmpty();
    var id = getIdFormHttpHeaders(result.getHeaders());
    checkHeaders(result.getHeaders(), alert, String.valueOf(id));
    return id;
  }

  protected <T> void assertCreateNotFound(String username, String path, T body, String alert,
      String param) throws Exception {
    var result = performPost(username, path, body);
    checkHeadersError(result.getHeaders(), alert, param);
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  protected <T> void assertCreateBadRequest(String username, String path, T body, String alert,
      String param) throws Exception {
    var result = performPost(username, path, body);
    checkHeadersError(result.getHeaders(), alert, param);
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
  }

  protected <T> void update(String username, String path, T body, String alert, String param)
      throws Exception {
    var result = performPut(username, path, body);
    checkHeaders(result.getHeaders(), alert, param);
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
  }

  protected <T> void assertUpdateNotFound(String username, String path, T body, String alert,
      String param) throws Exception {
    var result = performPut(username, path, body);
    checkHeadersError(result.getHeaders(), alert, param);
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  protected <T> void assertUpdateBadRequest(String username, String path, T body, String alert,
      String param) throws Exception {
    var result = performPut(username, path, body);
    checkHeadersError(result.getHeaders(), alert, param);
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
  }

  private void checkHeadersError(HttpHeaders headers, String alert, String param) {
    assertThat(headers).containsKeys(HeaderUtil.X_ALERT, HeaderUtil.X_ERROR, HeaderUtil.X_PARAMS);
    assertThat(headers.get(HeaderUtil.X_ALERT)).contains(alert);
    assertThat(headers.get(HeaderUtil.X_ERROR)).isNotEmpty();
    assertThat(headers.get(HeaderUtil.X_PARAMS)).contains(param);
  }

  protected <T> T get(String username, String path, Class<T> responseType, String alert,
      String param) throws Exception {
    var result = performGet(username, path, responseType);
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    checkHeaders(result.getHeaders(), alert, param);
    var value = result.getBody();
    assertThat(value).isNotNull();
    return value;
  }

  protected <T> T get(String username, String path, Pageable pageable, Class<T> responseType,
      String alert, String param) throws Exception {
    var result = performGet(username, path, pageable, responseType);
    checkHeaders(result.getHeaders(), alert, param);
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    var value = result.getBody();
    assertThat(value).isNotNull();
    return value;
  }

  protected <T> void assertGetNotFound(String username, String path, Class<T> responseType,
      String alert, String param) throws Exception {
    var result = performGet(username, path, responseType);
    checkHeadersError(result.getHeaders(), alert, param);
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  protected <T> void assertGetNotFound(String username, String path, Pageable pageable,
      Class<T> responseType, String alert, String param) throws Exception {
    var result = performGet(username, path, pageable, responseType);
    checkHeadersError(result.getHeaders(), alert, param);
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  protected <T> void assertGetBadRequest(String username, String path, Class<T> responseType,
      String alert, String param) throws Exception {
    var result = performGet(username, path, responseType);
    checkHeadersError(result.getHeaders(), alert, param);
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
  }

  protected void delete(String username, String path, String alert, String param) throws Exception {
    var result = performDelete(username, path);
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    checkHeaders(result.getHeaders(), alert, param);
  }

  protected void assertDeleteBadRequest(String username, String path, String alert, String param)
      throws Exception {
    var result = performDelete(username, path);
    checkHeadersError(result.getHeaders(), alert, param);
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
  }

  protected Long getIdFormHttpHeaders(HttpHeaders headers) {
    var url = headers.get(HttpHeaders.LOCATION).get(0);
    return Long.valueOf(url.substring(url.lastIndexOf('/') + 1));
  }

  private HttpHeaders createHttpHeaders(String username) {
    var headers = new HttpHeaders();
    if (username != null) {
      headers.setBasicAuth(username, UserUtils.PASSWORD);
    }
    return headers;
  }
}
