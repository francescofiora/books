package it.francescofiora.books.itt.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Util Tests.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UtilTests {

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

  /**
   * Create Ref JSONObject by Id.
   *
   * @param id the id
   * @return the JSONObject
   */
  public static JSONObject createRef(Long id) {
    var item = new JSONObject();
    try {
      item.put(ID, id);
    } catch (JSONException e) {
      throw new JsonException(e.getMessage());
    }
    return item;
  }

  /**
   * Create Ref JSONObject from Map by code.
   *
   * @param map the Map
   * @param code the code
   * @return the JSONObject
   */
  public static JSONObject createRef(Map<String, JSONObject> map, String code) {
    try {
      return createRef(map.get(code).getLong(ID));
    } catch (JSONException e) {
      throw new JsonException(e.getMessage());
    }
  }

  /**
   * Create Ref JSONArray from list of ids.
   *
   * @param ids the list of ids
   * @return the JSONArray
   */
  public static JSONArray createRefs(List<Long> ids) {
    var array = new JSONArray();
    for (var id : ids) {
      array.put(createRef(id));
    }
    return array;
  }

  /**
   * Create Ref JSONArray from Map by list of codes.
   *
   * @param map the Map
   * @param codes the list of codes
   * @return the JSONArray
   */
  public static JSONArray createRefs(Map<String, JSONObject> map, List<String> codes) {
    var array = new JSONArray();
    for (var code : codes) {
      array.put(createRef(map, code));
    }
    return array;
  }

  /**
   * Convert JSONArray to Map of JSONObject by name.
   *
   * @param list the JSONArray
   * @param name the name
   * @return the Map
   */
  public static Map<String, JSONObject> listToMap(JSONArray list, String name) {
    Map<String, JSONObject> map = new HashMap<>();
    for (int i = 0; i < list.length(); i++) {
      try {
        var item = list.getJSONObject(i);
        map.put(item.getString(name), item);
      } catch (JSONException e) {
        throw new JsonException(e.getMessage());
      }
    }
    return map;
  }

  /**
   * Create Publisher.
   *
   * @return JSONObject
   */
  public static JSONObject createPublisher() {
    var newPublisher = new JSONObject();
    try {
      newPublisher.put(PUBLISHER_NAME, "Publisher Name");
    } catch (JSONException e) {
      throw new JsonException(e.getMessage());
    }
    return newPublisher;
  }

  /**
   * Update the Publisher.
   *
   * @param publisher the publisher
   */
  public static void updatePublisher(JSONObject publisher) {
    try {
      publisher.put(PUBLISHER_NAME, "Update Publisher Name");
    } catch (JSONException e) {
      throw new JsonException(e.getMessage());
    }
  }

  /**
   * Create Role.
   *
   * @param mapPermission Map of all permissions
   * @return the Role
   */
  public static JSONObject createRole(Map<String, JSONObject> mapPermission) {
    var permissions = UtilTests
        .createRefs(mapPermission, List.of("OP_ROLE_READ", "OP_USER_READ", "OP_BOOK_READ"));
    var role = new JSONObject();
    try {
      role.put("permissions", permissions);
      role.put(NAME, "newRole");
      role.put(DESCRIPTION, "New Role");
    } catch (JSONException e) {
      throw new JsonException(e.getMessage());
    }
    return  role;
  }

  /**
   * Update the Role.
   *
   * @param role the role
   */
  public static void updateRole(JSONObject role) {
    try {
      role.put(NAME, "updateRole");
      role.put(DESCRIPTION, "Update Role");
    } catch (JSONException e) {
      throw new JsonException(e.getMessage());
    }
  }

  /**
   * Create Author.
   *
   * @return JSONObject
   */
  public static JSONObject createAuthor() {
    var newAuthor = new JSONObject();
    try {
      newAuthor.put(FIRST_NAME, "Fname");
      newAuthor.put(LAST_NAME, "Lname");
    } catch (JSONException e) {
      throw new JsonException(e.getMessage());
    }
    return newAuthor;
  }

  /**
   * Update the Author.
   *
   * @param author the author
   */
  public static void updateAuthor(JSONObject author) {
    try {
      author.put(FIRST_NAME, "Updatename");
      author.put(LAST_NAME, "Updatelast");
    } catch (JSONException e) {
      throw new JsonException(e.getMessage());
    }
  }

  /**
   * Create Title.
   *
   * @return JSONObject
   */
  public static JSONObject createTitle() {
    var newTitle = new JSONObject();
    try {
      newTitle.put(NAME, "Title Name");
      newTitle.put(EDITION_NUMBER, 20L);
      newTitle.put(LANGUAGE, "ENGLISH");
      newTitle.put(COPYRIGHT, 2000);
      newTitle.put(IMAGE_FILE, "image file");
      newTitle.put(PRICE, 40);
    } catch (JSONException e) {
      throw new JsonException(e.getMessage());
    }
    return newTitle;
  }

  /**
   * Update the Title.
   *
   * @param title the title
   */
  public static void updateTitle(JSONObject title) {
    try {
      title.put(NAME, "Title Name Update");
      title.put(EDITION_NUMBER, 20L);
      title.put(LANGUAGE, "ITALIAN");
      title.put(COPYRIGHT, 2001);
      title.put(IMAGE_FILE, "image file update");
      title.put(PRICE, 60);
    } catch (JSONException e) {
      throw new JsonException(e.getMessage());
    }
  }
}
