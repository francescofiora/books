package it.francescofiora.books.web.util;

import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpHeaders;

/**
 * Header Util.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class HeaderUtil {

  public static final String X_ALERT = "X-alert";
  public static final String X_ERROR = "X-error";
  public static final String X_PARAMS = "X-params";

  public static final String CREATED = ".created";
  public static final String UPDATED = ".updated";
  public static final String DELETED = ".deleted";
  public static final String GET = ".get";

  /**
   * Create Alert.
   *
   * @param alert the message
   * @param params the list of parameters
   * @param errorMessage the error message
   * @return HttpHeaders
   */
  public static HttpHeaders createAlert(String alert, List<String> params,
      String errorMessage) {
    var headers = new HttpHeaders();
    headers.add(X_ALERT, alert);
    headers.addAll(X_PARAMS, params);
    if (errorMessage != null) {
      headers.add(X_ERROR, errorMessage);
    }
    return headers;
  }

  /**
   * Create Alert.
   *
   * @param alert the message
   * @param param the parameter
   * @return HttpHeaders
   */
  public static HttpHeaders createAlert(String alert, String param) {
    return createAlert(alert, List.of(param), null);
  }

  /**
   * Create Alert.
   *
   * @param alert the message
   * @param param the parameter
   * @param errorMessage the error message
   * @return HttpHeaders
   */
  public static HttpHeaders createAlert(String alert, String param, String errorMessage) {
    return createAlert(alert, List.of(param), errorMessage);
  }
}
