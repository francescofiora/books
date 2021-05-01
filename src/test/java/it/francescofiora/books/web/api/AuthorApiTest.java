package it.francescofiora.books.web.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import it.francescofiora.books.service.AuthorService;
import it.francescofiora.books.service.dto.AuthorDto;
import it.francescofiora.books.service.dto.NewAuthorDto;
import it.francescofiora.books.service.dto.TitleDto;
import it.francescofiora.books.util.TestUtils;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MvcResult;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = AuthorApi.class)
@TestPropertySource(locations = {"classpath:application_test.properties"})
class AuthorApiTest extends AbstractTestApi {

  private static final Long ID = 1L;
  private static final String AUTHORS_URI = "/api/authors";
  private static final String AUTHORS_ID_URI = "/api/authors/{id}";
  private static final String AUTHORS_TITLES_URI = "/api/authors/{id}/titles";
  private static final String WRONG_URI = "/api/wrong";

  @MockBean
  private AuthorService authorService;

  @Test
  void testCreateAuthor() throws Exception {
    NewAuthorDto newAuthorDto = TestUtils.createNewAuthorDto();

    AuthorDto authorDto = TestUtils.createAuthorDto(ID);
    given(authorService.create(any(NewAuthorDto.class))).willReturn(authorDto);

    MvcResult result =
        performPost(AUTHORS_URI, newAuthorDto).andExpect(status().isCreated()).andReturn();

    assertThat(result.getResponse().getHeaderValue(HttpHeaders.LOCATION))
        .isEqualTo(AUTHORS_URI + "/" + ID);
  }

  @Test
  void testUpdateAuthorBadRequest() throws Exception {
    // id
    AuthorDto authorDto = TestUtils.createAuthorDto(null);
    performPut(AUTHORS_ID_URI, ID, authorDto).andExpect(status().isBadRequest());

    // firstName
    authorDto = TestUtils.createAuthorDto(ID);
    authorDto.setFirstName(null);
    performPut(AUTHORS_ID_URI, ID, authorDto).andExpect(status().isBadRequest());

    authorDto = TestUtils.createAuthorDto(ID);
    authorDto.setFirstName("");
    performPut(AUTHORS_ID_URI, ID, authorDto).andExpect(status().isBadRequest());

    authorDto = TestUtils.createAuthorDto(ID);
    authorDto.setFirstName("  ");
    performPut(AUTHORS_ID_URI, ID, authorDto).andExpect(status().isBadRequest());

    // lastName
    authorDto = TestUtils.createAuthorDto(ID);
    authorDto.setLastName(null);
    performPut(AUTHORS_ID_URI, ID, authorDto).andExpect(status().isBadRequest());

    authorDto = TestUtils.createAuthorDto(ID);
    authorDto.setLastName("");
    performPut(AUTHORS_ID_URI, ID, authorDto).andExpect(status().isBadRequest());

    authorDto = TestUtils.createAuthorDto(ID);
    authorDto.setLastName("  ");
    performPut(AUTHORS_ID_URI, ID, authorDto).andExpect(status().isBadRequest());
  }

  @Test
  void testUpdateAuthor() throws Exception {
    AuthorDto authorDto = TestUtils.createAuthorDto(ID);
    performPut(AUTHORS_ID_URI, ID, authorDto).andExpect(status().isOk());
  }

  @Test
  void testGetAllAuthors() throws Exception {
    Pageable pageable = PageRequest.of(1, 1);
    AuthorDto expected = TestUtils.createAuthorDto(ID);
    given(authorService.findAll(any(Pageable.class)))
        .willReturn(new PageImpl<AuthorDto>(Collections.singletonList(expected)));

    MvcResult result = performGet(AUTHORS_URI, pageable).andExpect(status().isOk()).andReturn();
    List<AuthorDto> list = readValue(result, new TypeReference<List<AuthorDto>>() {});
    assertThat(list).isNotNull().isNotEmpty();
    assertThat(list.get(0)).isEqualTo(expected);
  }

  @Test
  void testGetAuthor() throws Exception {
    AuthorDto expected = TestUtils.createAuthorDto(ID);
    given(authorService.findOne(eq(ID))).willReturn(Optional.of(expected));
    MvcResult result = performGet(AUTHORS_ID_URI, ID).andExpect(status().isOk()).andReturn();
    AuthorDto actual = readValue(result, new TypeReference<AuthorDto>() {});
    assertThat(actual).isNotNull().isEqualTo(expected);
  }

  @Test
  void testGetTitlesByAuthor() throws Exception {
    TitleDto expected = TestUtils.createTitleDto(ID);
    given(authorService.findTitlesByAuthorId(any(Pageable.class), eq(ID)))
        .willReturn(new PageImpl<TitleDto>(Collections.singletonList(expected)));

    Pageable pageable = PageRequest.of(1, 1);
    MvcResult result =
        performGet(AUTHORS_TITLES_URI, ID, pageable).andExpect(status().isOk()).andReturn();
    List<TitleDto> list = readValue(result, new TypeReference<List<TitleDto>>() {});
    assertThat(list).isNotNull().isNotEmpty();
    assertThat(list.get(0)).isEqualTo(expected);
  }

  @Test
  void testDeleteAuthor() throws Exception {
    performDelete(AUTHORS_ID_URI, ID).andExpect(status().isNoContent()).andReturn();
  }

  @Test
  void testWrongUri() throws Exception {
    performGet(WRONG_URI).andExpect(status().isNotFound()).andReturn();
  }
}
