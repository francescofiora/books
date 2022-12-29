package it.francescofiora.books.web.api;

import it.francescofiora.books.web.errors.NotFoundAlertException;
import it.francescofiora.books.web.util.HeaderUtil;
import it.francescofiora.books.web.util.PaginationUtil;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

/**
 * Abstract Api RestController.
 */
public abstract class AbstractApi {

  public static final String OP_ROLE_UPDATE = "OP_ROLE_UPDATE";
  public static final String OP_ROLE_READ = "OP_ROLE_READ";
  public static final String OP_USER_UPDATE = "OP_USER_UPDATE";
  public static final String OP_USER_READ = "OP_USER_READ";
  public static final String OP_BOOK_UPDATE = "OP_BOOK_UPDATE";
  public static final String OP_BOOK_READ = "OP_BOOK_READ";

  protected static final String AUTHORIZE = "hasAuthority";
  protected static final String AUTHORIZE_ROLE_UPDATE = AUTHORIZE + "('" + OP_ROLE_UPDATE + "')";
  protected static final String AUTHORIZE_ROLE_READ = AUTHORIZE + "('" + OP_ROLE_READ + "')";
  protected static final String AUTHORIZE_USER_UPDATE = AUTHORIZE + "('" + OP_USER_UPDATE + "')";
  protected static final String AUTHORIZE_USER_READ = AUTHORIZE + "('" + OP_USER_READ + "')";
  protected static final String AUTHORIZE_BOOK_UPDATE = AUTHORIZE + "('" + OP_BOOK_UPDATE + "')";
  protected static final String AUTHORIZE_BOOK_READ = AUTHORIZE + "('" + OP_BOOK_READ + "')";

  private final String entityName;

  protected AbstractApi(String entityName) {
    this.entityName = entityName;
  }

  /**
   * Create a ResponseEntity of a POST action.
   *
   * @param path the path
   * @param id the id of the resource created
   * @return ResponseEntity
   */
  protected ResponseEntity<Void> postResponse(final String path, final Long id) {
    // @formatter:off
    return ResponseEntity
        .created(createUri(path))
        .headers(HeaderUtil.createEntityCreationAlert(entityName, String.valueOf(id)))
        .build();
    // @formatter:on
  }

  /**
   * Create URI object from string.
   *
   * @param uri the URI in String to be converted
   * @return the URI created
   */
  public static URI createUri(String uri) {
    try {
      return new URI(uri);
    } catch (URISyntaxException e) {
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
    }
  }

  /**
   * Create a ResponseEntity of a PUT action.
   *
   * @param id the id of the resource updated
   * @return ResponseEntity
   */
  protected ResponseEntity<Void> putResponse(final Long id) {
    // @formatter:off
    return ResponseEntity
        .ok()
        .headers(HeaderUtil.createEntityUpdateAlert(entityName, String.valueOf(id)))
        .build();
    // @formatter:on
  }

  /**
   * Create a ResponseEntity of a pageable GET action.
   *
   * @param <T> type of the response
   * @param page the pagination information
   * @return ResponseEntity
   */
  protected <T> ResponseEntity<List<T>> getResponse(final Page<T> page) {
    return getResponse(entityName, page);
  }

  /**
   * Create a ResponseEntity of a pageable GET action.
   *
   * @param refEntityName the entity Name
   * @param <T> type of the response
   * @param page the pagination information
   * @return ResponseEntity
   */
  protected <T> ResponseEntity<List<T>> getResponse(final String refEntityName,
      final Page<T> page) {
    var headers = PaginationUtil.getHttpHeadersfromPagination(refEntityName, page);
    // @formatter:off
    return ResponseEntity
        .ok()
        .headers(headers)
        .body(page.getContent());
    // @formatter:on
  }

  /**
   * Create a ResponseEntity of a GET action with an OK status, or if it's empty, it returns a
   * ResponseEntity with NOT_FOUND.
   *
   * @param <T> type of the response
   * @param maybeResponse response to return if present
   * @return response containing {@code maybeResponse} if present or
   *         {@link org.springframework.http.HttpStatus#NOT_FOUND}
   */
  protected <T> ResponseEntity<T> getResponse(Optional<T> maybeResponse, Long id) {
    // @formatter:off
    return maybeResponse
        .map(response -> ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityGetAlert(entityName, String.valueOf(id)))
            .body(response))
        .orElseThrow(() -> new NotFoundAlertException(entityName, String.valueOf(id),
            String.format(NotFoundAlertException.MSG_NOT_FOUND_WITH_ID, entityName, id)));
    // @formatter:on
  }

  /**
   * Create a ResponseEntity of a DELETE action.
   *
   * @param id the id of the resource deleted
   * @return ResponseEntity
   */
  protected ResponseEntity<Void> deleteResponse(final Long id) {
    // @formatter:off
    return ResponseEntity
        .noContent()
        .headers(HeaderUtil.createEntityDeletionAlert(entityName, String.valueOf(id)))
        .build();
    // @formatter:on
  }
}
