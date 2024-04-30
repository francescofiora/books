package it.francescofiora.books.endtoend;

import static org.assertj.core.api.Assertions.assertThat;

import it.francescofiora.books.domain.Permission;
import it.francescofiora.books.domain.Role;
import it.francescofiora.books.domain.User;
import it.francescofiora.books.repository.PermissionRepository;
import it.francescofiora.books.repository.RoleRepository;
import it.francescofiora.books.repository.UserRepository;
import it.francescofiora.books.service.dto.AuthenticationDto;
import it.francescofiora.books.service.dto.RefPermissionDto;
import it.francescofiora.books.service.dto.RefRoleDto;
import it.francescofiora.books.service.dto.SigninDto;
import it.francescofiora.books.util.UserUtils;
import it.francescofiora.books.web.api.AbstractApi;
import it.francescofiora.books.web.util.HeaderUtil;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.data.domain.Example;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

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

  protected RefRoleDto getRefRoleDto(String roleName) {
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

  protected RefPermissionDto getPermissionDtoBookRead() {
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

  protected void testUnauthorized(String path) {
    var result = unauthorizedGet(path, String.class);
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);

    result = unauthorizedGetWrongToken(path, String.class);
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
  }

  protected <T> ResponseEntity<T> unauthorizedGet(String path, Class<T> responseType) {
    return restTemplate.getForEntity(AbstractApi.createUri(getPath(path)), responseType);
  }

  protected <T> ResponseEntity<T> unauthorizedGetWrongToken(String path, Class<T> responseType) {
    var request = new HttpEntity<>(createHttpHeaders("wrong"));
    return restTemplate.exchange(getPath(path), HttpMethod.GET, request, responseType);
  }

  protected <T> ResponseEntity<T> performGet(String token, String path, Class<T> responseType) {
    var request = new HttpEntity<>(createHttpHeaders(token));
    return restTemplate.exchange(getPath(path), HttpMethod.GET, request, responseType);
  }

  protected <T> ResponseEntity<T> performGet(String token, String path,
      MultiValueMap<String, String> pageable, Class<T> responseType) {
    var request = new HttpEntity<>(createHttpHeaders(token));
    var uri = UriComponentsBuilder.fromHttpUrl(getPath(path)).queryParams(pageable).build();
    return restTemplate.exchange(uri.toUriString(), HttpMethod.GET, request, responseType);
  }

  protected ResponseEntity<Void> performDelete(String token, String path) {
    var request = new HttpEntity<>(createHttpHeaders(token));
    return restTemplate.exchange(getPath(path), HttpMethod.DELETE, request, Void.class);
  }

  protected <T> ResponseEntity<Void> performPost(String token, String path, T body) {
    var request = new HttpEntity<>(body, createHttpHeaders(token));
    return restTemplate.postForEntity(AbstractApi.createUri(getPath(path)), request, Void.class);
  }

  protected <T> ResponseEntity<Void> performPut(String token, String path, T body) {
    var request = new HttpEntity<>(body, createHttpHeaders(token));
    return restTemplate.exchange(getPath(path), HttpMethod.PUT, request, Void.class);
  }

  protected AuthenticationDto getToken(String username) {
    var result = performLogin(username, UserUtils.PASSWORD);
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(result.getBody()).isNotNull();
    assertThat(result.getBody().getToken()).isNotNull();
    checkHeaders(result.getHeaders(), "AuthenticationDto.get", "token");
    return result.getBody();
  }

  protected ResponseEntity<AuthenticationDto> performLogin(String username, String password) {
    var signinDto = new SigninDto();
    signinDto.setUsername(username);
    signinDto.setPassword(password);
    var request = new HttpEntity<>(signinDto, new HttpHeaders());
    return restTemplate.postForEntity(AbstractApi.createUri(getPath("/api/v1/auth/login")),
        request, AuthenticationDto.class);
  }

  private void checkHeaders(HttpHeaders headers, String alert, String param) {
    assertThat(headers).containsKeys(HeaderUtil.X_ALERT, HeaderUtil.X_PARAMS);
    assertThat(headers.get(HeaderUtil.X_ALERT)).contains(alert);
    assertThat(headers.get(HeaderUtil.X_PARAMS)).contains(param);
  }

  protected <T> Long createAndReturnId(String token, String path, T body, String alert) {
    var result = performPost(token, path, body);
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    assertThat(result.getHeaders()).containsKeys(HeaderUtil.X_ALERT, HttpHeaders.LOCATION,
        HeaderUtil.X_PARAMS);
    assertThat(result.getHeaders().get(HeaderUtil.X_ALERT)).contains(alert);
    assertThat(result.getHeaders().get(HeaderUtil.X_PARAMS)).isNotEmpty();
    var id = getIdFormHttpHeaders(result.getHeaders());
    checkHeaders(result.getHeaders(), alert, String.valueOf(id));
    return id;
  }

  protected <T> void assertCreateNotFound(String token, String path, T body, String alert,
      String param) {
    var result = performPost(token, path, body);
    checkHeadersError(result.getHeaders(), alert, param);
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  protected <T> void assertCreateBadRequest(String token, String path, T body, String alert,
      String param) {
    var result = performPost(token, path, body);
    checkHeadersError(result.getHeaders(), alert, param);
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
  }

  protected <T> void update(String token, String path, T body, String alert, String param) {
    var result = performPut(token, path, body);
    checkHeaders(result.getHeaders(), alert, param);
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
  }

  protected <T> void assertUpdateNotFound(String token, String path, T body, String alert,
      String param) {
    var result = performPut(token, path, body);
    checkHeadersError(result.getHeaders(), alert, param);
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  protected <T> void assertUpdateBadRequest(String token, String path, T body, String alert,
      String param) {
    var result = performPut(token, path, body);
    checkHeadersError(result.getHeaders(), alert, param);
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
  }

  protected void checkHeadersError(HttpHeaders headers, String alert, String param) {
    assertThat(headers).containsKeys(HeaderUtil.X_ALERT, HeaderUtil.X_ERROR, HeaderUtil.X_PARAMS);
    assertThat(headers.get(HeaderUtil.X_ALERT)).contains(alert);
    assertThat(headers.get(HeaderUtil.X_ERROR)).isNotEmpty();
    assertThat(headers.get(HeaderUtil.X_PARAMS)).contains(param);
  }

  protected <T> T get(String token, String path, Class<T> responseType, String alert,
      String param) {
    var result = performGet(token, path, responseType);
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    checkHeaders(result.getHeaders(), alert, param);
    var value = result.getBody();
    assertThat(value).isNotNull();
    return value;
  }

  protected <T> T get(String token, String path, MultiValueMap<String, String> pageable,
      Class<T> responseType, String alert, String param) {
    var result = performGet(token, path, pageable, responseType);
    checkHeaders(result.getHeaders(), alert, param);
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    var value = result.getBody();
    assertThat(value).isNotNull();
    return value;
  }

  protected <T> void assertGetNotFound(String token, String path, Class<T> responseType,
      String alert, String param) {
    var result = performGet(token, path, responseType);
    checkHeadersError(result.getHeaders(), alert, param);
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  protected <T> void assertGetNotFound(String token, String path,
      MultiValueMap<String, String> pageable, Class<T> responseType, String alert, String param) {
    var result = performGet(token, path, pageable, responseType);
    checkHeadersError(result.getHeaders(), alert, param);
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  protected <T> void assertGetBadRequest(String token, String path, Class<T> responseType,
      String alert, String param) {
    var result = performGet(token, path, responseType);
    checkHeadersError(result.getHeaders(), alert, param);
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
  }

  protected void delete(String token, String path, String alert, String param) {
    var result = performDelete(token, path);
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    checkHeaders(result.getHeaders(), alert, param);
  }

  protected void assertDeleteBadRequest(String token, String path, String alert, String param) {
    var result = performDelete(token, path);
    checkHeadersError(result.getHeaders(), alert, param);
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
  }

  protected Long getIdFormHttpHeaders(HttpHeaders headers) {
    var url = headers.get(HttpHeaders.LOCATION).get(0);
    return Long.valueOf(url.substring(url.lastIndexOf('/') + 1));
  }

  private HttpHeaders createHttpHeaders(String token) {
    var headers = new HttpHeaders();
    if (token != null) {
      headers.add("Authorization", "Bearer " + token);
    }
    return headers;
  }
}
