package it.francescofiora.books.web.errors;

import java.io.Serial;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * Bad Request Exception.
 */
@Getter
public class BadRequestAlertException extends ResponseStatusException {

  @Serial
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
}
