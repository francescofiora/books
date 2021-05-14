package it.francescofiora.books.web.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class NotFoundAlertException extends ResponseStatusException {

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
