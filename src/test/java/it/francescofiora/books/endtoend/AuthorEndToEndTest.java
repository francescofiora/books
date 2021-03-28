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

  private static final String ALERT_CREATED = "AuthorDto.created";
  private static final String ALERT_UPDATED = "AuthorDto.updated";
  private static final String ALERT_DELETED = "AuthorDto.deleted";
  private static final String ALERT_GET = "AuthorDto.get";
  private static final String ALERT_BAD_REQUEST = "AuthorDto.badRequest";
  private static final String ALERT_NOT_FOUND = "AuthorDto.notFound";

  @Test
  public void testAuth() throws Exception {
    testUnauthorized(AUTHORS_URI);
  }

  @Test
  public void testCreate() throws Exception {
    NewAuthorDto newAuthorDto = TestUtils.createNewAuthorDto();
    Long authorId = createAndReturnId(AUTHORS_URI, newAuthorDto, ALERT_CREATED);

    final String authorsIdUri = String.format(AUTHORS_ID_URI, authorId);

    AuthorDto authorDto = TestUtils.createAuthorDto(authorId);
    update(authorsIdUri, authorDto, ALERT_UPDATED);

    AuthorDto actual = get(authorsIdUri, AuthorDto.class, ALERT_GET);
    assertThat(actual).isEqualTo(authorDto);
    assertThat(actual.getFirstName()).isEqualTo(authorDto.getFirstName());
    assertThat(actual.getLastName()).isEqualTo(authorDto.getLastName());

    AuthorDto[] authors = get(AUTHORS_URI, PageRequest.of(1, 1), AuthorDto[].class, ALERT_GET);
    assertThat(authors).isNotEmpty();
    Optional<AuthorDto> option =
        Stream.of(authors).filter(author -> author.getId().equals(authorId)).findAny();
    assertThat(option).isPresent();
    assertThat(option.get()).isEqualTo(authorDto);

    delete(authorsIdUri, ALERT_DELETED);

    assertGetNotFound(authorsIdUri, AuthorDto.class, ALERT_NOT_FOUND);
  }

  @Test
  public void testGetAuthorBadRequest() throws Exception {
    assertGetBadRequest(AUTHORS_URI + "/999999999999999999999999", String.class, "id.badRequest");
  }

  @Test
  public void testUpdateAuthorBadRequest() throws Exception {
    // id
    assertUpdateBadRequest(String.format(AUTHORS_ID_URI, 1L), TestUtils.createAuthorDto(null),
        ALERT_BAD_REQUEST);

    Long id = createAndReturnId(AUTHORS_URI, TestUtils.createNewAuthorDto(), ALERT_CREATED);

    assertUpdateBadRequest(String.format(AUTHORS_ID_URI, (id + 1)), TestUtils.createAuthorDto(id),
        ALERT_BAD_REQUEST);

    final String path = String.format(AUTHORS_ID_URI, id);

    // firstName
    AuthorDto authorDto = TestUtils.createAuthorDto(id);
    authorDto.setFirstName(null);
    assertUpdateBadRequest(path, authorDto, ALERT_BAD_REQUEST);

    authorDto = TestUtils.createAuthorDto(id);
    authorDto.setFirstName("");
    assertUpdateBadRequest(path, authorDto, ALERT_BAD_REQUEST);

    authorDto = TestUtils.createAuthorDto(id);
    authorDto.setFirstName("  ");
    assertUpdateBadRequest(path, authorDto, ALERT_BAD_REQUEST);

    // lastName
    authorDto = TestUtils.createAuthorDto(id);
    authorDto.setLastName(null);
    assertUpdateBadRequest(path, authorDto, ALERT_BAD_REQUEST);

    authorDto = TestUtils.createAuthorDto(id);
    authorDto.setLastName("");
    assertUpdateBadRequest(path, authorDto, ALERT_BAD_REQUEST);

    authorDto = TestUtils.createAuthorDto(id);
    authorDto.setLastName("  ");
    assertUpdateBadRequest(path, authorDto, ALERT_BAD_REQUEST);
  }
}
