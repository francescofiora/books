package it.francescofiora.books.endtoend;

import static org.assertj.core.api.Assertions.assertThat;

import it.francescofiora.books.service.dto.TitleDto;
import it.francescofiora.books.util.TestUtils;
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
class TitleEndToEndTest extends AbstractTestEndToEnd {

  private static final String AUTHORS_URI = "/api/authors";
  private static final String AUTHORS_ID_URI = "/api/authors/%d";
  private static final String AUTHORS_TITLES_URI = "/api/authors/%d/titles";
  private static final String PUBLISHERS_URI = "/api/publishers";
  private static final String PUBLISHERS_ID_URI = "/api/publishers/%d";
  private static final String TITLES_URI = "/api/titles";
  private static final String TITLES_ID_URI = "/api/titles/%d";
  private static final String WRONG_URI = "/api/wrong";

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
  private static final String PARAM_TITLE_NOT_BLANK = "[updatebleTitleDto.title - NotBlank]";
  private static final String PARAM_WRONG_URI = "/books" + WRONG_URI;

  @Test
  void testAuth() throws Exception {
    testUnauthorized(TITLES_URI);
  }

  @Test
  void testCreate() throws Exception {
    var authorId =
        createAndReturnId(AUTHORS_URI, TestUtils.createNewAuthorDto(), AUTHOR_ALERT_CREATED);

    var publisherId = createAndReturnId(PUBLISHERS_URI, TestUtils.createNewPublisherDto(),
        PUBLISHER_ALERT_CREATED);

    var newTitleDto = TestUtils.createNewSimpleTitleDto();
    newTitleDto.getAuthors().add(TestUtils.createRefAuthorDto(authorId));
    newTitleDto.setPublisher(TestUtils.createRefPublisherDto(publisherId));
    var titleId = createAndReturnId(TITLES_URI, newTitleDto, TITLE_ALERT_CREATED);

    final var titlesIdUri = String.format(TITLES_ID_URI, titleId);

    var titleDto = get(titlesIdUri, TitleDto.class, ALERT_GET, String.valueOf(titleId));
    assertThat(titleDto.getId()).isEqualTo(titleId);
    assertThat(titleDto.getTitle()).isEqualTo(newTitleDto.getTitle());
    assertThat(titleDto.getCopyright()).isEqualTo(newTitleDto.getCopyright());
    assertThat(titleDto.getEditionNumber()).isEqualTo(newTitleDto.getEditionNumber());
    assertThat(titleDto.getImageFile()).isEqualTo(newTitleDto.getImageFile());
    assertThat(titleDto.getLanguage()).isEqualTo(newTitleDto.getLanguage());
    assertThat(titleDto.getPrice()).isEqualTo(newTitleDto.getPrice());
    assertThat(titleDto.getPublisher().getId()).isEqualTo(publisherId);
    assertThat(titleDto.getAuthors()).hasSize(1);
    assertThat(titleDto.getAuthors().get(0).getId()).isEqualTo(authorId);

    authorId = createAndReturnId(AUTHORS_URI, TestUtils.createNewAuthorDto(), AUTHOR_ALERT_CREATED);

    var updatebleTitleDto = TestUtils.createSimpleUpdatebleTitleDto(titleId);
    updatebleTitleDto.getAuthors().add(TestUtils.createRefAuthorDto(authorId));
    updatebleTitleDto.setPublisher(TestUtils.createRefPublisherDto(publisherId));

    update(titlesIdUri, updatebleTitleDto, ALERT_UPDATED, String.valueOf(titleId));

    titleDto = get(titlesIdUri, TitleDto.class, ALERT_GET, String.valueOf(titleId));
    assertThat(titleDto.getId()).isEqualTo(updatebleTitleDto.getId());
    assertThat(titleDto.getTitle()).isEqualTo(updatebleTitleDto.getTitle());
    assertThat(titleDto.getCopyright()).isEqualTo(updatebleTitleDto.getCopyright());
    assertThat(titleDto.getEditionNumber()).isEqualTo(updatebleTitleDto.getEditionNumber());
    assertThat(titleDto.getImageFile()).isEqualTo(updatebleTitleDto.getImageFile());
    assertThat(titleDto.getLanguage()).isEqualTo(updatebleTitleDto.getLanguage());
    assertThat(titleDto.getPrice()).isEqualTo(updatebleTitleDto.getPrice());
    assertThat(titleDto.getPublisher().getId()).isEqualTo(publisherId);
    assertThat(titleDto.getAuthors()).hasSize(1);
    assertThat(titleDto.getAuthors().get(0).getId()).isEqualTo(authorId);

    var pageable = PageRequest.of(1, 1);
    var titles = get(TITLES_URI, pageable, TitleDto[].class, ALERT_GET, PARAM_PAGE_20);
    assertThat(titles).isNotEmpty();
    var option = Stream.of(titles).filter(title -> title.getId().equals(titleId)).findAny();
    assertThat(option).isPresent();
    assertThat(option.get()).isEqualTo(titleDto);

    final var authorsTitlesUri = String.format(AUTHORS_TITLES_URI, authorId);

    titles = get(authorsTitlesUri, pageable, TitleDto[].class, ALERT_GET, PARAM_PAGE_1);
    assertThat(titles).isNotEmpty();
    option = Stream.of(titles).filter(title -> title.getId().equals(titleId)).findAny();
    assertThat(option).isPresent();
    assertThat(option.get()).isEqualTo(titleDto);

    final var authorsIdUri = String.format(AUTHORS_ID_URI, authorId);

    assertDeleteBadRequest(authorsIdUri, AUTHOR_ALERT_BAD_REQUEST, String.valueOf(authorId));
    assertDeleteBadRequest(String.format(PUBLISHERS_ID_URI, publisherId),
        PUBLISHER_ALERT_BAD_REQUEST, String.valueOf(publisherId));

    delete(titlesIdUri, TITLE_ALERT_DELETED, String.valueOf(titleId));

    assertGetNotFound(titlesIdUri, TitleDto.class, TITLE_ALERT_NOT_FOUND, String.valueOf(titleId));

    delete(authorsIdUri, AUTHOR_ALERT_DELETED, String.valueOf(authorId));
    delete(String.format(PUBLISHERS_ID_URI, publisherId), PUBLISHER_ALERT_DELETED,
        String.valueOf(publisherId));

    assertGetNotFound(String.format(AUTHORS_TITLES_URI, authorId), pageable, TitleDto[].class,
        AUTHOR_ALERT_NOT_FOUND, String.valueOf(authorId));
  }

