package it.francescofiora.books.web.errors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;

class CustomErrorControllerTest {

  @Test
  void testgetStatus() {
    CustomErrorController ex = new CustomErrorController((x) -> null);

    HttpServletRequest request = mock(HttpServletRequest.class);

    assertThat(ex.getStatus(request)).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);

    when(request.getAttribute(eq(RequestDispatcher.ERROR_STATUS_CODE))).thenReturn(600);
    assertThat(ex.getStatus(request)).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @Test
  void testHandleError() {
    ErrorAttributes errorAttributes = mock(ErrorAttributes.class);
    when(
        errorAttributes.getErrorAttributes(any(WebRequest.class), any(ErrorAttributeOptions.class)))
            .thenReturn(Collections.singletonMap("error", "Error Message"));
    CustomErrorController ex = new CustomErrorController(errorAttributes);

    HttpServletRequest request = mock(HttpServletRequest.class);

    ResponseEntity<Void> response = ex.handleError(request);
    assertThat(response.getHeaders().get("X-error"))
        .contains("500 INTERNAL_SERVER_ERROR - Error Message ");

    when(
        errorAttributes.getErrorAttributes(any(WebRequest.class), any(ErrorAttributeOptions.class)))
            .thenReturn(Collections.singletonMap("message", "Message"));

    response = ex.handleError(request);
    assertThat(response.getHeaders().get("X-error"))
        .contains("500 INTERNAL_SERVER_ERROR - Message");
  }

}
