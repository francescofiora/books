package it.francescofiora.books.util;

import it.francescofiora.books.domain.Permission;
import it.francescofiora.books.domain.Role;
import it.francescofiora.books.domain.User;
import it.francescofiora.books.service.dto.NewRoleDto;
import it.francescofiora.books.service.dto.NewUserDto;
import it.francescofiora.books.service.dto.PermissionDto;
import it.francescofiora.books.service.dto.RoleDto;
import it.francescofiora.books.service.dto.UserDto;
import java.util.Objects;
import java.util.Random;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * User Utility for testing.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UserUtils {

  // Permissions
  public static final String OP_ROLE_UPDATE = "OP_ROLE_UPDATE";
  public static final String OP_ROLE_UPDATE_DESCR =
      "Create, update and delete roles and permissions";

  public static final String OP_ROLE_READ = "OP_ROLE_READ";
  public static final String OP_ROLE_READ_DESCR = "Read roles and permissions";

  public static final String OP_USER_UPDATE = "OP_USER_UPDATE";
  public static final String OP_USER_UPDATE_DESCR = "Create, update and delete users";

  public static final String OP_USER_READ = "OP_USER_READ";
  public static final String OP_USER_READ_DESCR = "Read users";

  public static final String OP_BOOK_UPDATE = "OP_BOOK_UPDATE";
  public static final String OP_BOOK_UPDATE_DESCR = "Create, update and delete books";

  public static final String OP_BOOK_READ = "OP_BOOK_READ";
  public static final String OP_BOOK_READ_DESCR = "Read books";

  // Roles
  public static final String ROLE_PERMISSION_ADMIN = "ROLE_PERMISSION_ADMIN";
  public static final String ROLE_PERMISSION_ADMIN_DESCR = "Administration roles and permissions";

  public static final String ROLE_USER_ADMIN = "ROLE_USER_ADMIN";
  public static final String ROLE_USER_ADMIN_DESCR = "Administration users";

  public static final String ROLE_BOOK_ADMIN = "ROLE_BOOK_ADMIN";
  public static final String ROLE_BOOK_ADMIN_DESCR = "Administration books";

  public static final String ROLE_BOOK_READ = "ROLE_BOOK_READ";
  public static final String ROLE_BOOK_READ_DESCR = "Read books";

  // Users
  public static final String PASSWORD = "password";
  public static final String PASSWORD_ENC = new BCryptPasswordEncoder().encode(PASSWORD);

  public static final String ROLE_ADMIN = "roleAdmin";
  public static final String USER_ADMIN = "userAdmin";
  public static final String BOOK_ADMIN = "bookAdmin";
  public static final String USER = "user";

  /**
   * Create Permission.
   *
   * @param name the name
   * @param description the description
   * @return Permission
   */
  public static Permission createPermission(String name, String description) {
    var permission = new Permission();
    permission.setName(name);
    permission.setDescription(description);
    return permission;
  }

  /**
   * Create ROLE_PERMISSION_ADMIN Role.
   *
   * @return Role
   */
  public static Role createRolePermissionAdmin() {
    var role = new Role();
    role.setName(ROLE_PERMISSION_ADMIN);
    role.setDescription(ROLE_PERMISSION_ADMIN_DESCR);
    role.getPermissions().add(createPermission(OP_ROLE_UPDATE, OP_ROLE_UPDATE_DESCR));
    role.getPermissions().add(createPermission(OP_ROLE_READ, OP_ROLE_READ_DESCR));
    return role;
  }

  /**
   * Create ROLE_USER_ADMIN Role.
   *
   * @return Role
   */
  public static Role createRoleUserAdmin() {
    var role = new Role();
    role.setName(ROLE_USER_ADMIN);
    role.setDescription(ROLE_USER_ADMIN_DESCR);
    role.getPermissions().add(createPermission(OP_ROLE_READ, OP_ROLE_READ_DESCR));
    role.getPermissions().add(createPermission(OP_USER_UPDATE, OP_USER_UPDATE_DESCR));
    role.getPermissions().add(createPermission(OP_USER_READ, OP_USER_READ_DESCR));
    return role;
  }

  /**
   * Create ROLE_BOOK_ADMIN Role.
   *
   * @return Role
   */
  public static Role createRoleBookAdmin() {
    var role = new Role();
    role.setName(ROLE_BOOK_ADMIN);
    role.setDescription(ROLE_BOOK_ADMIN_DESCR);
    role.getPermissions().add(createPermission(OP_BOOK_UPDATE, OP_BOOK_UPDATE_DESCR));
    role.getPermissions().add(createPermission(OP_BOOK_READ, OP_BOOK_READ_DESCR));
    return role;
  }

  /**
   * Create book_administration Role.
   *
   * @return Role
   */
  public static Role createRoleBookRead() {
    var role = new Role();
    role.setName(ROLE_BOOK_READ);
    role.setDescription(ROLE_BOOK_READ_DESCR);
    role.getPermissions().add(createPermission(OP_BOOK_READ, OP_BOOK_READ_DESCR));
    return role;
  }

  /**
   * Create ROLE_ADMIN User.
   *
   * @return User
   */
  public static User createUserRoleAdmin() {
    var user = new User();
    user.setUsername(ROLE_ADMIN);
    user.setPassword(PASSWORD_ENC);
    user.getRoles().add(createRolePermissionAdmin());
    return user;
  }

  /**
   * Create USER_ADMIN User.
   *
   * @return User
   */
  public static User createUserAdmin() {
    var user = new User();
    user.setUsername(USER_ADMIN);
    user.setPassword(PASSWORD_ENC);
    user.getRoles().add(createRoleUserAdmin());
    return user;
  }

  /**
   * Create BOOK_ADMIN User.
   *
   * @return User
   */
  public static User createUserBookAdmin() {
    var user = new User();
    user.setUsername(BOOK_ADMIN);
    user.setPassword(PASSWORD_ENC);
    user.getRoles().add(createRoleBookAdmin());
    return user;
  }

  /**
   * Create bookAdmin User.
   *
   * @return User
   */
  public static User createUserBookReader() {
    var user = new User();
    user.setUsername(USER);
    user.setPassword(PASSWORD_ENC);
    user.getRoles().add(createRoleBookRead());
    return user;
  }

  /**
   * Create PermissionDto.
   *
   * @param name the name
   * @param description the description
   * @return PermissionDto
   */
  public static PermissionDto createPermissionDto(String name, String description) {
    var permission = new PermissionDto();
    permission.setName(name);
    permission.setDescription(description);
    return permission;
  }

  /**
   * Create RoleDto.
   *
   * @param id the id
   * @return RoleDto
   */
  public static RoleDto createRoleDto(Long id) {
    var role = new RoleDto();
    role.setId(id);
    role.setName("name_diff");
    role.setDescription("description_diff");
    return role;
  }

  private static String generateRandomString(long maxSize) {
    return new Random().ints(97, 123).limit(maxSize)
        .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
        .toString();
  }

  /**
   * Create NewUserDto.
   *
   * @return NewUserDto
   */
  public static NewUserDto createNewUserDto() {
    return createNewUserDto(generateRandomString(8));
  }

  /**
   * Create NewUserDto.
   *
   * @param username the username
   * @return NewUserDto
   */
  public static NewUserDto createNewUserDto(String username) {
    var user = new NewUserDto();
    user.setUsername(username);
    user.setPassword(PASSWORD);
    user.setAccountNonExpired(true);
    user.setAccountNonLocked(true);
    user.setCredentialsNonExpired(true);
    user.setEnabled(true);
    return user;
  }

  /**
   * Create UserDto.
   *
   * @param id the id
   * @return UserDto
   */
  public static UserDto createUserDto(Long id) {
    var user = new UserDto();
    user.setUsername(generateRandomString(8));
    user.setAccountNonExpired(true);
    user.setAccountNonLocked(true);
    user.setCredentialsNonExpired(true);
    user.setEnabled(true);
    user.setId(id);
    return user;
  }

  /**
   * Create NewRoleDto.
   *
   * @param name the name
   * @param description the description
   * @return NewRoleDto
   */
  public static NewRoleDto createNewRoleDto(String name, String description) {
    var role = new NewRoleDto();
    role.setName(name);
    role.setDescription(description);
    return role;
  }

  /**
   * Check that actual data equals expected data.
   *
   * @param expected Permission
   * @param actual Permission
   * @return true if actual contains same data then expected
   */
  public static boolean dataEquals(Permission expected, Permission actual) {
    return Objects.equals(expected.getName(), actual.getName())
        && Objects.equals(expected.getDescription(), actual.getDescription());
  }

  /**
   * Check that actual data equals expected data.
   *
   * @param expected Role
   * @param actual Role
   * @return true if actual contains same data then expected
   */
  public static boolean dataEquals(Role expected, Role actual) {
    return Objects.equals(expected.getName(), actual.getName())
        && Objects.equals(expected.getDescription(), actual.getDescription());
  }

  /**
   * Check that actual data equals expected data.
   *
   * @param expected User
   * @param actual User
   * @return true if actual contains same data then expected
   */
  public static boolean dataEquals(User expected, User actual) {
    return Objects.equals(expected.getUsername(), actual.getUsername())
        && Objects.equals(expected.getPassword(), actual.getPassword());
  }
}
