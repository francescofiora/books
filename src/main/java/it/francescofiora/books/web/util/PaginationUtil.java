package it.francescofiora.books.web.util;

import java.text.MessageFormat;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * Pagination Util.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PaginationUtil {
  private static final String HEADER_X_TOTAL_COUNT = "X-Total-Count";
  private static final String HEADER_LINK_FORMAT = "<{0}>; rel=\"{1}\"";

  /**
   * Generate pagination headers for a Spring Data {@link org.springframework.data.domain.Page}
   * object.
   *
   * @param entityName the entity name
   * @param page The page
   * @param <T> The type of object
   * @return http header
   */
  public static <T> HttpHeaders getHttpHeadersfromPagination(String entityName, Page<T> page) {
    var uriBuilder = ServletUriComponentsBuilder.fromCurrentRequest();
    var pageNumber = page.getNumber();
    var pageSize = page.getSize();
    var headers = HeaderUtil.createEntityGetAlert(entityName, pageNumber + " " + pageSize);
    headers.add(HEADER_X_TOTAL_COUNT, Long.toString(page.getTotalElements()));
    var link = new StringBuilder();
    if (pageNumber < page.getTotalPages() - 1) {
      link.append(prepareLink(uriBuilder, pageNumber + 1, pageSize, "next")).append(",");
    }
    if (pageNumber > 0) {
      link.append(prepareLink(uriBuilder, pageNumber - 1, pageSize, "prev")).append(",");
    }
    link.append(prepareLink(uriBuilder, page.getTotalPages() - 1, pageSize, "last")).append(",")
        .append(prepareLink(uriBuilder, 0, pageSize, "first"));
    headers.add(HttpHeaders.LINK, link.toString());
    return headers;
  }

  private static String prepareLink(UriComponentsBuilder uriBuilder, int pageNumber, int pageSize,
      String relType) {
    return MessageFormat.format(HEADER_LINK_FORMAT,
        preparePageUri(uriBuilder, pageNumber, pageSize), relType);
  }

  private static String preparePageUri(UriComponentsBuilder uriBuilder, int pageNumber,
      int pageSize) {
    return uriBuilder.replaceQueryParam("page", Integer.toString(pageNumber))
        .replaceQueryParam("size", Integer.toString(pageSize)).toUriString().replace(",", "%2C")
        .replace(";", "%3B");
  }
}
