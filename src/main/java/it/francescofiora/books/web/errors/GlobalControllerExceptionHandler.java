package it.francescofiora.books.web.errors;

import it.francescofiora.books.web.util.HeaderUtil;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalControllerExceptionHandler {

  /**
   * Handle BadRequest.
   *
   * @param ex BadRequestAlertException
   * @return ResponseEntity
   */
  @ExceptionHandler(BadRequestAlertException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<Void> handleBadRequest(BadRequestAlertException ex) {

    return ResponseEntity.badRequest().headers(HeaderUtil
        .createFailureAlert(ex.getEntityName() + ".badRequest", ex.getParam(), ex.getMessage()))
        .build();
  }

  /**
   * Handle Validation Exception.
   *
   * @param ex MethodArgumentNotValidException
   * @return ResponseEntity
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<Void> handleBadRequest(MethodArgumentNotValidException ex) {

    final BindingResult result = ex.getBindingResult();
    final List<String> fieldErrors = result.getFieldErrors().stream()
        .map(f -> f.getObjectName() + "." + f.getField() + ": " + f.getCode())
        .collect(Collectors.toList());
    final String entityName = ex.getTarget().getClass().getSimpleName();

    return ResponseEntity.badRequest()
        .headers(
            HeaderUtil.createFailureAlert(entityName + ".badRequest", fieldErrors, ex.getMessage()))
        .build();
  }

  /**
   * Handle Item Not Found.
   *
   * @param ex NotFoundAlertException
   * @return ResponseEntity
   */
  @ExceptionHandler(NotFoundAlertException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ResponseEntity<Void> handleItemNotFound(NotFoundAlertException ex) {

    return ResponseEntity.notFound().headers(HeaderUtil
        .createFailureAlert(ex.getEntityName() + ".notFound", ex.getErrorKey(), ex.getMessage()))
        .build();
  }
}
