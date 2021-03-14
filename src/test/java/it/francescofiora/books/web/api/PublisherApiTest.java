package it.francescofiora.books.web.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import it.francescofiora.books.service.PublisherService;
import it.francescofiora.books.service.dto.NewPublisherDto;
import it.francescofiora.books.service.dto.PublisherDto;
import it.francescofiora.books.util.TestUtils;
import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = PublisherApi.class)
public class PublisherApiTest extends AbstractApiTest {

  private static final Long ID = 1L;
  private static final String PUBLISHERS_URI = "/api/publishers";
  private static final String PUBLISHERS_ID_URI = "/api/publishers/{id}";
  private static final String WRONG_URI = "/api/wrong";

  @Autowired
  private MockMvc mvc;

  @MockBean
  private PublisherService publisherService;

  @Test
  public void testCreatePublisher() throws Exception {
    NewPublisherDto newPublisherDto = TestUtils.createNewPublisherDto();
    PublisherDto publisherDto = TestUtils.createPublisherDto(ID);
    given(publisherService.create(any(NewPublisherDto.class))).willReturn(publisherDto);
    MvcResult result = mvc
        .perform(post(new URI(PUBLISHERS_URI)).contentType(APPLICATION_JSON)
            .content(writeValueAsString(newPublisherDto)))
        .andExpect(status().isCreated()).andReturn();
    assertThat(result.getResponse().getHeaderValue("location"))
        .isEqualTo(PUBLISHERS_URI + "/" + ID);
  }

  @Test
  public void testUpdatePublisherBadRequest() throws Exception {
    PublisherDto publisherDto = TestUtils.createPublisherDto(null);
    mvc.perform(put(new URI(PUBLISHERS_URI)).contentType(APPLICATION_JSON)
        .content(writeValueAsString(publisherDto))).andExpect(status().isBadRequest());

    publisherDto = TestUtils.createPublisherDto(ID);
    publisherDto.setPublisherName(null);
    mvc.perform(put(new URI(PUBLISHERS_URI)).contentType(APPLICATION_JSON)
        .content(writeValueAsString(publisherDto))).andExpect(status().isBadRequest());

    publisherDto = TestUtils.createPublisherDto(ID);
    publisherDto.setPublisherName("");
    mvc.perform(put(new URI(PUBLISHERS_URI)).contentType(APPLICATION_JSON)
        .content(writeValueAsString(publisherDto))).andExpect(status().isBadRequest());

    publisherDto = TestUtils.createPublisherDto(ID);
    publisherDto.setPublisherName("  ");
    mvc.perform(put(new URI(PUBLISHERS_URI)).contentType(APPLICATION_JSON)
        .content(writeValueAsString(publisherDto))).andExpect(status().isBadRequest());
  }

  @Test
  public void testUpdatePublisher() throws Exception {
    PublisherDto publisherDto = TestUtils.createPublisherDto(ID);
    mvc.perform(put(new URI(PUBLISHERS_URI)).contentType(APPLICATION_JSON)
        .content(writeValueAsString(publisherDto))).andExpect(status().isOk());
  }

  @Test
  public void testGetAllPublishers() throws Exception {
    Pageable pageable = PageRequest.of(1, 1);
    PublisherDto expected = TestUtils.createPublisherDto(ID);
    given(publisherService.findAll(any(Pageable.class)))
        .willReturn(new PageImpl<PublisherDto>(Collections.singletonList(expected)));
    MvcResult result = mvc.perform(get(new URI(PUBLISHERS_URI)).contentType(APPLICATION_JSON)
        .content(writeValueAsString(pageable))).andExpect(status().isOk()).andReturn();
    List<PublisherDto> list = readValue(result, new TypeReference<List<PublisherDto>>() {});
    assertThat(list).isNotNull().isNotEmpty();
    assertThat(list.get(0)).isEqualTo(expected);
  }

  @Test
  public void testGetPublisher() throws Exception {
    PublisherDto expected = TestUtils.createPublisherDto(ID);
    given(publisherService.findOne(eq(ID))).willReturn(Optional.of(expected));
    MvcResult result =
        mvc.perform(get(PUBLISHERS_ID_URI, ID)).andExpect(status().isOk()).andReturn();
    PublisherDto actual = readValue(result, new TypeReference<PublisherDto>() {});
    assertThat(actual).isNotNull().isEqualTo(expected);
  }

  @Test
  public void testDeletePublisher() throws Exception {
    mvc.perform(delete(PUBLISHERS_ID_URI, ID)).andExpect(status().isNoContent()).andReturn();
  }

  @Test
  public void testWrongUri() throws Exception {
    mvc.perform(get(WRONG_URI)).andExpect(status().isNotFound()).andReturn();
  }
}
