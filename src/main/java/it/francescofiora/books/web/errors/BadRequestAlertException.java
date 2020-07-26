package it.francescofiora.books.web.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class BadRequestAlertException extends ResponseStatusException {

  private static final long serialVersionUID = 1L;

  private final String entityName;

  private final String errorKey;

  public BadRequestAlertException(String entityName, String errorKey) {
    this(entityName, errorKey, null);
  }

  /**
   * Constructor.
   * @param entityName entity name
   * @param errorKey error Key
   * @param message message
   */
  public BadRequestAlertException(String entityName, String errorKey, String message) {
    super(HttpStatus.BAD_REQUEST, message);
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
