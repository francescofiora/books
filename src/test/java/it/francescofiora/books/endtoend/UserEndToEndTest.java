package it.francescofiora.books.endtoend;

import static org.assertj.core.api.Assertions.assertThat;

import it.francescofiora.books.service.dto.RefRoleDto;
import it.francescofiora.books.service.dto.UserDto;
import it.francescofiora.books.util.UserUtils;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = {"classpath:application_test.properties"})
class UserEndToEndTest extends AbstractTestEndToEnd {

  private static final Long ID_NOT_FOUND = 100L;

  private static final String USERS_URI = "/api/v1/users";
  private static final String USERS_ID_URI = "/api/v1/users/%d";

  private static final String ALERT_CREATED = "UserDto.created";
  private static final String ALERT_UPDATED = "UserDto.updated";
  private static final String ALERT_DELETED = "UserDto.deleted";
  private static final String ALERT_GET = "UserDto.get";
  private static final String ALERT_BAD_REQUEST = "UserDto.badRequest";
  private static final String ALERT_NEW_BAD_REQUEST = "NewUserDto.badRequest";
  private static final String ALERT_NOT_FOUND = "UserDto.notFound";

  private static final String PARAM_PAGE_20 = "0 20";
  private static final String PARAM_NOT_VALID_LONG =
      "'id' should be a valid 'Long' and '999999999999999999999999' isn't";

  private static final String PARAM_ID_NOT_NULL = "[userDto.id - NotNull]";
  private static final String PARAM_USERNAME_NOT_BLANK = "[userDto.username - NotBlank]";
  private static final String PARAM_ACCOUNT_NOT_EXPIRED_NOT_NULL =
      "[userDto.accountNonExpired - NotNull]";
  private static final String PARAM_NOT_LOCKED_NOT_NULL = "[userDto.accountNonLocked - NotNull]";
  private static final String PARAM_CREDENTIAL_NOT_EXPIRED_NOT_NULL =
      "[userDto.credentialsNonExpired - NotNull]";
  private static final String PARAM_ENABLED_NOT_NULL = "[userDto.enabled - NotNull]";
  private static final String PARAM_ROLES_NOT_NULL = "[userDto.roles - NotEmpty]";
  private static final String PARAM_NEW_ROLES_NOT_NULL = "[newUserDto.roles - NotEmpty]";
  private static final String ALERT_ROLE_NOT_FOUND = "RoleDto.notFound";

  @Test
  void testAuth() throws Exception {
    testUnauthorized(USERS_URI);
  }

  private UserDto createUserDto(Long id, RefRoleDto refRoleDto) {
    var userDto = UserUtils.createUserDto(id);
    userDto.getRoles().add(refRoleDto);
    return userDto;
  }

  @Test
  void testCreateBadRequest() throws Exception {
    var newUserDto = UserUtils.createNewUserDto();
    assertCreateBadRequest(UserUtils.USER_ADMIN, USERS_URI, newUserDto, ALERT_NEW_BAD_REQUEST,
        PARAM_NEW_ROLES_NOT_NULL);
  }

  @Test
  void testCreateNotFound() throws Exception {
    var newUserDto = UserUtils.createNewUserDto();
    var refRoleDto = new RefRoleDto();
    refRoleDto.setId(ID_NOT_FOUND);
    newUserDto.setRoles(List.of(refRoleDto));
    assertCreateNotFound(UserUtils.USER_ADMIN, USERS_URI, newUserDto, ALERT_ROLE_NOT_FOUND,
        String.valueOf(ID_NOT_FOUND));
  }

  @Test
  void testCreate() throws Exception {
    var refRoleDto = getRefRoleDto(UserUtils.ROLE_BOOK_ADMIN);

    var newUserDto = UserUtils.createNewUserDto();
    newUserDto.getRoles().add(refRoleDto);
    var userId = createAndReturnId(UserUtils.USER_ADMIN, USERS_URI, newUserDto, ALERT_CREATED);

    final var usersIdUri = String.format(USERS_ID_URI, userId);

    var actual =
        get(UserUtils.USER_ADMIN, usersIdUri, UserDto.class, ALERT_GET, String.valueOf(userId));
    assertThat(actual.getUsername()).isEqualTo(newUserDto.getUsername());

    var userDto = createUserDto(userId, refRoleDto);
    update(UserUtils.USER_ADMIN, usersIdUri, userDto, ALERT_UPDATED, String.valueOf(userId));

    actual =
        get(UserUtils.USER_ADMIN, usersIdUri, UserDto.class, ALERT_GET, String.valueOf(userId));
    assertThat(actual).isEqualTo(userDto);
    assertThat(actual.getUsername()).isEqualTo(userDto.getUsername());

    var users = get(UserUtils.USER_ADMIN, USERS_URI, PageRequest.of(1, 1), UserDto[].class,
        ALERT_GET, PARAM_PAGE_20);
    assertThat(users).isNotEmpty();
    var option = Stream.of(users).filter(user -> user.getId().equals(userId)).findAny();
    assertThat(option).isPresent().contains(userDto);

    delete(UserUtils.USER_ADMIN, usersIdUri, ALERT_DELETED, String.valueOf(userId));

    assertGetNotFound(UserUtils.USER_ADMIN, usersIdUri, UserDto.class, ALERT_NOT_FOUND,
        String.valueOf(userId));
  }

