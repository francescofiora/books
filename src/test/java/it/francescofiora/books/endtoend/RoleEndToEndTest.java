package it.francescofiora.books.endtoend;

import static org.assertj.core.api.Assertions.assertThat;

import it.francescofiora.books.service.dto.PermissionDto;
import it.francescofiora.books.service.dto.RefPermissionDto;
import it.francescofiora.books.service.dto.RoleDto;
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
class RoleEndToEndTest extends AbstractTestEndToEnd {

  private static final Long ID_NOT_FOUND = 100L;

  private static final String ROLES_URI = "/api/v1/roles";
  private static final String ROLES_ID_URI = "/api/v1/roles/%d";
  private static final String PERMISSIONS_URI = "/api/v1/permissions";

  private static final String ALERT_CREATED = "RoleDto.created";
  private static final String ALERT_UPDATED = "RoleDto.updated";
  private static final String ALERT_DELETED = "RoleDto.deleted";
  private static final String ALERT_GET = "RoleDto.get";
  private static final String ALERT_BAD_REQUEST = "RoleDto.badRequest";
  private static final String ALERT_NEW_BAD_REQUEST = "NewRoleDto.badRequest";
  private static final String ALERT_PERMISSION_NOT_FOUND = "PermissionDto.notFound";
  private static final String ALERT_NOT_FOUND = "RoleDto.notFound";

  private static final String PARAM_PAGE_20 = "0 20";
  private static final String PARAM_NOT_VALID_LONG =
      "'id' should be a valid 'Long' and '999999999999999999999999' isn't";

  private static final String PARAM_ID_NOT_NULL = "[roleDto.id - NotNull]";
  private static final String PARAM_NEW_PERMISSIONS_NOT_NULL =
      "[newRoleDto.permissions - NotEmpty]";
  private static final String PARAM_NAME_NOT_BLANK = "[roleDto.name - NotBlank]";
  private static final String PARAM_DESCRIPTION_NOT_BLANK = "[roleDto.description - NotBlank]";
  private static final String PARAM_PERMISSIONS_NOT_NULL = "[roleDto.permissions - NotEmpty]";

  @Test
  void testAuth() {
    testUnauthorized(ROLES_URI);
  }

  @Test
  void testCreateBadRequest() {
    var newRoleDto =
        UserUtils.createNewRoleDto(UserUtils.ROLE_BOOK_READ, UserUtils.ROLE_BOOK_READ_DESCR);
    assertCreateBadRequest(UserUtils.ROLE_ADMIN, ROLES_URI, newRoleDto, ALERT_NEW_BAD_REQUEST,
        PARAM_NEW_PERMISSIONS_NOT_NULL);
  }

  @Test
  void testCreateNotFound() {
    var newRoleDto =
        UserUtils.createNewRoleDto(UserUtils.ROLE_BOOK_READ, UserUtils.ROLE_BOOK_READ_DESCR);
    var refPermDto = new RefPermissionDto();
    refPermDto.setId(ID_NOT_FOUND);
    newRoleDto.setPermissions(List.of(refPermDto));
    assertCreateNotFound(UserUtils.ROLE_ADMIN, ROLES_URI, newRoleDto, ALERT_PERMISSION_NOT_FOUND,
        String.valueOf(ID_NOT_FOUND));
  }

  @Test
  void testGetPermissions() {
    var permissions = get(UserUtils.ROLE_ADMIN, PERMISSIONS_URI, PageRequest.of(1, 1),
        PermissionDto[].class, ALERT_GET, PARAM_PAGE_20);
    assertThat(permissions).isNotEmpty();
  }

  @Test
  void testGetPermissionsBadRequest() {
    assertGetBadRequest(UserUtils.ROLE_ADMIN, PERMISSIONS_URI + "?sort=badField",
        PermissionDto[].class, ".badRequest", "badField");
  }

  @Test
  void testCreate() {
    var refPermDto = getPermissionDtoBookRead();

    var newRoleDto =
        UserUtils.createNewRoleDto(UserUtils.ROLE_BOOK_READ, UserUtils.ROLE_BOOK_READ_DESCR);
    newRoleDto.getPermissions().add(refPermDto);
    var roleId = createAndReturnId(UserUtils.ROLE_ADMIN, ROLES_URI, newRoleDto, ALERT_CREATED);

    final var rolesIdUri = String.format(ROLES_ID_URI, roleId);

    var actual =
        get(UserUtils.ROLE_ADMIN, rolesIdUri, RoleDto.class, ALERT_GET, String.valueOf(roleId));
    assertThat(actual.getName()).isEqualTo(newRoleDto.getName());
    assertThat(actual.getDescription()).isEqualTo(newRoleDto.getDescription());
    assertThat(actual.getPermissions()).isNotNull().hasSize(1);
    assertThat(actual.getPermissions().get(0).getId()).isEqualTo(refPermDto.getId());

    var roleDto = createRoleDto(roleId, refPermDto);
    update(UserUtils.ROLE_ADMIN, rolesIdUri, roleDto, ALERT_UPDATED, String.valueOf(roleId));

    actual =
        get(UserUtils.ROLE_ADMIN, rolesIdUri, RoleDto.class, ALERT_GET, String.valueOf(roleId));
    assertThat(actual).isEqualTo(roleDto);
    assertThat(actual.getName()).isEqualTo(roleDto.getName());
    assertThat(actual.getDescription()).isEqualTo(roleDto.getDescription());

    var roles = get(UserUtils.ROLE_ADMIN, ROLES_URI, PageRequest.of(1, 1), RoleDto[].class,
        ALERT_GET, PARAM_PAGE_20);
    assertThat(roles).isNotEmpty();
    var option = Stream.of(roles).filter(role -> role.getId().equals(roleId)).findAny();
    assertThat(option).isPresent().contains(roleDto);

    delete(UserUtils.ROLE_ADMIN, rolesIdUri, ALERT_DELETED, String.valueOf(roleId));

    assertGetNotFound(UserUtils.ROLE_ADMIN, rolesIdUri, RoleDto.class, ALERT_NOT_FOUND,
        String.valueOf(roleId));
  }

