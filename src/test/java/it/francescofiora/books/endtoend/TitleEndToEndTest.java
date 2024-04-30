package it.francescofiora.books.endtoend;

import static org.assertj.core.api.Assertions.assertThat;

import it.francescofiora.books.service.dto.BaseTitleDto;
import it.francescofiora.books.service.dto.TitleDto;
import it.francescofiora.books.util.TestUtils;
import it.francescofiora.books.util.UserUtils;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = {"classpath:application_test.properties"})
class TitleEndToEndTest extends AbstractTestEndToEnd {

  private static final String AUTHORS_URI = "/api/v1/authors";
  private static final String AUTHORS_ID_URI = "/api/v1/authors/%d";
  private static final String AUTHORS_TITLES_URI = "/api/v1/authors/%d/titles";
  private static final String PUBLISHERS_URI = "/api/v1/publishers";
  private static final String PUBLISHERS_ID_URI = "/api/v1/publishers/%d";
  private static final String TITLES_URI = "/api/v1/titles";
  private static final String TITLES_ID_URI = "/api/v1/titles/%d";
  private static final String WRONG_URI = "/api/v1/wrong";

  private static final String AUTHOR_ALERT_CREATED = "AuthorDto.created";
  private static final String PUBLISHER_ALERT_CREATED = "PublisherDto.created";
  private static final String TITLE_ALERT_CREATED = "TitleDto.created";

  private static final String AUTHOR_ALERT_DELETED = "AuthorDto.deleted";
  private static final String PUBLISHER_ALERT_DELETED = "PublisherDto.deleted";
  private static final String TITLE_ALERT_DELETED = "TitleDto.deleted";

  private static final String AUTHOR_ALERT_BAD_REQUEST = "AuthorDto.badRequest";
  private static final String PUBLISHER_ALERT_BAD_REQUEST = "PublisherDto.badRequest";
  private static final String TITLE_ALERT_BAD_REQUEST = "TitleDto.badRequest";
  private static final String TITLE_ALERT_BEAN_BAD_REQUEST = "UpdatebleTitleDto.badRequest";

  private static final String AUTHOR_ALERT_NOT_FOUND = "AuthorDto.notFound";
  private static final String PUBLISHER_ALERT_NOT_FOUND = "PublisherDto.notFound";
  private static final String TITLE_ALERT_NOT_FOUND = "TitleDto.notFound";

  private static final String ALERT_UPDATED = "TitleDto.updated";
  private static final String ALERT_GET = "TitleDto.get";

  private static final String PARAM_PAGE_20 = "0 20";
  private static final String PARAM_PAGE_10 = "0 10";
  private static final String PARAM_PAGE_1 = "0 1";
  private static final String PARAM_NOT_VALID_LONG =
      "'id' should be a valid 'Long' and '999999999999999999999999' isn't";

  private static final String PARAM_ID_NOT_NULL = "[updatebleTitleDto.id - NotNull]";
  private static final String PARAM_ID_NOT_NULL2 = "[updatebleTitleDto.authors[0].id - NotNull]";
  private static final String PARAM_AUTHORS_NOT_EMPTY = "[updatebleTitleDto.authors - NotEmpty]";
  private static final String PARAM_ID_NOT_NULL3 = "[updatebleTitleDto.publisher.id - NotNull]";
  private static final String PARAM_PUBLISHER_NOT_EMPTY = "[updatebleTitleDto.publisher - NotNull]";
  private static final String PARAM_COPYRIGHT_NOT_EMPTY = "[updatebleTitleDto.copyright - NotNull]";
  private static final String PARAM_EDITION_NUMBER_NOT_EMPTY =
      "[updatebleTitleDto.editionNumber - NotNull]";
  private static final String PARAM_LANGUAGE_NOT_EMPTY = "[updatebleTitleDto.language - NotNull]";
  private static final String PARAM_PRICE_NOT_EMPTY = "[updatebleTitleDto.price - NotNull]";
  private static final String PARAM_PRICE_POSITIVE = "[updatebleTitleDto.price - Positive]";
  private static final String PARAM_NAME_NOT_BLANK = "[updatebleTitleDto.name - NotBlank]";

  @Test
  void testAuth() {
    testUnauthorized(TITLES_URI);
  }

