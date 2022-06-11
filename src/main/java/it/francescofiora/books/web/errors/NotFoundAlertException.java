package it.francescofiora.books.web.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * Not Found Alert Exception.
 */
public class NotFoundAlertException extends ResponseStatusException {

  public static final String MSG_NOT_FOUND_WITH_ID = "%s not found with id %s";

  private static final long serialVersionUID = 1L;

  private final String entityName;

  private final String param;

  /**
   * Constructor.
   *
   * @param entityName entity Name
   * @param param the parameter
   * @param errorMessage message
   */
  public NotFoundAlertException(String entityName, String param, String errorMessage) {
    super(HttpStatus.NOT_FOUND, errorMessage);
    this.entityName = entityName;
    this.param = param;
  }

  public String getEntityName() {
    return entityName;
  }

  public String getErrorKey() {
    return param;
  }
}
