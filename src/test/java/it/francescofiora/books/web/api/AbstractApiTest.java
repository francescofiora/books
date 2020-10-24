package it.francescofiora.books.web.api;

import java.io.UnsupportedEncodingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MvcResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class AbstractApiTest {

  @Autowired
  private ObjectMapper mapper;

  protected String writeValueAsString(Object value) throws JsonProcessingException {
    return mapper.writeValueAsString(value);
  }

  protected <T> T readValue(MvcResult result, TypeReference<T> valueTypeRef)
      throws JsonProcessingException, JsonMappingException, UnsupportedEncodingException {
    return mapper.readValue(result.getResponse().getContentAsString(), valueTypeRef);
  }
}
