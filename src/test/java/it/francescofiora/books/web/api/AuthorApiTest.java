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

import it.francescofiora.books.service.AuthorService;
import it.francescofiora.books.service.dto.AuthorDto;
import it.francescofiora.books.service.dto.NewAuthorDto;
import it.francescofiora.books.service.dto.TitleDto;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = AuthorApi.class)
public class AuthorApiTest extends AbstractApiTest {

  private static final Long ID = 1L;
  private static final String AUTHORS_URI = "/api/authors";
  private static final String AUTHORS_ID_URI = "/api/authors/{id}";
  private static final String AUTHORS_TITLES_URI = "/api/authors/{id}/titles";
  private static final String WRONG_URI = "/api/wrong";

  @Autowired
  private MockMvc mvc;

  @MockBean
  private AuthorService authorService;

  @Test
  public void testCreateAuthor() throws Exception {
    NewAuthorDto newAuthorDto = new NewAuthorDto();
    fillAuthor(newAuthorDto);

    AuthorDto authorDto = new AuthorDto();
    authorDto.setId(ID);
    given(authorService.create(any(NewAuthorDto.class))).willReturn(authorDto);
    MvcResult result = mvc.perform(post(new URI(AUTHORS_URI)).contentType(APPLICATION_JSON)
        .content(writeValueAsString(newAuthorDto))).andExpect(status().isCreated()).andReturn();
    assertThat(result.getResponse().getHeaderValue("location")).isEqualTo(AUTHORS_URI + "/" + ID);
  }

  @Test
  public void testUpdateAuthorBadRequest() throws Exception {
    // id
    AuthorDto authorDto = updateAuthorDto();
    authorDto.setId(null);
    mvc.perform(put(new URI(AUTHORS_URI)).contentType(APPLICATION_JSON)
        .content(writeValueAsString(authorDto))).andExpect(status().isBadRequest());

    // firstName
    authorDto = updateAuthorDto();
    authorDto.setFirstName(null);
    mvc.perform(put(new URI(AUTHORS_URI)).contentType(APPLICATION_JSON)
        .content(writeValueAsString(authorDto))).andExpect(status().isBadRequest());

    authorDto = updateAuthorDto();
    authorDto.setFirstName("");
    mvc.perform(put(new URI(AUTHORS_URI)).contentType(APPLICATION_JSON)
        .content(writeValueAsString(authorDto))).andExpect(status().isBadRequest());

    authorDto = updateAuthorDto();
    authorDto.setFirstName("  ");
    mvc.perform(put(new URI(AUTHORS_URI)).contentType(APPLICATION_JSON)
        .content(writeValueAsString(authorDto))).andExpect(status().isBadRequest());

    // lastName
    authorDto = updateAuthorDto();
    authorDto.setLastName(null);
    mvc.perform(put(new URI(AUTHORS_URI)).contentType(APPLICATION_JSON)
        .content(writeValueAsString(authorDto))).andExpect(status().isBadRequest());

    authorDto = updateAuthorDto();
    authorDto.setLastName("");
    mvc.perform(put(new URI(AUTHORS_URI)).contentType(APPLICATION_JSON)
        .content(writeValueAsString(authorDto))).andExpect(status().isBadRequest());

    authorDto = updateAuthorDto();
    authorDto.setLastName("  ");
    mvc.perform(put(new URI(AUTHORS_URI)).contentType(APPLICATION_JSON)
        .content(writeValueAsString(authorDto))).andExpect(status().isBadRequest());
  }

  @Test
  public void testUpdateAuthor() throws Exception {
    AuthorDto authorDto = updateAuthorDto();
    mvc.perform(put(new URI(AUTHORS_URI)).contentType(APPLICATION_JSON)
        .content(writeValueAsString(authorDto))).andExpect(status().isOk());
  }

  private AuthorDto updateAuthorDto() {
    AuthorDto authorDto = new AuthorDto();
    fillAuthor(authorDto);
    authorDto.setId(ID);
    return authorDto;
  }
  
  private void fillAuthor(NewAuthorDto authorDto) {
    authorDto.setFirstName("John");
    authorDto.setLastName("Smith");
  }

  @Test
  public void testGetAllAuthors() throws Exception {
    Pageable pageable = PageRequest.of(1, 1);
    AuthorDto expected = new AuthorDto();
    expected.setId(ID);
    given(authorService.findAll(any(Pageable.class)))
        .willReturn(new PageImpl<AuthorDto>(Collections.singletonList(expected)));

    MvcResult result = mvc.perform(get(new URI(AUTHORS_URI)).contentType(APPLICATION_JSON)
        .content(writeValueAsString(pageable))).andExpect(status().isOk()).andReturn();
    List<AuthorDto> list = readValue(result, new TypeReference<List<AuthorDto>>() {});
    assertThat(list).isNotNull().isNotEmpty();
    assertThat(list.get(0)).isEqualTo(expected);
  }

  @Test
  public void testGetAuthor() throws Exception {
    AuthorDto expected = new AuthorDto();
    expected.setId(ID);
    given(authorService.findOne(eq(ID))).willReturn(Optional.of(expected));
    MvcResult result = mvc.perform(get(AUTHORS_ID_URI, ID)).andExpect(status().isOk()).andReturn();
    AuthorDto actual = readValue(result, new TypeReference<AuthorDto>() {});
    assertThat(actual).isNotNull().isEqualTo(expected);
  }

  @Test
  public void testGetTitlesByAuthor() throws Exception {
    TitleDto expected = new TitleDto();
    expected.setId(ID);
    given(authorService.findTitlesByAuthorId(any(Pageable.class), eq(ID)))
        .willReturn(new PageImpl<TitleDto>(Collections.singletonList(expected)));

    Pageable pageable = PageRequest.of(1, 1);
    MvcResult result = mvc.perform(get(AUTHORS_TITLES_URI, ID).contentType(APPLICATION_JSON)
        .content(writeValueAsString(pageable))).andExpect(status().isOk()).andReturn();
    List<TitleDto> list = readValue(result, new TypeReference<List<TitleDto>>() {});
    assertThat(list).isNotNull().isNotEmpty();
    assertThat(list.get(0)).isEqualTo(expected);
  }

  @Test
  public void testDeleteAuthor() throws Exception {
    mvc.perform(delete(AUTHORS_ID_URI, ID)).andExpect(status().isNoContent()).andReturn();
  }

  @Test
  public void testWrongUri() throws Exception {
    mvc.perform(get(WRONG_URI)).andExpect(status().isNotFound()).andReturn();
  }
}
