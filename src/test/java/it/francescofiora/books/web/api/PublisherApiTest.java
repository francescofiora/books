package it.francescofiora.books.web.api;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.assertj.core.api.Assertions.assertThat;

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

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.francescofiora.books.service.PublisherService;
import it.francescofiora.books.service.dto.PublisherDto;
import it.francescofiora.books.service.dto.NewPublisherDto;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = PublisherApi.class)
public class PublisherApiTest {

  private static final Long ID = 1L;
  private static final String PUBLISHERS_URI = "/api/publishers";
  private static final String PUBLISHERS_ID_URI = "/api/publishers/{id}";
  private static final String WRONG_URI = "/api/wrong";

  @Autowired
  private MockMvc mvc;

  @MockBean
  private PublisherService publisherService;

  @Autowired
  private ObjectMapper mapper;

  @Test
  public void testCreatePublisher() throws Exception {
    NewPublisherDto newPublisherDto = new NewPublisherDto();
    fillPublisher(newPublisherDto);

    PublisherDto publisherDto = new PublisherDto();
    publisherDto.setId(ID);
    given(publisherService.create(any(NewPublisherDto.class))).willReturn(publisherDto);
    MvcResult result = mvc
        .perform(post(new URI(PUBLISHERS_URI)).contentType(APPLICATION_JSON)
            .content(mapper.writeValueAsString(newPublisherDto)))
        .andExpect(status().isCreated()).andReturn();
    assertThat(result.getResponse().getHeaderValue("location"))
        .isEqualTo(PUBLISHERS_URI + "/" + ID);
  }

  private void fillPublisher(NewPublisherDto publisherDto) {
    publisherDto.setPublisherName("Publisher Ltd");

  }

  @Test
  public void testUpdatePublisherBadRequest() throws Exception {
    PublisherDto publisherDto = new PublisherDto();
    fillPublisher(publisherDto);
    mvc.perform(put(new URI(PUBLISHERS_URI)).contentType(APPLICATION_JSON)
        .content(mapper.writeValueAsString(publisherDto))).andExpect(status().isBadRequest());
  }

  @Test
  public void testUpdatePublisher() throws Exception {
    PublisherDto publisherDto = new PublisherDto();
    fillPublisher(publisherDto);
    publisherDto.setId(ID);
    mvc.perform(put(new URI(PUBLISHERS_URI)).contentType(APPLICATION_JSON)
        .content(mapper.writeValueAsString(publisherDto))).andExpect(status().isOk());
  }

  @Test
  public void testGetAllPublishers() throws Exception {
    Pageable pageable = PageRequest.of(1, 1);
    PublisherDto expected = new PublisherDto();
    expected.setId(ID);
    given(publisherService.findAll(any(Pageable.class)))
        .willReturn(new PageImpl<PublisherDto>(Collections.singletonList(expected)));
    MvcResult result = mvc.perform(get(new URI(PUBLISHERS_URI)).contentType(APPLICATION_JSON)
        .content(mapper.writeValueAsString(pageable))).andExpect(status().isOk()).andReturn();
    List<PublisherDto> list = mapper.readValue(result.getResponse().getContentAsString(),
        new TypeReference<List<PublisherDto>>() {
        });
    assertThat(list).isNotNull().isNotEmpty();
    assertThat(list.get(0)).isEqualTo(expected);
  }

  @Test
  public void testGetPublisher() throws Exception {
    PublisherDto expected = new PublisherDto();
    expected.setId(ID);
    given(publisherService.findOne(eq(ID))).willReturn(Optional.of(expected));
    MvcResult result = mvc.perform(get(PUBLISHERS_ID_URI, ID)).andExpect(status().isOk())
        .andReturn();
    PublisherDto actual = mapper.readValue(result.getResponse().getContentAsString(),
        new TypeReference<PublisherDto>() {
        });
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
