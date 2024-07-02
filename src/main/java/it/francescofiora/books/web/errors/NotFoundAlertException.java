package it.francescofiora.books.web.errors;

import java.io.Serial;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * Not Found Alert Exception.
 */
@Getter
public class NotFoundAlertException extends ResponseStatusException {

  public static final String MSG_NOT_FOUND_WITH_ID = "%s not found with id %s";

  @Serial
  private static final long serialVersionUID = 1L;

  private final String entityName;

  private final String errorKey;

  /**
   * Constructor.
   *
   * @param entityName entity Name
   * @param errorKey the parameter related to the error
   * @param errorMessage message
   */
  public NotFoundAlertException(String entityName, String errorKey, String errorMessage) {
    super(HttpStatus.NOT_FOUND, errorMessage);
    this.entityName = entityName;
    this.errorKey = errorKey;
  }
}
