package it.francescofiora.books.web.errors;

import io.swagger.v3.oas.annotations.Hidden;
import it.francescofiora.books.web.util.HeaderUtil;
import java.util.Map;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

@Controller
@Hidden
public class CustomErrorController implements ErrorController {

  private final ErrorAttributes errorAttributes;

  public CustomErrorController(ErrorAttributes errorAttributes) {
    this.errorAttributes = errorAttributes;
  }

  protected Map<String, Object> getErrorAttributes(HttpServletRequest request,
      ErrorAttributeOptions options) {
    WebRequest webRequest = new ServletWebRequest(request);
    return this.errorAttributes.getErrorAttributes(webRequest, options);
  }

  protected HttpStatus getStatus(HttpServletRequest request) {
    Integer statusCode = (Integer) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
    if (statusCode == null) {
      return HttpStatus.INTERNAL_SERVER_ERROR;
    }
    try {
      return HttpStatus.valueOf(statusCode);
    } catch (Exception ex) {
      return HttpStatus.INTERNAL_SERVER_ERROR;
    }
  }

  /**
   * Return the error in JSON format.
   * 
   * @param request rest request
   * @return handle Error
   */
  @RequestMapping(value = "{$server.error.path}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Void> handleError(HttpServletRequest request) {
    HttpStatus status = getStatus(request);
    Map<String, Object> map = getErrorAttributes(request, ErrorAttributeOptions.defaults());

    StringBuilder sb = new StringBuilder();
    sb.append(status + " - ");
    final Object error = map.get("error");
    if (error != null) {
      sb.append(error.toString() + " ");
    }
    final Object message = map.get("message");
    if (message != null) {
      sb.append(message.toString());
    }

    String path = String.valueOf(map.get("path"));

    return ResponseEntity.status(status)
        .headers(HeaderUtil.createFailureAlert(status.toString(), path, sb.toString())).build();
  }
}
