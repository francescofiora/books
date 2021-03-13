package it.francescofiora.books.web.util;

import it.francescofiora.books.web.errors.NotFoundAlertException;
import java.util.Optional;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

public interface ResponseUtil {
  /**
   * Wrap the optional into a {@link org.springframework.http.ResponseEntity} with
   * an {@link org.springframework.http.HttpStatus#OK} status, or if it's empty,
   * it returns a {@link org.springframework.http.ResponseEntity} with
   * {@link org.springframework.http.HttpStatus#NOT_FOUND}.
   *
   * @param <X>           type of the response
   * @param maybeResponse response to return if present
   * @return response containing {@code maybeResponse} if present or
   *         {@link org.springframework.http.HttpStatus#NOT_FOUND}
   */
  static <X> ResponseEntity<X> wrapOrNotFound(String entityName, Optional<X> maybeResponse) {
    return wrapOrNotFound(entityName, maybeResponse, null);
  }

  /**
   * Wrap the optional into a {@link org.springframework.http.ResponseEntity} with
   * an {@link org.springframework.http.HttpStatus#OK} status with the headers, or
   * if it's empty, throws a {@link NotFoundAlertException} with status
   *         {@link org.springframework.http.HttpStatus#NOT_FOUND}.
   *
   * @param <X>           type of the response
   * @param maybeResponse response to return if present
   * @param header        headers to be added to the response
   * @return response containing {@code maybeResponse} if present
   * @throws NotFoundAlertException {@code 404 (Not found)} if
   *                                {@code maybeResponse} is empty
   */
  static <X> ResponseEntity<X> wrapOrNotFound(String entityName, Optional<X> maybeResponse,
      HttpHeaders header) {
    return maybeResponse.map(response -> ResponseEntity.ok().headers(header).body(response))
        .orElseThrow(() -> new NotFoundAlertException(entityName));
  }
}
