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

import it.francescofiora.books.domain.enumeration.Language;
import it.francescofiora.books.service.TitleService;
import it.francescofiora.books.service.dto.TitleDto;
import it.francescofiora.books.service.dto.BaseTitleDto;
import it.francescofiora.books.service.dto.NewTitleDto;
import it.francescofiora.books.service.dto.RefPublisherDto;
import it.francescofiora.books.service.dto.UpdatebleTitleDto;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = TitleApi.class)
public class TitleApiTest {

  private static final Long ID = 1L;
  private static final String TITLES_URI = "/api/titles";
  private static final String TITLES_ID_URI = "/api/titles/{id}";
  private static final String WRONG_URI = "/api/wrong";

  @Autowired
  private MockMvc mvc;

  @MockBean
  private TitleService titleService;

  @Autowired
  private ObjectMapper mapper;

  @Test
  public void testCreateTitle() throws Exception {
    NewTitleDto newTitleDto = new NewTitleDto();
    fillTitle(newTitleDto);
    newTitleDto.setPublisher(new RefPublisherDto());
    newTitleDto.getPublisher().setId(1L);
    TitleDto titleDto = new TitleDto();
    titleDto.setId(ID);
    given(titleService.create(any(NewTitleDto.class))).willReturn(titleDto);
    MvcResult result = mvc
        .perform(post(new URI(TITLES_URI)).contentType(APPLICATION_JSON)
            .content(mapper.writeValueAsString(newTitleDto)))
        .andExpect(status().isCreated()).andReturn();

    assertThat(result.getResponse().getHeaderValue("location")).isEqualTo(TITLES_URI + "/" + ID);
  }

  private void fillTitle(BaseTitleDto titleDto) {
    titleDto.setLanguage(Language.ENGLISH);
    titleDto.setEditionNumber(1L);
    titleDto.setCopyright(2020);
    titleDto.setTitle("My Book");
    titleDto.setPrice(10L);
  }

  @Test
  public void testUpdateTitleBadRequest() throws Exception {
    UpdatebleTitleDto titleDto = new UpdatebleTitleDto();
    fillTitle(titleDto);
    titleDto.setPublisher(new RefPublisherDto());
    titleDto.getPublisher().setId(1L);
    mvc.perform(put(new URI(TITLES_URI)).contentType(APPLICATION_JSON)
        .content(mapper.writeValueAsString(titleDto))).andExpect(status().isBadRequest());
  }

  @Test
  public void testUpdateTitle() throws Exception {
    UpdatebleTitleDto titleDto = new UpdatebleTitleDto();
    fillTitle(titleDto);
    titleDto.setPublisher(new RefPublisherDto());
    titleDto.getPublisher().setId(1L);
    titleDto.setId(ID);
    mvc.perform(put(new URI(TITLES_URI)).contentType(APPLICATION_JSON)
        .content(mapper.writeValueAsString(titleDto))).andExpect(status().isOk());
  }

  @Test
  public void testGetAllTitles() throws Exception {
    Pageable pageable = PageRequest.of(1, 1);
    TitleDto expected = new TitleDto();
    expected.setId(ID);
    given(titleService.findAll(any(Pageable.class)))
        .willReturn(new PageImpl<TitleDto>(Collections.singletonList(expected)));
    MvcResult result = mvc.perform(get(new URI(TITLES_URI)).contentType(APPLICATION_JSON)
        .content(mapper.writeValueAsString(pageable))).andExpect(status().isOk()).andReturn();
    List<TitleDto> list = mapper.readValue(result.getResponse().getContentAsString(),
        new TypeReference<List<TitleDto>>() {
        });
    assertThat(list).isNotNull().isNotEmpty();
    assertThat(list.get(0)).isEqualTo(expected);
  }

  @Test
  public void testGetTitle() throws Exception {
    TitleDto expected = new TitleDto();
    expected.setId(ID);
    given(titleService.findOne(eq(ID))).willReturn(Optional.of(expected));
    MvcResult result = mvc.perform(get(TITLES_ID_URI, ID)).andExpect(status().isOk()).andReturn();
    TitleDto actual = mapper.readValue(result.getResponse().getContentAsString(),
        new TypeReference<TitleDto>() {
        });
    assertThat(actual).isNotNull().isEqualTo(expected);
  }

  @Test
  public void testDeleteTitle() throws Exception {
    mvc.perform(delete(TITLES_ID_URI, ID)).andExpect(status().isNoContent()).andReturn();
  }

  @Test
  public void testWrongUri() throws Exception {
    mvc.perform(get(WRONG_URI)).andExpect(status().isNotFound()).andReturn();
  }
}
