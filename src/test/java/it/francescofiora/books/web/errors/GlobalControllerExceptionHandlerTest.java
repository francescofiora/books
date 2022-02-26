package it.francescofiora.books.web.errors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Executable;
import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/**
 * GlobalControllerExceptionHandler Test.
 */
class GlobalControllerExceptionHandlerTest {

  @Test
  void testHandleArgumentNotValid() {
    var exceptionHandler = new GlobalControllerExceptionHandler();
    var methodParameter = mock(MethodParameter.class);
    when(methodParameter.getExecutable()).thenReturn(mock(Executable.class));
    var ex = new MethodArgumentNotValidException(methodParameter, mock(BindingResult.class));
    var result = exceptionHandler.handleArgumentNotValid(ex);
    assertThat(result).isNotNull();
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
  }

  @Test
  void testHandleArgumentTypeMismatch() {
    var exceptionHandler = new GlobalControllerExceptionHandler();
    var ex = new MethodArgumentTypeMismatchException("", null, "name", mock(MethodParameter.class),
        mock(Throwable.class));
    var result = exceptionHandler.handleArgumentTypeMismatch(ex);
    assertThat(result).isNotNull();
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
  }
}
