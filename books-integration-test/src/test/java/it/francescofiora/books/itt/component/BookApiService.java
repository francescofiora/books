package it.francescofiora.books.itt.component;

import it.francescofiora.books.itt.container.HttpClient;
import it.francescofiora.books.itt.util.JsonException;
import lombok.RequiredArgsConstructor;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;

/**
 * BookApi Service.
 */
@RequiredArgsConstructor
public class BookApiService {

  public static final String USER_URL = "/api/v1/users";
  public static final String ROLE_URL = "/api/v1/roles";
  public static final String PERMISSION_URL = "/api/v1/permissions";
  public static final String AUTHOR_URL = "/api/v1/authors";
  public static final String PUBLISHER_URL = "/api/v1/publishers";
  public static final String TITLE_URL = "/api/v1/titles";

  private final HttpClient httpClient;

  public Long createUser(String user) {
    return httpClient.createAndReturnId(USER_URL, user);
  }

  /**
   * Login.
   *
   * @param signin the signin
   */
  public void login(String signin) {
    var result = httpClient.performLogin(signin);
    try {
      var tokenObj = new JSONObject(result.getBody());
      httpClient.setToken(tokenObj.getString("token"));
    } catch (JSONException e) {
      throw new JsonException(e.getMessage());
    }
  }

  public ResponseEntity<Void> deleteUserById(Long userId) {
    return httpClient.performDelete(USER_URL + "/" + userId);
  }

  public ResponseEntity<String> findRoles() {
    return httpClient.performGet(ROLE_URL);
  }

  public ResponseEntity<String> getRoleById(Long roleId) {
    return httpClient.performGet(ROLE_URL + "/" + roleId);
  }

  public ResponseEntity<Void> deleteRoleById(Long roleId) {
    return httpClient.performDelete(ROLE_URL + "/" + roleId);
  }

  public Long createRole(String role) {
    return httpClient.createAndReturnId(ROLE_URL, role);
  }

  public ResponseEntity<Void> updateRole(Long roleId, String role) {
    return httpClient.performPut(ROLE_URL + "/" + roleId, role);
  }

  public ResponseEntity<String> findPermissions() {
    return httpClient.performGet(PERMISSION_URL);
  }

  public Long createAuthor(String author) {
    return httpClient.createAndReturnId(AUTHOR_URL, author);
  }

  public ResponseEntity<String> findAuthors() {
    return httpClient.performGet(AUTHOR_URL);
  }

  public ResponseEntity<String> getAuthorById(Long authorId) {
    return httpClient.performGet(AUTHOR_URL + "/" + authorId);
  }

  public ResponseEntity<Void> updateAuthor(Long authorId, String author) {
    return httpClient.performPut(AUTHOR_URL + "/" + authorId, author);
  }

  public ResponseEntity<Void> deleteAuthorById(Long authorId) {
    return httpClient.performDelete(AUTHOR_URL + "/" + authorId);
  }

  public Long createPublisher(String publisher) {
    return httpClient.createAndReturnId(PUBLISHER_URL, publisher);
  }

  public ResponseEntity<String> findPublishers() {
    return httpClient.performGet(PUBLISHER_URL);
  }

  public ResponseEntity<String> getPublisherById(Long publisherId) {
    return httpClient.performGet(PUBLISHER_URL + "/" + publisherId);
  }

  public ResponseEntity<Void> updatePublisher(Long publisherId, String publisher) {
    return httpClient.performPut(PUBLISHER_URL + "/" + publisherId, publisher);
  }

  public ResponseEntity<Void> deletePublisherById(Long publisherId) {
    return httpClient.performDelete(PUBLISHER_URL + "/" + publisherId);
  }

  public Long createTitle(String title) {
    return httpClient.createAndReturnId(TITLE_URL, title);
  }

  public ResponseEntity<String> findTitles() {
    return httpClient.performGet(TITLE_URL);
  }

  public ResponseEntity<String> getTitleById(Long titleId) {
    return httpClient.performGet(TITLE_URL + "/" + titleId);
  }

  public ResponseEntity<Void> updateTitle(Long titleId, String title) {
    return httpClient.performPut(TITLE_URL + "/" + titleId, title);
  }

  public ResponseEntity<Void> deleteTitleById(Long titleId) {
    return httpClient.performDelete(TITLE_URL + "/" + titleId);
  }
}