  @Test
  void testCreate() {
    var auth = getToken(UserUtils.BOOK_ADMIN);
    var authorId = createAndReturnId(auth.getToken(), AUTHORS_URI,
        TestUtils.createNewAuthorDto(), AUTHOR_ALERT_CREATED);

    var publisherId = createAndReturnId(auth.getToken(), PUBLISHERS_URI,
        TestUtils.createNewPublisherDto(), PUBLISHER_ALERT_CREATED);

    var newTitleDto = TestUtils.createNewSimpleTitleDto();
    newTitleDto.getAuthors().add(TestUtils.createRefAuthorDto(authorId));
    newTitleDto.setPublisher(TestUtils.createRefPublisherDto(publisherId));
    var titleId = createAndReturnId(auth.getToken(), TITLES_URI, newTitleDto, TITLE_ALERT_CREATED);

    final var titlesIdUri = String.format(TITLES_ID_URI, titleId);

    var titleDto =
        get(auth.getToken(), titlesIdUri, TitleDto.class, ALERT_GET, String.valueOf(titleId));
    assertThat(titleDto.getId()).isEqualTo(titleId);
    checkTitleDto(titleDto, newTitleDto);
    assertThat(titleDto.getPublisher().getId()).isEqualTo(publisherId);
    assertThat(titleDto.getAuthors()).hasSize(1);
    assertThat(titleDto.getAuthors().get(0).getId()).isEqualTo(authorId);

    authorId = createAndReturnId(auth.getToken(), AUTHORS_URI, TestUtils.createNewAuthorDto(),
        AUTHOR_ALERT_CREATED);

    var updatebleTitleDto = TestUtils.createSimpleUpdatebleTitleDto(titleId);
    updatebleTitleDto.getAuthors().add(TestUtils.createRefAuthorDto(authorId));
    updatebleTitleDto.setPublisher(TestUtils.createRefPublisherDto(publisherId));

    update(auth.getToken(), titlesIdUri, updatebleTitleDto, ALERT_UPDATED, String.valueOf(titleId));

    titleDto =
        get(auth.getToken(), titlesIdUri, TitleDto.class, ALERT_GET, String.valueOf(titleId));
    assertThat(titleDto.getId()).isEqualTo(updatebleTitleDto.getId());
    checkTitleDto(titleDto, updatebleTitleDto);
    assertThat(titleDto.getPublisher().getId()).isEqualTo(publisherId);
    assertThat(titleDto.getAuthors()).hasSize(1);
    assertThat(titleDto.getAuthors().get(0).getId()).isEqualTo(authorId);

    var titles = get(auth.getToken(), TITLES_URI, TitleDto[].class, ALERT_GET, PARAM_PAGE_20);
    assertThat(titles).isNotEmpty();
    var option = Stream.of(titles).filter(title -> title.getId().equals(titleId)).findAny();
    assertThat(option).isPresent().contains(titleDto);

    var pageRequest = TestUtils.createPageRequestAsMap(0, 10);
    titles = get(auth.getToken(), TITLES_URI, pageRequest, TitleDto[].class, ALERT_GET,
        PARAM_PAGE_10);
    assertThat(titles).isNotEmpty();
    option = Stream.of(titles).filter(title -> title.getId().equals(titleId)).findAny();
    assertThat(option).isPresent().contains(titleDto);

    final var authorsTitlesUri = String.format(AUTHORS_TITLES_URI, authorId);

    titles = get(auth.getToken(), authorsTitlesUri, pageRequest, TitleDto[].class, ALERT_GET,
        PARAM_PAGE_1);
    assertThat(titles).isNotEmpty();
    option = Stream.of(titles).filter(title -> title.getId().equals(titleId)).findAny();
    assertThat(option).isPresent().contains(titleDto);

    final var authorsIdUri = String.format(AUTHORS_ID_URI, authorId);

    assertDeleteBadRequest(auth.getToken(), authorsIdUri, AUTHOR_ALERT_BAD_REQUEST,
        String.valueOf(authorId));
    assertDeleteBadRequest(auth.getToken(), String.format(PUBLISHERS_ID_URI, publisherId),
        PUBLISHER_ALERT_BAD_REQUEST, String.valueOf(publisherId));

    delete(auth.getToken(), titlesIdUri, TITLE_ALERT_DELETED, String.valueOf(titleId));

    assertGetNotFound(auth.getToken(), titlesIdUri, TitleDto.class, TITLE_ALERT_NOT_FOUND,
        String.valueOf(titleId));

    delete(auth.getToken(), authorsIdUri, AUTHOR_ALERT_DELETED, String.valueOf(authorId));
    delete(auth.getToken(), String.format(PUBLISHERS_ID_URI, publisherId),
        PUBLISHER_ALERT_DELETED, String.valueOf(publisherId));

    assertGetNotFound(auth.getToken(), String.format(AUTHORS_TITLES_URI, authorId),
        pageRequest, TitleDto[].class, AUTHOR_ALERT_NOT_FOUND, String.valueOf(authorId));
  }

