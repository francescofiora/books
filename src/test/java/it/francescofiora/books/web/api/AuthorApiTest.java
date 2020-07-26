package it.francescofiora.books.web.api;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.francescofiora.books.service.AuthorService;
import it.francescofiora.books.service.dto.AuthorDto;
import it.francescofiora.books.service.dto.NewAuthorDto;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = AuthorApi.class)
public class AuthorApiTest {

  private static final Long ID = 1L;
  private static final String AUTHORS_URI = "/api/authors";
  private static final String AUTHORS_ID_URI = "/api/authors/{id}";
  private static final String WRONG_URI = "/api/wrong";

  @Autowired
  private MockMvc mvc;

  @MockBean
  private AuthorService authorService;

  @Autowired
  private ObjectMapper mapper;

  @Test
  public void testCreateAuthor() throws Exception {
    NewAuthorDto newAuthorDto = new NewAuthorDto();
    fillAuthor(newAuthorDto);

    AuthorDto authorDto = new AuthorDto();
    authorDto.setId(ID);
    given(authorService.create(any(NewAuthorDto.class))).willReturn(authorDto);
    MvcResult result = mvc
        .perform(post(new URI(AUTHORS_URI)).contentType(APPLICATION_JSON)
            .content(mapper.writeValueAsString(newAuthorDto)))
        .andExpect(status().isCreated()).andReturn();
    Assert.assertEquals(AUTHORS_URI + "/" + ID, result.getResponse().getHeaderValue("location"));
  }

  @Test
  public void testUpdateAuthorBadRequest() throws Exception {
    AuthorDto authorDto = new AuthorDto();
    fillAuthor(authorDto);
    mvc.perform(put(new URI(AUTHORS_URI)).contentType(APPLICATION_JSON)
        .content(mapper.writeValueAsString(authorDto))).andExpect(status().isBadRequest());
  }

  @Test
  public void testUpdateAuthor() throws Exception {
    AuthorDto authorDto = new AuthorDto();
    fillAuthor(authorDto);
    authorDto.setId(ID);
    mvc.perform(put(new URI(AUTHORS_URI)).contentType(APPLICATION_JSON)
        .content(mapper.writeValueAsString(authorDto))).andExpect(status().isOk());
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
        .content(mapper.writeValueAsString(pageable))).andExpect(status().isOk()).andReturn();
    List<AuthorDto> list = mapper.readValue(result.getResponse().getContentAsString(),
        new TypeReference<List<AuthorDto>>() {
        });
    Assert.assertNotNull(list);
    Assert.assertFalse(list.isEmpty());
    Assert.assertEquals(expected, list.get(0));
  }

  @Test
  public void testGetAuthor() throws Exception {
    AuthorDto expected = new AuthorDto();
    expected.setId(ID);
    given(authorService.findOne(eq(ID))).willReturn(Optional.of(expected));
    MvcResult result = mvc.perform(get(AUTHORS_ID_URI, ID)).andExpect(status().isOk()).andReturn();
    AuthorDto actual = mapper.readValue(result.getResponse().getContentAsString(),
        new TypeReference<AuthorDto>() {
        });
    Assert.assertNotNull(actual);
    Assert.assertEquals(expected, actual);
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
