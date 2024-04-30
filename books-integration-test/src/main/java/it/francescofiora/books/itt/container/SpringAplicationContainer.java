package it.francescofiora.books.itt.container;

import java.net.URI;
import java.net.URISyntaxException;
import lombok.Setter;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.testcontainers.containers.GenericContainer;

/**
 * Container of a SpringAplication.
 */
public class SpringAplicationContainer extends GenericContainer<SpringAplicationContainer> {

  @Setter
  private String token;

  private final RestTemplate rest = new RestTemplate();

  public SpringAplicationContainer(String dockerImageName) {
    super(dockerImageName);
  }

  /**
   * Perform Login.
   *
   * @param jsonBody the json body
   * @return the token
   */
  public ResponseEntity<String> performLogin(final String jsonBody) {
    var headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    var request = new HttpEntity<>(jsonBody, headers);
    var path = "/api/v1/auth/login";
    return rest.postForEntity(createUri(getHttpPath(path)), request, String.class);
  }

  /**
   * Create HttpHeaders.
   *
   * @return HttpHeaders
   */
  public HttpHeaders createHttpHeaders() {
    var headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.add("Authorization", "Bearer " + token);
    return headers;
  }

  public String getHttpPath(String path) {
    return "http://" + getHost() + ":" + getFirstMappedPort() + path;
  }

  public Long createAndReturnId(String path, String jsonBody) {
    var result = performPost(path, jsonBody);
    return getIdFormHttpHeaders(result.getHeaders());
  }

  private Long getIdFormHttpHeaders(HttpHeaders headers) {
    var url = headers.get(HttpHeaders.LOCATION).get(0);
    return Long.valueOf(url.substring(url.lastIndexOf('/') + 1));
  }

  public ResponseEntity<String> performGet(String path) {
    var request = new HttpEntity<>(createHttpHeaders());
    return rest.exchange(getHttpPath(path), HttpMethod.GET, request, String.class);
  }

  public ResponseEntity<String> performGet(String path, Pageable pageable) {
    var request = new HttpEntity<>(pageable, createHttpHeaders());
    return rest.exchange(getHttpPath(path), HttpMethod.GET, request, String.class);
  }

  public ResponseEntity<Void> performDelete(String path) {
    var request = new HttpEntity<>(createHttpHeaders());
    return rest.exchange(getHttpPath(path), HttpMethod.DELETE, request, Void.class);
  }

  public ResponseEntity<Void> performPost(String path, String jsonBody) {
    var request = new HttpEntity<>(jsonBody, createHttpHeaders());
    return rest.postForEntity(createUri(getHttpPath(path)), request, Void.class);
  }

  public ResponseEntity<Void> performPatch(String path, String jsonBody) {
    var request = new HttpEntity<>(jsonBody, createHttpHeaders());
    return rest.exchange(getHttpPath(path), HttpMethod.PATCH, request, Void.class);
  }

  public ResponseEntity<Void> performPut(String path, String jsonBody) {
    var request = new HttpEntity<>(jsonBody, createHttpHeaders());
    return rest.exchange(getHttpPath(path), HttpMethod.PUT, request, Void.class);
  }

  private URI createUri(String uri) {
    try {
      return new URI(uri);
    } catch (URISyntaxException e) {
      throw new RuntimeException(e);
    }
  }
}
