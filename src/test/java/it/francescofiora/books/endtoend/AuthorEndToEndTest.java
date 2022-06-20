package it.francescofiora.books.endtoend;

import static org.assertj.core.api.Assertions.assertThat;

import it.francescofiora.books.service.dto.AuthorDto;
import it.francescofiora.books.util.TestUtils;
import it.francescofiora.books.util.UserUtils;
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
class AuthorEndToEndTest extends AbstractTestEndToEnd {

  private static final String AUTHORS_URI = "/books/api/v1/authors";
  private static final String AUTHORS_ID_URI = "/books/api/v1/authors/%d";

  private static final String ALERT_CREATED = "AuthorDto.created";
  private static final String ALERT_UPDATED = "AuthorDto.updated";
  private static final String ALERT_DELETED = "AuthorDto.deleted";
  private static final String ALERT_GET = "AuthorDto.get";
  private static final String ALERT_BAD_REQUEST = "AuthorDto.badRequest";
  private static final String ALERT_NOT_FOUND = "AuthorDto.notFound";

  private static final String PARAM_PAGE_20 = "0 20";
  private static final String PARAM_NOT_VALID_LONG =
      "'id' should be a valid 'Long' and '999999999999999999999999' isn't";

  private static final String PARAM_ID_NOT_NULL = "[authorDto.id - NotNull]";
  private static final String PARAM_FIRST_NAME_NOT_BLANK = "[authorDto.firstName - NotBlank]";
  private static final String PARAM_LAST_NAME_NOT_BLANK = "[authorDto.lastName - NotBlank]";

  @Test
  void testAuth() throws Exception {
    testUnauthorized(AUTHORS_URI);
  }

  @Test
  void testCreate() throws Exception {
    var newAuthorDto = TestUtils.createNewAuthorDto();
    var authorId =
        createAndReturnId(UserUtils.BOOK_ADMIN, AUTHORS_URI, newAuthorDto, ALERT_CREATED);

    final var authorsIdUri = String.format(AUTHORS_ID_URI, authorId);

    var actual = get(UserUtils.BOOK_ADMIN, authorsIdUri, AuthorDto.class, ALERT_GET,
        String.valueOf(authorId));
    assertThat(actual.getFirstName()).isEqualTo(newAuthorDto.getFirstName());
    assertThat(actual.getLastName()).isEqualTo(newAuthorDto.getLastName());

    var authorDto = TestUtils.createAuthorDto(authorId);
    update(UserUtils.BOOK_ADMIN, authorsIdUri, authorDto, ALERT_UPDATED, String.valueOf(authorId));

    actual = get(UserUtils.BOOK_ADMIN, authorsIdUri, AuthorDto.class, ALERT_GET,
        String.valueOf(authorId));
    assertThat(actual).isEqualTo(authorDto);
    assertThat(actual.getFirstName()).isEqualTo(authorDto.getFirstName());
    assertThat(actual.getLastName()).isEqualTo(authorDto.getLastName());

    var authors = get(UserUtils.BOOK_ADMIN, AUTHORS_URI, PageRequest.of(1, 1), AuthorDto[].class,
        ALERT_GET, PARAM_PAGE_20);
    assertThat(authors).isNotEmpty();
    var option = Stream.of(authors).filter(author -> author.getId().equals(authorId)).findAny();
    assertThat(option).isPresent().contains(authorDto);

    delete(UserUtils.BOOK_ADMIN, authorsIdUri, ALERT_DELETED, String.valueOf(authorId));

    assertGetNotFound(UserUtils.BOOK_ADMIN, authorsIdUri, AuthorDto.class, ALERT_NOT_FOUND,
        String.valueOf(authorId));
  }

  @Test
  void testGetBadRequest() throws Exception {
    assertGetBadRequest(UserUtils.BOOK_ADMIN, AUTHORS_URI + "/999999999999999999999999",
        String.class, "id.badRequest", PARAM_NOT_VALID_LONG);
  }

  @Test
  void testUpdateBadRequest() throws Exception {
    // id
    assertUpdateBadRequest(UserUtils.BOOK_ADMIN, String.format(AUTHORS_ID_URI, 1L),
        TestUtils.createAuthorDto(null), ALERT_BAD_REQUEST, PARAM_ID_NOT_NULL);

    var id = createAndReturnId(UserUtils.BOOK_ADMIN, AUTHORS_URI, TestUtils.createNewAuthorDto(),
        ALERT_CREATED);

    assertUpdateBadRequest(UserUtils.BOOK_ADMIN, String.format(AUTHORS_ID_URI, (id + 1)),
        TestUtils.createAuthorDto(id), ALERT_BAD_REQUEST, String.valueOf(id));

    final var path = String.format(AUTHORS_ID_URI, id);

    // firstName
    var authorDto = TestUtils.createAuthorDto(id);
    authorDto.setFirstName(null);
    assertUpdateBadRequest(UserUtils.BOOK_ADMIN, path, authorDto, ALERT_BAD_REQUEST,
        PARAM_FIRST_NAME_NOT_BLANK);

    authorDto = TestUtils.createAuthorDto(id);
    authorDto.setFirstName("");
    assertUpdateBadRequest(UserUtils.BOOK_ADMIN, path, authorDto, ALERT_BAD_REQUEST,
        PARAM_FIRST_NAME_NOT_BLANK);

    authorDto = TestUtils.createAuthorDto(id);
    authorDto.setFirstName("  ");
    assertUpdateBadRequest(UserUtils.BOOK_ADMIN, path, authorDto, ALERT_BAD_REQUEST,
        PARAM_FIRST_NAME_NOT_BLANK);

    // lastName
    authorDto = TestUtils.createAuthorDto(id);
    authorDto.setLastName(null);
    assertUpdateBadRequest(UserUtils.BOOK_ADMIN, path, authorDto, ALERT_BAD_REQUEST,
        PARAM_LAST_NAME_NOT_BLANK);

    authorDto = TestUtils.createAuthorDto(id);
    authorDto.setLastName("");
    assertUpdateBadRequest(UserUtils.BOOK_ADMIN, path, authorDto, ALERT_BAD_REQUEST,
        PARAM_LAST_NAME_NOT_BLANK);

    authorDto = TestUtils.createAuthorDto(id);
    authorDto.setLastName("  ");
    assertUpdateBadRequest(UserUtils.BOOK_ADMIN, path, authorDto, ALERT_BAD_REQUEST,
        PARAM_LAST_NAME_NOT_BLANK);
  }
}
