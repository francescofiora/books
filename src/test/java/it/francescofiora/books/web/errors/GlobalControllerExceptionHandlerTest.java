package it.francescofiora.books.web.errors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import java.util.HashMap;
import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.validation.MapBindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/**
 * GlobalControllerExceptionHandler Test.
 */
class GlobalControllerExceptionHandlerTest {

  @Test
  void testHandleArgumentNotValid() throws NoSuchMethodException, SecurityException {
    var bindingResult = new MapBindingResult(new HashMap<>(), "objectName");
    bindingResult.addError(new FieldError("objectName", "field1", "message"));
    bindingResult.addError(new FieldError("objectName", "field2", "message"));
    var method = this.getClass().getMethod("toString", (Class<?>[]) null);
    var parameter = new MethodParameter(method, -1);
    var ex = new MethodArgumentNotValidException(parameter, bindingResult);
    var exceptionHandler = new GlobalControllerExceptionHandler();
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