  @Test
  void testCreateBadRequest() throws Exception {
    var newTitleDto = TestUtils.createNewSimpleTitleDto();

    var authorId =
        createAndReturnId(AUTHORS_URI, TestUtils.createNewAuthorDto(), AUTHOR_ALERT_CREATED);
    newTitleDto.getAuthors().add(TestUtils.createRefAuthorDto(authorId));
    var publisherId = 100L;
    newTitleDto.setPublisher(TestUtils.createRefPublisherDto(publisherId));

    assertCreateNotFound(TITLES_URI, newTitleDto, PUBLISHER_ALERT_NOT_FOUND,
        String.valueOf(publisherId));

    authorId = 100L;
    newTitleDto.getAuthors().add(TestUtils.createRefAuthorDto(authorId));
    publisherId = createAndReturnId(PUBLISHERS_URI, TestUtils.createNewPublisherDto(),
        PUBLISHER_ALERT_CREATED);
    newTitleDto.setPublisher(TestUtils.createRefPublisherDto(publisherId));

    assertCreateNotFound(TITLES_URI, newTitleDto, AUTHOR_ALERT_NOT_FOUND, String.valueOf(authorId));
  }


  @Test
  void testGetBadRequest() throws Exception {
    assertGetBadRequest(TITLES_URI + "/999999999999999999999999", String.class, "id.badRequest",
        PARAM_NOT_VALID_LONG);
  }

