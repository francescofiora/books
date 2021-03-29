package it.francescofiora.books.web.errors;

import static org.assertj.core.api.Assertions.assertThat;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;

public class CustomErrorControllerTest {

  @Test
  public void testgetStatus() {
    CustomErrorController ex = new CustomErrorController((x) -> null);

    HttpServletRequest request = Mockito.mock(HttpServletRequest.class);

    assertThat(ex.getStatus(request)).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);

    Mockito.when(request.getAttribute(Mockito.eq(RequestDispatcher.ERROR_STATUS_CODE)))
        .thenReturn(600);
    assertThat(ex.getStatus(request)).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
