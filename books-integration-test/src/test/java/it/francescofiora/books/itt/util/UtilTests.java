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
}
