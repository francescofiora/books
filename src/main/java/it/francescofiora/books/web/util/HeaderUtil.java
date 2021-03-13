package it.francescofiora.books.web.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import org.springframework.http.HttpHeaders;

public interface HeaderUtil {

  /**
   * <p>
   * createEntityCreationAlert.
   * </p>
   *
   * @param entityName a {@link java.lang.String} object.
   * @param param      a {@link java.lang.String} object.
   * @return a {@link org.springframework.http.HttpHeaders} object.
   */
  public static HttpHeaders createEntityCreationAlert(String entityName, String param) {
    return createAlert(entityName + ".created", param);
  }

  /**
   * <p>
   * createEntityUpdateAlert.
   * </p>
   *
   * @param entityName a {@link java.lang.String} object.
   * @param param      a {@link java.lang.String} object.
   * @return a {@link org.springframework.http.HttpHeaders} object.
   */
  public static HttpHeaders createEntityUpdateAlert(String entityName, String param) {
    return createAlert(entityName + ".updated", param);
  }

  /**
   * <p>
   * createEntityDeletionAlert.
   * </p>
   *
   * @param entityName a {@link java.lang.String} object.
   * @param param      a {@link java.lang.String} object.
   * @return a {@link org.springframework.http.HttpHeaders} object.
   */
  public static HttpHeaders createEntityDeletionAlert(String entityName, String param) {
    return createAlert(entityName + ".deleted", param);
  }

  /**
   * <p>
   * createFailureAlert.
   * </p>
   *
   * @param entityName a {@link java.lang.String} object.
   * @param message    a {@link java.lang.String} object.
   * @return a {@link org.springframework.http.HttpHeaders} object.
   */
  public static HttpHeaders createFailureAlert(String entityName, String message) {
    HttpHeaders headers = new HttpHeaders();
    headers.add("X-error", message);
    headers.add("X-params", entityName);
    return headers;
  }

  /**
   * <p>
   * createAlert.
   * </p>
   *
   * @param message a {@link java.lang.String} object.
   * @param param   a {@link java.lang.String} object.
   * @return a {@link org.springframework.http.HttpHeaders} object.
   */
  public static HttpHeaders createAlert(String message, String param) {
    HttpHeaders headers = new HttpHeaders();
    headers.add("X-alert", message);
    try {
      headers.add("X-params", URLEncoder.encode(param, StandardCharsets.UTF_8.toString()));
    } catch (UnsupportedEncodingException e) {
      // StandardCharsets are supported by every Java implementation so this exception
      // will never happen
    }
    return headers;
  }
}