  void checkTitleDto(TitleDto titleDto, BaseTitleDto baseTitleDto) {
    assertThat(titleDto.getName()).isEqualTo(baseTitleDto.getName());
    assertThat(titleDto.getCopyright()).isEqualTo(baseTitleDto.getCopyright());
    assertThat(titleDto.getEditionNumber()).isEqualTo(baseTitleDto.getEditionNumber());
    assertThat(titleDto.getImageFile()).isEqualTo(baseTitleDto.getImageFile());
    assertThat(titleDto.getLanguage()).isEqualTo(baseTitleDto.getLanguage());
    assertThat(titleDto.getPrice()).isEqualTo(baseTitleDto.getPrice());
  }

  @Test
  void testCreateBadRequest() {
    var newTitleDto = TestUtils.createNewSimpleTitleDto();

    var auth = getToken(UserUtils.BOOK_ADMIN);
    var authorId = createAndReturnId(auth.getToken(), AUTHORS_URI,
        TestUtils.createNewAuthorDto(), AUTHOR_ALERT_CREATED);
    newTitleDto.getAuthors().add(TestUtils.createRefAuthorDto(authorId));
    var publisherId = 100L;
    newTitleDto.setPublisher(TestUtils.createRefPublisherDto(publisherId));

    assertCreateNotFound(auth.getToken(), TITLES_URI, newTitleDto, PUBLISHER_ALERT_NOT_FOUND,
        String.valueOf(publisherId));

    authorId = 100L;
    newTitleDto.getAuthors().add(TestUtils.createRefAuthorDto(authorId));
    publisherId = createAndReturnId(auth.getToken(), PUBLISHERS_URI,
        TestUtils.createNewPublisherDto(), PUBLISHER_ALERT_CREATED);
    newTitleDto.setPublisher(TestUtils.createRefPublisherDto(publisherId));

    assertCreateNotFound(auth.getToken(), TITLES_URI, newTitleDto, AUTHOR_ALERT_NOT_FOUND,
        String.valueOf(authorId));
  }

  @Test
  void testGetBadRequest() {
    var auth = getToken(UserUtils.BOOK_ADMIN);
    assertGetBadRequest(auth.getToken(), TITLES_URI + "/999999999999999999999999",
        String.class, "id.badRequest", PARAM_NOT_VALID_LONG);
  }

