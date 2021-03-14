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
import it.francescofiora.books.service.TitleService;
import it.francescofiora.books.service.dto.NewTitleDto;
import it.francescofiora.books.service.dto.TitleDto;
import it.francescofiora.books.service.dto.UpdatebleTitleDto;
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
@WebMvcTest(controllers = TitleApi.class)
public class TitleApiTest extends AbstractApiTest {

  private static final Long ID = 1L;
  private static final String TITLES_URI = "/api/titles";
  private static final String TITLES_ID_URI = "/api/titles/{id}";
  private static final String WRONG_URI = "/api/wrong";

  @Autowired
  private MockMvc mvc;

  @MockBean
  private TitleService titleService;

  @Test
  public void testCreateTitle() throws Exception {
    NewTitleDto newTitleDto = TestUtils.createNewTitleDto();
    TitleDto titleDto = TestUtils.createTitleDto(ID);
    given(titleService.create(any(NewTitleDto.class))).willReturn(titleDto);
    MvcResult result = mvc.perform(post(new URI(TITLES_URI)).contentType(APPLICATION_JSON)
        .content(writeValueAsString(newTitleDto))).andExpect(status().isCreated()).andReturn();

    assertThat(result.getResponse().getHeaderValue("location")).isEqualTo(TITLES_URI + "/" + ID);
  }

  @Test
  public void testCreateTitleBadRequest() throws Exception {
    // Authors
    NewTitleDto titleDto = TestUtils.createNewTitleDto();
    titleDto.getAuthors().get(0).setId(null);
    mvc.perform(post(new URI(TITLES_URI)).contentType(APPLICATION_JSON)
        .content(writeValueAsString(titleDto))).andExpect(status().isBadRequest());

    titleDto = TestUtils.createNewTitleDto();
    titleDto.getAuthors().clear();
    mvc.perform(post(new URI(TITLES_URI)).contentType(APPLICATION_JSON)
        .content(writeValueAsString(titleDto))).andExpect(status().isBadRequest());

    titleDto = TestUtils.createNewTitleDto();
    titleDto.setAuthors(null);
    mvc.perform(post(new URI(TITLES_URI)).contentType(APPLICATION_JSON)
        .content(writeValueAsString(titleDto))).andExpect(status().isBadRequest());

    // Publisher
    titleDto = TestUtils.createNewTitleDto();
    titleDto.getPublisher().setId(null);
    mvc.perform(post(new URI(TITLES_URI)).contentType(APPLICATION_JSON)
        .content(writeValueAsString(titleDto))).andExpect(status().isBadRequest());

    titleDto = TestUtils.createNewTitleDto();
    titleDto.setPublisher(null);
    mvc.perform(post(new URI(TITLES_URI)).contentType(APPLICATION_JSON)
        .content(writeValueAsString(titleDto))).andExpect(status().isBadRequest());

    // copyright
    titleDto = TestUtils.createNewTitleDto();
    titleDto.setCopyright(null);
    mvc.perform(post(new URI(TITLES_URI)).contentType(APPLICATION_JSON)
        .content(writeValueAsString(titleDto))).andExpect(status().isBadRequest());

    // editionNumber
    titleDto = TestUtils.createNewTitleDto();
    titleDto.setEditionNumber(null);
    mvc.perform(post(new URI(TITLES_URI)).contentType(APPLICATION_JSON)
        .content(writeValueAsString(titleDto))).andExpect(status().isBadRequest());

    // language
    titleDto = TestUtils.createNewTitleDto();
    titleDto.setLanguage(null);
    mvc.perform(post(new URI(TITLES_URI)).contentType(APPLICATION_JSON)
        .content(writeValueAsString(titleDto))).andExpect(status().isBadRequest());

    // price
    titleDto = TestUtils.createNewTitleDto();
    titleDto.setPrice(null);
    mvc.perform(post(new URI(TITLES_URI)).contentType(APPLICATION_JSON)
        .content(writeValueAsString(titleDto))).andExpect(status().isBadRequest());

    titleDto = TestUtils.createNewTitleDto();
    titleDto.setPrice(0L);
    mvc.perform(post(new URI(TITLES_URI)).contentType(APPLICATION_JSON)
        .content(writeValueAsString(titleDto))).andExpect(status().isBadRequest());

    // title
    titleDto = TestUtils.createNewTitleDto();
    titleDto.setTitle(null);
    mvc.perform(post(new URI(TITLES_URI)).contentType(APPLICATION_JSON)
        .content(writeValueAsString(titleDto))).andExpect(status().isBadRequest());

    titleDto = TestUtils.createNewTitleDto();
    titleDto.setTitle("  ");
    mvc.perform(post(new URI(TITLES_URI)).contentType(APPLICATION_JSON)
        .content(writeValueAsString(titleDto))).andExpect(status().isBadRequest());
  }

