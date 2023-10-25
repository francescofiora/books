package it.francescofiora.books.web.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

class PaginationUtilTest {

  private static final String NAME = "name";
  private static final String HEADER_X_TOTAL_COUNT = "X-Total-Count";
  private static final String HEADER_LINK_0_1 = "<?page=1&size=1>; rel=\"next\","
      + "<?page=99&size=1>; rel=\"last\",<?page=0&size=1>; rel=\"first\"";
  private static final String HEADER_LINK_9_10 = "<?page=8&size=10>; rel=\"prev\","
      + "<?page=9&size=10>; rel=\"last\",<?page=0&size=10>; rel=\"first\"";

  private static final int TOTAL_COUNTS = 100;

  @Test
  void testgetHttpHeadersfromPagination1() {
    testgetHttpHeadersfromPagination(0, 1, HEADER_LINK_0_1);
  }

  @Test
  void testgetHttpHeadersfromPagination2() {
    testgetHttpHeadersfromPagination(9, 10, HEADER_LINK_9_10);
  }

  @Test
  void testConstructorIsPrivate() throws NoSuchMethodException, IllegalAccessException,
      InvocationTargetException, InstantiationException {
    var constructor = PaginationUtil.class.getDeclaredConstructor();
    assertTrue(Modifier.isPrivate(constructor.getModifiers()));
    constructor.setAccessible(true);
    constructor.newInstance();
  }

  private void testgetHttpHeadersfromPagination(int page, int size, String headerLink) {
    var attributes = new ServletRequestAttributes(mock(HttpServletRequest.class));
    RequestContextHolder.setRequestAttributes(attributes);

    var pageImpl = new PageImpl<String>(List.of(), PageRequest.of(page, size), TOTAL_COUNTS);
    var result = PaginationUtil.getHttpHeadersfromPagination(NAME, pageImpl);

    checkHeader(result, HeaderUtil.X_ALERT, NAME + ".get");
    checkHeader(result, HeaderUtil.X_PARAMS, page + " " + size);
    checkHeader(result, HEADER_X_TOTAL_COUNT, String.valueOf(TOTAL_COUNTS));
    checkHeader(result, HttpHeaders.LINK, headerLink);
  }

  private void checkHeader(HttpHeaders result, String key, String value) {
    assertThat(result.get(key)).hasSize(1);
    assertThat(result.get(key).get(0)).isEqualTo(value);
  }
}
