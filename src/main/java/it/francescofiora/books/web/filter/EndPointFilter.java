package it.francescofiora.books.web.filter;

import java.io.IOException;
import java.util.UUID;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.boot.info.BuildProperties;
import org.springframework.stereotype.Component;

/**
 * EndPoint Filter.
 */
@Component
@RequiredArgsConstructor
public class EndPointFilter implements Filter {

  public static final String X_REQUEST_ID = "X-request-id";
  public static final String X_NAME = "X-name";
  public static final String X_VERSION = "X-version";

  private final BuildProperties buildProperties;

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {

    if (request instanceof HttpServletRequest && response instanceof HttpServletResponse) {
      var req = (HttpServletRequest) request;
      var requestId = req.getHeader(X_REQUEST_ID);
      if (requestId == null) {
        requestId = UUID.randomUUID().toString();
      }
      MDC.put(X_REQUEST_ID, requestId);

      var res = (HttpServletResponse) response;
      res.addHeader(X_NAME, buildProperties.getName());
      res.addHeader(X_VERSION, buildProperties.getVersion());
      res.addHeader(X_REQUEST_ID, requestId);
    }
    
    chain.doFilter(request, response);

    MDC.put(X_REQUEST_ID, "");
  }
}
