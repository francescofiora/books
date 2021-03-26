package it.francescofiora.books.web.api;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

public abstract class AbstractTestApi {

  @Autowired
  private MockMvc mvc;

  @Autowired
  private ObjectMapper mapper;

  @Value("${spring.security.user.name}")
  private String user;

  @Value("${spring.security.user.password}")
  private String password;

  protected String writeValueAsString(Object value) throws JsonProcessingException {
    return mapper.writeValueAsString(value);
  }

  protected <T> T readValue(MvcResult result, TypeReference<T> valueTypeRef)
      throws JsonProcessingException, JsonMappingException, UnsupportedEncodingException {
    return mapper.readValue(result.getResponse().getContentAsString(), valueTypeRef);
  }

  protected ResultActions performPost(String path, Object content) throws Exception {
    // @formatter:off
    return mvc.perform(post(new URI(path))
        .contentType(APPLICATION_JSON)
        .headers(createHttpHeaders())
        .content(writeValueAsString(content)));
    // @formatter:on
  }

  protected ResultActions performPut(String path, Long id, Object content) throws Exception {
    // @formatter:off
    return mvc.perform(put(path, id)
        .contentType(APPLICATION_JSON)
        .headers(createHttpHeaders())
        .content(writeValueAsString(content)));
    // @formatter:on
  }

  protected ResultActions performGet(String path, Long id) throws Exception {
    return mvc.perform(get(path, id).headers(createHttpHeaders()));
  }

  protected ResultActions performGet(String path, Pageable pageable) throws Exception {
    // @formatter:off
    return mvc.perform(get(new URI(path))
        .headers(createHttpHeaders())
        .contentType(APPLICATION_JSON)
        .content(writeValueAsString(pageable)));
    // @formatter:on
  }

  protected ResultActions performGet(String path, Long id, Pageable pageable) throws Exception {
    // @formatter:off
    return mvc.perform(get(path, id)
        .headers(createHttpHeaders())
        .contentType(APPLICATION_JSON)
        .content(writeValueAsString(pageable)));
    // @formatter:on
  }

  protected ResultActions performGet(String path) throws Exception {
    return mvc.perform(get(new URI(path)).headers(createHttpHeaders()));
  }

  protected ResultActions performDelete(String path, Long id) throws Exception {
    return mvc.perform(delete(path, id).headers(createHttpHeaders()));
  }

  private HttpHeaders createHttpHeaders() {
    HttpHeaders headers = new HttpHeaders();
    headers.setBasicAuth(user, password);
    return headers;
  }
}
