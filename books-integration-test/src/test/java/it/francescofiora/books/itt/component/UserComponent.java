package it.francescofiora.books.itt.component;

import static org.assertj.core.api.Assertions.assertThat;

import it.francescofiora.books.itt.util.JsonException;
import it.francescofiora.books.itt.util.UtilTests;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;

/**
 * User Component.
 */
@RequiredArgsConstructor
public class UserComponent {

  private final List<Long> users = new ArrayList<>();

  private final BookApiService bookApiService;

  /**
   * set User.
   *
   * @param user the user
   * @param password the password
   */
  public void setUser(String user, String password) {
    try {
      var signin = new JSONObject();
      signin.put("username", user);
      signin.put("password", password);
      bookApiService.login(signin.toString());
    } catch (JSONException e) {
      throw new JsonException(e.getMessage());
    }
  }

  /**
   * Create User.
   *
   * @param user the user
   * @param password the password
   * @param listRole the list of Roles
   */
  public void createUser(String user, String password, List<String> listRole) {
    var result = bookApiService.findRoles();
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    var newUser = new JSONObject();
    try {
      var mapRole = UtilTests.listToMap(new JSONArray(result.getBody()), UtilTests.NAME);
      var roles = UtilTests.createRefs(mapRole, listRole);
      newUser.put("roles", roles);
      newUser.put("password", password);
      newUser.put("username", user);
      newUser.put("accountNonExpired", true);
      newUser.put("accountNonLocked", true);
      newUser.put("credentialsNonExpired", true);
      newUser.put("enabled", true);
    } catch (JSONException e) {
      throw new JsonException(e.getMessage());
    }
    var userId = bookApiService.createUser(newUser.toString());
    users.add(userId);
  }

  /**
   * Delete all Users.
   */
  public void deleteAllUsers() {
    for (var userId : users) {
      var result = bookApiService.deleteUserById(userId);
      assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
  }
}
