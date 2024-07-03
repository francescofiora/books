package it.francescofiora.books.itt.api;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Abstract class for Test Containers.
 */
public abstract class AbstractTestContainer {

  public static final String ID = "id";
  public static final String NAME = "name";
  public static final String EDITION_NUMBER = "editionNumber";
  public static final String LANGUAGE = "language";
  public static final String COPYRIGHT = "copyright";
  public static final String IMAGE_FILE = "imageFile";
  public static final String PRICE = "price";
  public static final String FIRST_NAME = "firstName";
  public static final String LAST_NAME = "lastName";
  public static final String DESCRIPTION = "description";
  public static final String PUBLISHER_NAME = "publisherName";

  public static final String PERMISSION_URL = "/api/v1/permissions";
  public static final String ROLE_URL = "/api/v1/roles";
  public static final String AUTHOR_URL = "/api/v1/authors";
  public static final String PUBLISHER_URL = "/api/v1/publishers";
  public static final String TITLE_URL = "/api/v1/titles";

  public static final String ROLE_ADMIN = "roleAdmin";
  public static final String USER_ADMIN = "userAdmin";
  public static final String BOOK_ADMIN = "bookAdmin";

  public static final String USER_TEST = "username";
  public static final String USER_READER_TEST = "userRead";
  public static final String PASSWORD_TEST = "mypassword";

  public static final String PASSWORD = "password";

  protected void checkRoleAndPermissionList(JSONArray list) throws JSONException {
    for (int i = 0; i < list.length(); i++) {
      var item = list.getJSONObject(i);
      var id = item.getLong(ID);
      assertThat(id).isGreaterThan(0);
      var name = item.getString(NAME);
      assertThat(name).isNotNull();
      var description = item.getString(DESCRIPTION);
      assertThat(description).isNotNull();
    }
  }

  protected void checkRole(JSONObject item, JSONObject newItem) throws JSONException {
    assertThat(item.getString(NAME)).isEqualTo(newItem.getString(NAME));
    assertThat(item.getString(DESCRIPTION)).isEqualTo(newItem.getString(DESCRIPTION));
  }

  protected void checkAuthor(JSONObject item, JSONObject newItem) throws JSONException {
    assertThat(item.getString(FIRST_NAME)).isEqualTo(newItem.getString(FIRST_NAME));
    assertThat(item.getString(LAST_NAME)).isEqualTo(newItem.getString(LAST_NAME));
  }

  protected void checkPublisher(JSONObject item, JSONObject newItem) throws JSONException {
    assertThat(item.getString(PUBLISHER_NAME)).isEqualTo(newItem.getString(PUBLISHER_NAME));
  }

  protected void checkTitle(JSONObject item, JSONObject newItem) throws JSONException {
    assertThat(item.getString(NAME)).isEqualTo(newItem.getString(NAME));
    assertThat(item.getLong(EDITION_NUMBER)).isEqualTo(newItem.getLong(EDITION_NUMBER));
    assertThat(item.getString(LANGUAGE)).isEqualTo(newItem.getString(LANGUAGE));
    assertThat(item.getString(COPYRIGHT)).isEqualTo(newItem.getString(COPYRIGHT));
    assertThat(item.getString(IMAGE_FILE)).isEqualTo(newItem.getString(IMAGE_FILE));
    assertThat(item.getLong(PRICE)).isEqualTo(newItem.getLong(PRICE));
  }
}