  @Test
  public void testUpdateTitleBadRequest() throws Exception {
    // Authors
    UpdatebleTitleDto titleDto = TestUtils.createUpdatebleTitleDto(ID);
    titleDto.getAuthors().get(0).setId(null);
    mvc.perform(put(new URI(TITLES_URI)).contentType(APPLICATION_JSON)
        .content(writeValueAsString(titleDto))).andExpect(status().isBadRequest());

    titleDto = TestUtils.createUpdatebleTitleDto(ID);
    titleDto.getAuthors().clear();
    mvc.perform(put(new URI(TITLES_URI)).contentType(APPLICATION_JSON)
        .content(writeValueAsString(titleDto))).andExpect(status().isBadRequest());

    titleDto = TestUtils.createUpdatebleTitleDto(ID);
    titleDto.setAuthors(null);
    mvc.perform(put(new URI(TITLES_URI)).contentType(APPLICATION_JSON)
        .content(writeValueAsString(titleDto))).andExpect(status().isBadRequest());

    // Publisher
    titleDto = TestUtils.createUpdatebleTitleDto(ID);
    titleDto.getPublisher().setId(null);
    mvc.perform(put(new URI(TITLES_URI)).contentType(APPLICATION_JSON)
        .content(writeValueAsString(titleDto))).andExpect(status().isBadRequest());

    titleDto = TestUtils.createUpdatebleTitleDto(ID);
    titleDto.setPublisher(null);
    mvc.perform(put(new URI(TITLES_URI)).contentType(APPLICATION_JSON)
        .content(writeValueAsString(titleDto))).andExpect(status().isBadRequest());

    // copyright
    titleDto = TestUtils.createUpdatebleTitleDto(ID);
    titleDto.setCopyright(null);
    mvc.perform(put(new URI(TITLES_URI)).contentType(APPLICATION_JSON)
        .content(writeValueAsString(titleDto))).andExpect(status().isBadRequest());

    // editionNumber
    titleDto = TestUtils.createUpdatebleTitleDto(ID);
    titleDto.setEditionNumber(null);
    mvc.perform(put(new URI(TITLES_URI)).contentType(APPLICATION_JSON)
        .content(writeValueAsString(titleDto))).andExpect(status().isBadRequest());

    // id
    titleDto = TestUtils.createUpdatebleTitleDto(ID);
    titleDto.setId(null);
    mvc.perform(put(new URI(TITLES_URI)).contentType(APPLICATION_JSON)
        .content(writeValueAsString(titleDto))).andExpect(status().isBadRequest());

    // language
    titleDto = TestUtils.createUpdatebleTitleDto(ID);
    titleDto.setLanguage(null);
    mvc.perform(put(new URI(TITLES_URI)).contentType(APPLICATION_JSON)
        .content(writeValueAsString(titleDto))).andExpect(status().isBadRequest());

    // price
    titleDto = TestUtils.createUpdatebleTitleDto(ID);
    titleDto.setPrice(null);
    mvc.perform(put(new URI(TITLES_URI)).contentType(APPLICATION_JSON)
        .content(writeValueAsString(titleDto))).andExpect(status().isBadRequest());

    titleDto = TestUtils.createUpdatebleTitleDto(ID);
    titleDto.setPrice(0L);
    mvc.perform(put(new URI(TITLES_URI)).contentType(APPLICATION_JSON)
        .content(writeValueAsString(titleDto))).andExpect(status().isBadRequest());

    // title
    titleDto = TestUtils.createUpdatebleTitleDto(ID);
    titleDto.setTitle(null);
    mvc.perform(put(new URI(TITLES_URI)).contentType(APPLICATION_JSON)
        .content(writeValueAsString(titleDto))).andExpect(status().isBadRequest());

    titleDto = TestUtils.createUpdatebleTitleDto(ID);
    titleDto.setTitle("  ");
    mvc.perform(put(new URI(TITLES_URI)).contentType(APPLICATION_JSON)
        .content(writeValueAsString(titleDto))).andExpect(status().isBadRequest());
  }

  @Test
  public void testUpdateTitle() throws Exception {
    UpdatebleTitleDto titleDto = TestUtils.createUpdatebleTitleDto(ID);
    mvc.perform(put(new URI(TITLES_URI)).contentType(APPLICATION_JSON)
        .content(writeValueAsString(titleDto))).andExpect(status().isOk());
  }

  @Test
  public void testGetAllTitles() throws Exception {
    Pageable pageable = PageRequest.of(1, 1);
    TitleDto expected = new TitleDto();
    expected.setId(ID);
    given(titleService.findAll(any(Pageable.class)))
        .willReturn(new PageImpl<TitleDto>(Collections.singletonList(expected)));
    MvcResult result = mvc.perform(get(new URI(TITLES_URI)).contentType(APPLICATION_JSON)
        .content(writeValueAsString(pageable))).andExpect(status().isOk()).andReturn();
    List<TitleDto> list = readValue(result, new TypeReference<List<TitleDto>>() {});
    assertThat(list).isNotNull().isNotEmpty();
    assertThat(list.get(0)).isEqualTo(expected);
  }

  @Test
  public void testGetTitle() throws Exception {
    TitleDto expected = new TitleDto();
    expected.setId(ID);
    given(titleService.findOne(eq(ID))).willReturn(Optional.of(expected));
    MvcResult result = mvc.perform(get(TITLES_ID_URI, ID)).andExpect(status().isOk()).andReturn();
    TitleDto actual = readValue(result, new TypeReference<TitleDto>() {});
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
