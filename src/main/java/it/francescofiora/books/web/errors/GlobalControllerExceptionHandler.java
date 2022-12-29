package it.francescofiora.books.web.errors;

import it.francescofiora.books.web.util.HeaderUtil;
import java.util.stream.Collectors;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/**
 * Global Controller Exception Handler.
 */
@RestControllerAdvice
public class GlobalControllerExceptionHandler {

  private static final String TYPE_MISMATCH_MESSAGE = "'%s' should be a valid '%s' and '%s' isn't";
  private static final String ALERT_BAD_REQUEST = ".badRequest";
  private static final String ALERT_NOT_FOUND = ".notFound";
  private static final String FORMAT_FIELD = "[%s.%s - %s]";

  /**
   * Handle BadRequest.
   *
   * @param ex BadRequestAlertException
   * @return ResponseEntity
   */
  @ExceptionHandler(BadRequestAlertException.class)
  public ResponseEntity<Void> handleBadRequest(BadRequestAlertException ex) {

    return createBadRequest(HeaderUtil.createFailureAlert(ex.getEntityName() + ALERT_BAD_REQUEST,
        ex.getParam(), ex.getMessage()));
  }

  private ResponseEntity<Void> createBadRequest(HttpHeaders httpHeaders) {
    return ResponseEntity.badRequest().headers(httpHeaders).build();
  }

  /**
   * Handle Validation Exception.
   *
   * @param ex MethodArgumentNotValidException
   * @return ResponseEntity
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Void> handleArgumentNotValid(MethodArgumentNotValidException ex) {

    final var result = ex.getBindingResult();
    final var fieldErrors = result.getFieldErrors().stream()
        .map(f -> String.format(FORMAT_FIELD, f.getObjectName(), f.getField(), f.getCode()))
        .collect(Collectors.toList());

    return createBadRequest(HeaderUtil.createFailureAlert(
        getSimpleName(ex.getTarget()) + ALERT_BAD_REQUEST, fieldErrors, ex.getMessage()));
  }

  private String getSimpleName(Object obj) {
    return obj != null ? obj.getClass().getSimpleName() : "";
  }

  private String getSimpleName(Class<?> clazz) {
    return clazz != null ? clazz.getSimpleName() : "";
  }

  /**
   * Handle Validation Exception.
   *
   * @param ex MethodArgumentTypeMismatchException
   * @return ResponseEntity
   */
  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<Void> handleArgumentTypeMismatch(MethodArgumentTypeMismatchException ex) {

    final var fieldError = String.format(TYPE_MISMATCH_MESSAGE, ex.getName(),
        getSimpleName(ex.getRequiredType()), ex.getValue());
    final var entityName = ex.getName();

    return createBadRequest(
        HeaderUtil.createFailureAlert(entityName + ALERT_BAD_REQUEST, fieldError, ex.getMessage()));
  }

  /**
   * Handle Item Not Found.
   *
   * @param ex NotFoundAlertException
   * @return ResponseEntity
   */
  @ExceptionHandler(NotFoundAlertException.class)
  public ResponseEntity<Void> handleNotFound(NotFoundAlertException ex) {

    return ResponseEntity.notFound()
        .headers(HeaderUtil.createFailureAlert(ex.getEntityName() + ALERT_NOT_FOUND,
            ex.getErrorKey(), ex.getMessage()))
        .build();
  }

  /**
   * Handle Property Reference Exception.
   *
   * @param ex PropertyReferenceException
   * @return ResponseEntity
   */
  @ExceptionHandler(PropertyReferenceException.class)
  public ResponseEntity<Void> handlePropertyReferenceException(PropertyReferenceException ex) {

    return createBadRequest(
        HeaderUtil.createFailureAlert(ALERT_BAD_REQUEST, ex.getPropertyName(), ex.getMessage()));
  }
}
