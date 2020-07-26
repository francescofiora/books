package it.francescofiora.books.web.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class NotFoundAlertException extends ResponseStatusException {

  private static final long serialVersionUID = 1L;

  private final String entityName;

  private final String errorKey;

  public NotFoundAlertException(String entityName) {
    this(entityName, null, null);
  }

  public NotFoundAlertException(String entityName, String errorKey) {
    this(entityName, errorKey, null);
  }

  /**
   * Construttor.
   * @param entityName entity Name
   * @param errorKey error Key
   * @param message message
   */
  public NotFoundAlertException(String entityName, String errorKey, String message) {
    super(HttpStatus.NOT_FOUND, message);
    this.entityName = entityName;
    this.errorKey = errorKey;
  }

  public String getEntityName() {
    return entityName;
  }

  public String getErrorKey() {
    return errorKey;
  }
}