  private RoleDto createRoleDto(Long id, RefPermissionDto refPermDto) {
    var roleDto = UserUtils.createRoleDto(id);
    roleDto.getPermissions().add(refPermDto);
    return roleDto;
  }

  @Test
  void testGetBadRequest() {
    assertGetBadRequest(UserUtils.ROLE_ADMIN, ROLES_URI + "/999999999999999999999999", String.class,
        "id.badRequest", PARAM_NOT_VALID_LONG);
  }

  @Test
  void testUpdateBadRequest() {
    var refPermDto = getPermissionDtoBookRead();

    // id
    var roleDto = createRoleDto(null, refPermDto);
    assertUpdateBadRequest(UserUtils.ROLE_ADMIN, String.format(ROLES_ID_URI, 1L), roleDto,
        ALERT_BAD_REQUEST, PARAM_ID_NOT_NULL);

    var newRoleDto =
        UserUtils.createNewRoleDto(UserUtils.ROLE_BOOK_READ, UserUtils.ROLE_BOOK_READ_DESCR);
    newRoleDto.getPermissions().add(refPermDto);
    var id = createAndReturnId(UserUtils.ROLE_ADMIN, ROLES_URI, newRoleDto, ALERT_CREATED);

    roleDto = createRoleDto(id, refPermDto);
    assertUpdateBadRequest(UserUtils.ROLE_ADMIN, String.format(ROLES_ID_URI, (id + 1)), roleDto,
        ALERT_BAD_REQUEST, String.valueOf(id));

    final var path = String.format(ROLES_ID_URI, id);

    // username
    roleDto = createRoleDto(id, refPermDto);
    roleDto.setName(null);
    assertUpdateBadRequest(UserUtils.ROLE_ADMIN, path, roleDto, ALERT_BAD_REQUEST,
        PARAM_NAME_NOT_BLANK);

    roleDto = createRoleDto(id, refPermDto);
    roleDto.setName("");
    assertUpdateBadRequest(UserUtils.ROLE_ADMIN, path, roleDto, ALERT_BAD_REQUEST,
        PARAM_NAME_NOT_BLANK);

    roleDto = createRoleDto(id, refPermDto);
    roleDto.setName("  ");
    assertUpdateBadRequest(UserUtils.ROLE_ADMIN, path, roleDto, ALERT_BAD_REQUEST,
        PARAM_NAME_NOT_BLANK);

    // description
    roleDto = createRoleDto(id, refPermDto);
    roleDto.setDescription(null);
    assertUpdateBadRequest(UserUtils.ROLE_ADMIN, path, roleDto, ALERT_BAD_REQUEST,
        PARAM_DESCRIPTION_NOT_BLANK);

    roleDto = createRoleDto(id, refPermDto);
    roleDto.setDescription("");
    assertUpdateBadRequest(UserUtils.ROLE_ADMIN, path, roleDto, ALERT_BAD_REQUEST,
        PARAM_DESCRIPTION_NOT_BLANK);

    roleDto = createRoleDto(id, refPermDto);
    roleDto.setDescription("   ");
    assertUpdateBadRequest(UserUtils.ROLE_ADMIN, path, roleDto, ALERT_BAD_REQUEST,
        PARAM_DESCRIPTION_NOT_BLANK);

    // roles
    roleDto = UserUtils.createRoleDto(id);
    assertUpdateBadRequest(UserUtils.ROLE_ADMIN, path, roleDto, ALERT_BAD_REQUEST,
        PARAM_PERMISSIONS_NOT_NULL);
  }

  @Test
  void testDeleteBadRequest() {
    var roles = get(UserUtils.ROLE_ADMIN, ROLES_URI, PageRequest.of(1, 1), RoleDto[].class,
        ALERT_GET, PARAM_PAGE_20);

    final var rolesIdUri = String.format(ROLES_ID_URI, roles[0].getId());
    assertDeleteBadRequest(UserUtils.ROLE_ADMIN, rolesIdUri, ALERT_BAD_REQUEST,
        String.valueOf(roles[0].getId()));
  }
}
