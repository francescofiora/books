package it.francescofiora.books.web.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * Bad Request Exception.
 */
public class BadRequestAlertException extends ResponseStatusException {

  private static final long serialVersionUID = 1L;

  private final String entityName;

  private final String param;

  /**
   * Constructor.
   *
   * @param entityName entity name
   * @param param the parameter
   * @param errorMessage message
   */
  public BadRequestAlertException(String entityName, String param, String errorMessage) {
    super(HttpStatus.BAD_REQUEST, errorMessage);
    this.entityName = entityName;
    this.param = param;
  }

  public String getEntityName() {
    return entityName;
  }

  public String getParam() {
    return param;
  }
}
