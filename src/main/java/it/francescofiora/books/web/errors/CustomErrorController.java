package it.francescofiora.books.web.errors;

import io.swagger.v3.oas.annotations.Hidden;
import it.francescofiora.books.web.util.HeaderUtil;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Hidden
public class CustomErrorController extends AbstractErrorController {
  private static final String PATH = "/error";

  public CustomErrorController(ErrorAttributes errorAttributes) {
    super(errorAttributes);
  }

  /**
   * return the error in JSON format.
   * 
   * @param request rest request
   * @return handle Error
   */
  @RequestMapping(value = PATH, produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  public ResponseEntity<Void> handleError(HttpServletRequest request) {
    HttpStatus status = super.getStatus(request);
    Map<String, Object> map = super.getErrorAttributes(request, ErrorAttributeOptions.defaults());

    StringBuilder sb = new StringBuilder();
    sb.append(status.value() + " - ");
    if (map.containsKey("error")) {
      sb.append(map.get("error") + " ");
    }
    if (map.containsKey("message")) {
      sb.append(map.get("message"));
    }

    String path = map.get("path").toString();

    return ResponseEntity.status(status).headers(HeaderUtil.createFailureAlert(path, sb.toString()))
        .build();
  }

  @Override
  public String getErrorPath() {
    return PATH;
  }
}