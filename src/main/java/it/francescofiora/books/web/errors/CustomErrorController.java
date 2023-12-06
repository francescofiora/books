package it.francescofiora.books.web.errors;

import static org.springframework.boot.web.error.ErrorAttributeOptions.Include.BINDING_ERRORS;
import static org.springframework.boot.web.error.ErrorAttributeOptions.Include.EXCEPTION;
import static org.springframework.boot.web.error.ErrorAttributeOptions.Include.MESSAGE;

import io.swagger.v3.oas.annotations.Hidden;
import it.francescofiora.books.web.util.HeaderUtil;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;
import lombok.AllArgsConstructor;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.ServletWebRequest;

/**
 * Custom ErrorController.
 */
@Controller
@Hidden
@AllArgsConstructor
public class CustomErrorController implements ErrorController {

  private final ErrorAttributes errorAttributes;

  protected Map<String, Object> getErrorAttributes(HttpServletRequest request,
      ErrorAttributeOptions options) {
    var webRequest = new ServletWebRequest(request);
    return this.errorAttributes.getErrorAttributes(webRequest, options);
  }

  protected HttpStatus getStatus(HttpServletRequest request) {
    var statusCode = (Integer) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
    if (statusCode == null) {
      return HttpStatus.INTERNAL_SERVER_ERROR;
    }
    try {
      return HttpStatus.valueOf(statusCode);
    } catch (Exception ex) {
      return HttpStatus.INTERNAL_SERVER_ERROR;
    }
  }

  protected void appendMsg(StringBuilder sb, Map<String, Object> map, String[] names) {
    for (var name : names) {
      var msg = map.get(name);
      if (msg != null) {
        sb.append(msg.toString() + " ");
      }
    }
  }

  /**
   * Return the error in JSON format.
   *
   * @param request rest request
   * @return handle Error
   */
  @SuppressWarnings("squid:S3752")
  @RequestMapping(value = "{$errorPath}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Void> handleError(HttpServletRequest request) {
    var status = getStatus(request);
    var map =
        getErrorAttributes(request, ErrorAttributeOptions.of(MESSAGE, EXCEPTION, BINDING_ERRORS));

    var sb = new StringBuilder();
    sb.append(status + " - ");
    appendMsg(sb, map, new String[] {"exception", "error", "message"});
    var path = String.valueOf(map.get("path"));
    return ResponseEntity.status(status)
        .headers(HeaderUtil.createFailureAlert(status.toString(), path, sb.toString())).build();
  }
}