  @Test
  void testUpdateBadRequest() throws Exception {
    // id
    var titleDto = TestUtils.createUpdatebleTitleDto(null);
    assertUpdateBadRequest(String.format(TITLES_ID_URI, 1L), titleDto, TITLE_ALERT_BEAN_BAD_REQUEST,
        PARAM_ID_NOT_NULL);

    var authorId =
        createAndReturnId(AUTHORS_URI, TestUtils.createNewAuthorDto(), AUTHOR_ALERT_CREATED);

    var publisherId = createAndReturnId(PUBLISHERS_URI, TestUtils.createNewPublisherDto(),
        PUBLISHER_ALERT_CREATED);

    var newTitleDto = TestUtils.createNewSimpleTitleDto();
    newTitleDto.getAuthors().add(TestUtils.createRefAuthorDto(authorId));
    newTitleDto.setPublisher(TestUtils.createRefPublisherDto(publisherId));
    Long id = createAndReturnId(TITLES_URI, newTitleDto, TITLE_ALERT_CREATED);

    titleDto = TestUtils.createUpdatebleTitleDto(id);
    assertUpdateBadRequest(String.format(TITLES_ID_URI, id + 1), titleDto, TITLE_ALERT_BAD_REQUEST,
        String.valueOf(id));

    final var path = String.format(TITLES_ID_URI, id);

    // Authors
    titleDto = TestUtils.createUpdatebleTitleDto(id);
    titleDto.getAuthors().get(0).setId(null);
    assertUpdateBadRequest(path, titleDto, TITLE_ALERT_BEAN_BAD_REQUEST, PARAM_ID_NOT_NULL2);

    titleDto = TestUtils.createUpdatebleTitleDto(id);
    titleDto.getAuthors().clear();
    assertUpdateBadRequest(path, titleDto, TITLE_ALERT_BEAN_BAD_REQUEST, PARAM_AUTHORS_NOT_EMPTY);

    titleDto = TestUtils.createUpdatebleTitleDto(id);
    titleDto.setAuthors(null);
    assertUpdateBadRequest(path, titleDto, TITLE_ALERT_BEAN_BAD_REQUEST, PARAM_AUTHORS_NOT_EMPTY);

    // Publisher
    titleDto = TestUtils.createUpdatebleTitleDto(id);
    titleDto.getPublisher().setId(null);
    assertUpdateBadRequest(path, titleDto, TITLE_ALERT_BEAN_BAD_REQUEST, PARAM_ID_NOT_NULL3);

    titleDto = TestUtils.createUpdatebleTitleDto(id);
    titleDto.setPublisher(null);
    assertUpdateBadRequest(path, titleDto, TITLE_ALERT_BEAN_BAD_REQUEST, PARAM_PUBLISHER_NOT_EMPTY);

    // copyright
    titleDto = TestUtils.createUpdatebleTitleDto(id);
    titleDto.setCopyright(null);
    assertUpdateBadRequest(path, titleDto, TITLE_ALERT_BEAN_BAD_REQUEST, PARAM_COPYRIGHT_NOT_EMPTY);

    // editionNumber
    titleDto = TestUtils.createUpdatebleTitleDto(id);
    titleDto.setEditionNumber(null);
    assertUpdateBadRequest(path, titleDto, TITLE_ALERT_BEAN_BAD_REQUEST,
        PARAM_EDITION_NUMBER_NOT_EMPTY);

    // language
    titleDto = TestUtils.createUpdatebleTitleDto(id);
    titleDto.setLanguage(null);
    assertUpdateBadRequest(path, titleDto, TITLE_ALERT_BEAN_BAD_REQUEST, PARAM_LANGUAGE_NOT_EMPTY);

    // price
    titleDto = TestUtils.createUpdatebleTitleDto(id);
    titleDto.setPrice(null);
    assertUpdateBadRequest(path, titleDto, TITLE_ALERT_BEAN_BAD_REQUEST, PARAM_PRICE_NOT_EMPTY);

    titleDto = TestUtils.createUpdatebleTitleDto(id);
    titleDto.setPrice(0L);
    assertUpdateBadRequest(path, titleDto, TITLE_ALERT_BEAN_BAD_REQUEST, PARAM_PRICE_POSITIVE);

    // title
    titleDto = TestUtils.createUpdatebleTitleDto(id);
    titleDto.setTitle(null);
    assertUpdateBadRequest(path, titleDto, TITLE_ALERT_BEAN_BAD_REQUEST, PARAM_TITLE_NOT_BLANK);

    titleDto = TestUtils.createUpdatebleTitleDto(id);
    titleDto.setTitle("  ");
    assertUpdateBadRequest(path, titleDto, TITLE_ALERT_BEAN_BAD_REQUEST, PARAM_TITLE_NOT_BLANK);
  }

  @Test
  void testWrongUri() throws Exception {
    assertGetNotFound(WRONG_URI, String.class, "404 NOT_FOUND", PARAM_WRONG_URI);
  }
}
