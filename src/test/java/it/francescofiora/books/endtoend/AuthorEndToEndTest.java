package it.francescofiora.books.endtoend;

import static org.assertj.core.api.Assertions.assertThat;

import it.francescofiora.books.service.dto.AuthorDto;
import it.francescofiora.books.service.dto.NewAuthorDto;
import it.francescofiora.books.util.TestUtils;
import java.util.Optional;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = {"classpath:application_test.properties"})
public class AuthorEndToEndTest extends AbstractTestEndToEnd {

  private static final String AUTHORS_URI = "/api/authors";
  private static final String AUTHORS_ID_URI = "/api/authors/%d";

  @Test
  public void testAuth() throws Exception {
    testUnauthorized(AUTHORS_URI);
  }

  @Test
  public void testCreate() throws Exception {
    NewAuthorDto newAuthorDto = TestUtils.createNewAuthorDto();
    Long authorId = createResource(AUTHORS_URI, newAuthorDto, "Author.created");

    final String authorsIdUri = String.format(AUTHORS_ID_URI, authorId);

    AuthorDto authorDto = TestUtils.createAuthorDto(authorId);
    updateResource(authorsIdUri, authorDto, "Author.updated");

    AuthorDto actual = getResource(authorsIdUri, AuthorDto.class);
    assertThat(actual).isEqualTo(authorDto);

    AuthorDto[] authors = getResource(AUTHORS_URI, PageRequest.of(1, 1), AuthorDto[].class);
    assertThat(authors).isNotEmpty();
    Optional<AuthorDto> option =
        Stream.of(authors).filter(author -> author.getId().equals(authorId)).findAny();
    assertThat(option).isPresent();
    assertThat(option.get()).isEqualTo(authorDto);

    deleteResource(authorsIdUri);

    getResourceNotFound(authorsIdUri, AuthorDto.class);
  }

  @Test
  public void testUpdateAuthorBadRequest() throws Exception {
    // id
    updateResourceBadRequest(String.format(AUTHORS_ID_URI, 1L), TestUtils.createAuthorDto(null));

    Long id = createResource(AUTHORS_URI, TestUtils.createNewAuthorDto(), "Author.created");

    updateResourceBadRequest(String.format(AUTHORS_ID_URI, (id + 1)),
        TestUtils.createAuthorDto(id));

    final String path = String.format(AUTHORS_ID_URI, id);

    // firstName
    AuthorDto authorDto = TestUtils.createAuthorDto(id);
    authorDto.setFirstName(null);
    updateResourceBadRequest(path, authorDto);

    authorDto = TestUtils.createAuthorDto(id);
    authorDto.setFirstName("");
    updateResourceBadRequest(path, authorDto);

    authorDto = TestUtils.createAuthorDto(id);
    authorDto.setFirstName("  ");
    updateResourceBadRequest(path, authorDto);

    // lastName
    authorDto = TestUtils.createAuthorDto(id);
    authorDto.setLastName(null);
    updateResourceBadRequest(path, authorDto);

    authorDto = TestUtils.createAuthorDto(id);
    authorDto.setLastName("");
    updateResourceBadRequest(path, authorDto);

    authorDto = TestUtils.createAuthorDto(id);
    authorDto.setLastName("  ");
    updateResourceBadRequest(path, authorDto);
  }
}
