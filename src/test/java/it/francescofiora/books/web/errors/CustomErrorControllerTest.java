package it.francescofiora.books.web.errors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

public class CustomErrorControllerTest {

  @Test
  void testgetStatus() {
    CustomErrorController ex = new CustomErrorController((x) -> null);

    HttpServletRequest request = mock(HttpServletRequest.class);

    assertThat(ex.getStatus(request)).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);

    when(request.getAttribute(eq(RequestDispatcher.ERROR_STATUS_CODE)))
        .thenReturn(600);
    assertThat(ex.getStatus(request)).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
