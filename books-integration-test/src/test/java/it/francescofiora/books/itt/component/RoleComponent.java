package it.francescofiora.books.itt.component;

import static org.assertj.core.api.Assertions.assertThat;

import it.francescofiora.books.itt.context.RoleContext;
import it.francescofiora.books.itt.util.JsonException;
import it.francescofiora.books.itt.util.UtilTests;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;

/**
 * Role Component.
 */
@RequiredArgsConstructor
public class RoleComponent {

  private final BookApiService bookApiService;
  private final RoleContext roleContext;

  /**
   * Create Role.
   */
  public void createRole() {
    var resultPermissions = roleContext.getResultPermissions();
    try {
      var mapPermission = UtilTests
          .listToMap(new JSONArray(resultPermissions.getBody()), UtilTests.NAME);
      var role = UtilTests.createRole(mapPermission);
      roleContext.setNewRole(role);
      var roleId = bookApiService.createRole(role.toString());
      roleContext.setRoleId(roleId);
    } catch (JSONException e) {
      throw new JsonException(e.getMessage());
    }
  }

  /**
   * Update Role.
   */
  public void updateRole() {
    UtilTests.updateRole(roleContext.getNewRole());
    var resultVoid = bookApiService.updateRole(
        roleContext.getRoleId(), roleContext.getNewRole().toString());
    assertThat(resultVoid.getStatusCode()).isEqualTo(HttpStatus.OK);
  }

  /**
   * Find Roles.
   */
  public void findRoles() {
    var resultRoles = bookApiService.findRoles();
    assertThat(resultRoles.getStatusCode()).isEqualTo(HttpStatus.OK);
    roleContext.setResultRoles(resultRoles);
  }

  /**
   * Get Role By Id.
   */
  public void getRoleById() {
    var resultRole = bookApiService.getRoleById(roleContext.getRoleId());
    assertThat(resultRole.getStatusCode()).isEqualTo(HttpStatus.OK);
    try {
      roleContext.setRole(new JSONObject(resultRole.getBody()));
    } catch (JSONException e) {
      throw new JsonException(e.getMessage());
    }
  }

  /**
   * Find Permissions.
   */
  public void findPermissions() {
    var resultPermissions = bookApiService.findPermissions();
    assertThat(resultPermissions.getStatusCode()).isEqualTo(HttpStatus.OK);
    roleContext.setResultPermissions(resultPermissions);
  }

  public void deleteRoleById() {
    var result = bookApiService.deleteRoleById(roleContext.getRoleId());
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
  }

  /**
   * Check Role List.
   */
  public void checkRoleList() {
    checkList(roleContext.getResultRoles().getBody());
  }

  /**
   * Check Permission List.
   */
  public void checkPermissionList() {
    checkList(roleContext.getResultPermissions().getBody());
  }

  private void checkList(String json) {
    try {
      var list = new JSONArray(json);
      for (int i = 0; i < list.length(); i++) {
        var item = list.getJSONObject(i);
        var id = item.getLong(UtilTests.ID);
        assertThat(id).isGreaterThan(0);
        var name = item.getString(UtilTests.NAME);
        assertThat(name).isNotNull();
        var description = item.getString(UtilTests.DESCRIPTION);
        assertThat(description).isNotNull();
      }
    } catch (JSONException e) {
      throw new JsonException(e.getMessage());
    }
  }

  /**
   * Check Role.
   */
  public void checkRole() {
    var role = roleContext.getRole();
    try {
      var newRole = roleContext.getNewRole();
      assertThat(role.getString(UtilTests.NAME)).isEqualTo(newRole.getString(UtilTests.NAME));
      assertThat(role.getString(UtilTests.DESCRIPTION))
          .isEqualTo(newRole.getString(UtilTests.DESCRIPTION));
      roleContext.setNewRole(role);
    } catch (JSONException e) {
      throw new JsonException(e.getMessage());
    }
  }
}