  @Test
  void testUpdateBadRequest() {
    // id
    var titleDto = TestUtils.createUpdatebleTitleDto(null);
    var auth = getToken(UserUtils.BOOK_ADMIN);
    assertUpdateBadRequest(auth.getToken(), String.format(TITLES_ID_URI, 1L), titleDto,
        TITLE_ALERT_BEAN_BAD_REQUEST, PARAM_ID_NOT_NULL);

    var authorId = createAndReturnId(auth.getToken(), AUTHORS_URI,
        TestUtils.createNewAuthorDto(), AUTHOR_ALERT_CREATED);

    var publisherId = createAndReturnId(auth.getToken(), PUBLISHERS_URI,
        TestUtils.createNewPublisherDto(), PUBLISHER_ALERT_CREATED);

    var newTitleDto = TestUtils.createNewSimpleTitleDto();
    newTitleDto.getAuthors().add(TestUtils.createRefAuthorDto(authorId));
    newTitleDto.setPublisher(TestUtils.createRefPublisherDto(publisherId));
    Long id = createAndReturnId(auth.getToken(), TITLES_URI, newTitleDto, TITLE_ALERT_CREATED);

    titleDto = TestUtils.createUpdatebleTitleDto(id);
    assertUpdateBadRequest(auth.getToken(), String.format(TITLES_ID_URI, id + 1), titleDto,
        TITLE_ALERT_BAD_REQUEST, String.valueOf(id));

    final var path = String.format(TITLES_ID_URI, id);

    // Authors
    titleDto = TestUtils.createUpdatebleTitleDto(id);
    titleDto.getAuthors().get(0).setId(null);
    assertUpdateBadRequest(auth.getToken(), path, titleDto, TITLE_ALERT_BEAN_BAD_REQUEST,
        PARAM_ID_NOT_NULL2);

    titleDto = TestUtils.createUpdatebleTitleDto(id);
    titleDto.getAuthors().clear();
    assertUpdateBadRequest(auth.getToken(), path, titleDto, TITLE_ALERT_BEAN_BAD_REQUEST,
        PARAM_AUTHORS_NOT_EMPTY);

    titleDto = TestUtils.createUpdatebleTitleDto(id);
    titleDto.setAuthors(null);
    assertUpdateBadRequest(auth.getToken(), path, titleDto, TITLE_ALERT_BEAN_BAD_REQUEST,
        PARAM_AUTHORS_NOT_EMPTY);

    authorId = 100L;
    titleDto = TestUtils.createUpdatebleTitleDto(id);
    titleDto.getAuthors().get(0).setId(authorId);
    assertUpdateNotFound(auth.getToken(), path, titleDto, AUTHOR_ALERT_NOT_FOUND,
        String.valueOf(authorId));

    // Publisher
    titleDto = TestUtils.createUpdatebleTitleDto(id);
    titleDto.getPublisher().setId(null);
    assertUpdateBadRequest(auth.getToken(), path, titleDto, TITLE_ALERT_BEAN_BAD_REQUEST,
        PARAM_ID_NOT_NULL3);

    titleDto = TestUtils.createUpdatebleTitleDto(id);
    titleDto.setPublisher(null);
    assertUpdateBadRequest(auth.getToken(), path, titleDto, TITLE_ALERT_BEAN_BAD_REQUEST,
        PARAM_PUBLISHER_NOT_EMPTY);

    publisherId = 100L;
    titleDto = TestUtils.createUpdatebleTitleDto(id);
    titleDto.getPublisher().setId(publisherId);
    assertUpdateNotFound(auth.getToken(), path, titleDto, PUBLISHER_ALERT_NOT_FOUND,
        String.valueOf(publisherId));

    // copyright
    titleDto = TestUtils.createUpdatebleTitleDto(id);
    titleDto.setCopyright(null);
    assertUpdateBadRequest(auth.getToken(), path, titleDto, TITLE_ALERT_BEAN_BAD_REQUEST,
        PARAM_COPYRIGHT_NOT_EMPTY);

    // editionNumber
    titleDto = TestUtils.createUpdatebleTitleDto(id);
    titleDto.setEditionNumber(null);
    assertUpdateBadRequest(auth.getToken(), path, titleDto, TITLE_ALERT_BEAN_BAD_REQUEST,
        PARAM_EDITION_NUMBER_NOT_EMPTY);

    // language
    titleDto = TestUtils.createUpdatebleTitleDto(id);
    titleDto.setLanguage(null);
    assertUpdateBadRequest(auth.getToken(), path, titleDto, TITLE_ALERT_BEAN_BAD_REQUEST,
        PARAM_LANGUAGE_NOT_EMPTY);

    // price
    titleDto = TestUtils.createUpdatebleTitleDto(id);
    titleDto.setPrice(null);
    assertUpdateBadRequest(auth.getToken(), path, titleDto, TITLE_ALERT_BEAN_BAD_REQUEST,
        PARAM_PRICE_NOT_EMPTY);

    titleDto = TestUtils.createUpdatebleTitleDto(id);
    titleDto.setPrice(0L);
    assertUpdateBadRequest(auth.getToken(), path, titleDto, TITLE_ALERT_BEAN_BAD_REQUEST,
        PARAM_PRICE_POSITIVE);

    // title
    titleDto = TestUtils.createUpdatebleTitleDto(id);
    titleDto.setName(null);
    assertUpdateBadRequest(auth.getToken(), path, titleDto, TITLE_ALERT_BEAN_BAD_REQUEST,
        PARAM_NAME_NOT_BLANK);

    titleDto = TestUtils.createUpdatebleTitleDto(id);
    titleDto.setName("  ");
    assertUpdateBadRequest(auth.getToken(), path, titleDto, TITLE_ALERT_BEAN_BAD_REQUEST,
        PARAM_NAME_NOT_BLANK);
  }

  @Test
  void testWrongUri() {
    var auth = getToken(UserUtils.BOOK_ADMIN);
    var result = performGet(auth.getToken(), WRONG_URI, String.class);
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
  }
}
