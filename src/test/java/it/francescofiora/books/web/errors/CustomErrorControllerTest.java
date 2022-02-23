package it.francescofiora.books.web.errors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Map;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.WebRequest;

class CustomErrorControllerTest {

  @Test
  void testgetStatus() {
    var ex = new CustomErrorController((x) -> null);

    var request = mock(HttpServletRequest.class);

    assertThat(ex.getStatus(request)).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);

    when(request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE)).thenReturn(400);
    assertThat(ex.getStatus(request)).isEqualTo(HttpStatus.BAD_REQUEST);

    when(request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE)).thenReturn(600);
    assertThat(ex.getStatus(request)).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @Test
  void testHandleError() {
    var errorAttributes = mock(ErrorAttributes.class);
    when(
        errorAttributes.getErrorAttributes(any(WebRequest.class), any(ErrorAttributeOptions.class)))
            .thenReturn(Map.of("error", "Error Message"));
    var ex = new CustomErrorController(errorAttributes);

    var request = mock(HttpServletRequest.class);

    var response = ex.handleError(request);
    assertThat(response.getHeaders().get("X-error"))
        .contains("500 INTERNAL_SERVER_ERROR - Error Message ");

    when(
        errorAttributes.getErrorAttributes(any(WebRequest.class), any(ErrorAttributeOptions.class)))
            .thenReturn(Map.of("message", "Message"));

    response = ex.handleError(request);
    assertThat(response.getHeaders().get("X-error"))
        .contains("500 INTERNAL_SERVER_ERROR - Message");
  }
}