  @Test
  void testGetBadRequest() throws Exception {
    assertGetBadRequest(UserUtils.USER_ADMIN, USERS_URI + "/999999999999999999999999", String.class,
        "id.badRequest", PARAM_NOT_VALID_LONG);
  }

  @Test
  void testUpdateBadRequest() throws Exception {
    var refRoleDto = getRefRoleDto(UserUtils.ROLE_BOOK_ADMIN);

    // id
    var userDto = createUserDto(null, refRoleDto);
    assertUpdateBadRequest(UserUtils.USER_ADMIN, String.format(USERS_ID_URI, 1L), userDto,
        ALERT_BAD_REQUEST, PARAM_ID_NOT_NULL);

    var newUserDto = UserUtils.createNewUserDto();
    newUserDto.getRoles().add(refRoleDto);
    var id = createAndReturnId(UserUtils.USER_ADMIN, USERS_URI, newUserDto, ALERT_CREATED);

    userDto = createUserDto(id, refRoleDto);
    assertUpdateBadRequest(UserUtils.USER_ADMIN, String.format(USERS_ID_URI, (id + 1)), userDto,
        ALERT_BAD_REQUEST, String.valueOf(id));

    final var path = String.format(USERS_ID_URI, id);

    // username
    userDto = createUserDto(id, refRoleDto);
    userDto.setUsername(null);
    assertUpdateBadRequest(UserUtils.USER_ADMIN, path, userDto, ALERT_BAD_REQUEST,
        PARAM_USERNAME_NOT_BLANK);

    userDto = createUserDto(id, refRoleDto);
    userDto.setUsername("");
    assertUpdateBadRequest(UserUtils.USER_ADMIN, path, userDto, ALERT_BAD_REQUEST,
        PARAM_USERNAME_NOT_BLANK);

    userDto = createUserDto(id, refRoleDto);
    userDto.setUsername("  ");
    assertUpdateBadRequest(UserUtils.USER_ADMIN, path, userDto, ALERT_BAD_REQUEST,
        PARAM_USERNAME_NOT_BLANK);

    // accountNonExpired
    userDto = createUserDto(id, refRoleDto);
    userDto.setAccountNonExpired(null);
    assertUpdateBadRequest(UserUtils.USER_ADMIN, path, userDto, ALERT_BAD_REQUEST,
        PARAM_ACCOUNT_NOT_EXPIRED_NOT_NULL);

    // accountNonLocked
    userDto = createUserDto(id, refRoleDto);
    userDto.setAccountNonLocked(null);
    assertUpdateBadRequest(UserUtils.USER_ADMIN, path, userDto, ALERT_BAD_REQUEST,
        PARAM_NOT_LOCKED_NOT_NULL);

    // credentialsNonExpired
    userDto = createUserDto(id, refRoleDto);
    userDto.setCredentialsNonExpired(null);
    assertUpdateBadRequest(UserUtils.USER_ADMIN, path, userDto, ALERT_BAD_REQUEST,
        PARAM_CREDENTIAL_NOT_EXPIRED_NOT_NULL);

    // enabled
    userDto = createUserDto(id, refRoleDto);
    userDto.setEnabled(null);
    assertUpdateBadRequest(UserUtils.USER_ADMIN, path, userDto, ALERT_BAD_REQUEST,
        PARAM_ENABLED_NOT_NULL);

    // roles
    userDto = UserUtils.createUserDto(id);
    assertUpdateBadRequest(UserUtils.USER_ADMIN, path, userDto, ALERT_BAD_REQUEST,
        PARAM_ROLES_NOT_NULL);
  }
}