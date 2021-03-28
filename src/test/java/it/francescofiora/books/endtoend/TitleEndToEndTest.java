package it.francescofiora.books.endtoend;

import static org.assertj.core.api.Assertions.assertThat;

import it.francescofiora.books.service.dto.NewTitleDto;
import it.francescofiora.books.service.dto.TitleDto;
import it.francescofiora.books.service.dto.UpdatebleTitleDto;
import it.francescofiora.books.util.TestUtils;
import java.util.Optional;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = {"classpath:application_test.properties"})
public class TitleEndToEndTest extends AbstractTestEndToEnd {

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
  private static final String TITLE_ALERT_NOT_FOUND = "TitleDto.notFound";

  private static final String ALERT_UPDATED = "TitleDto.updated";
  private static final String ALERT_GET = "TitleDto.get";

  @Test
  public void testAuth() throws Exception {
    testUnauthorized(TITLES_URI);
  }

  @Test
  public void testCreate() throws Exception {
    Long authorId =
        createAndReturnId(AUTHORS_URI, TestUtils.createNewAuthorDto(), AUTHOR_ALERT_CREATED);

    Long publisherId = createAndReturnId(PUBLISHERS_URI, TestUtils.createNewPublisherDto(),
        PUBLISHER_ALERT_CREATED);

    NewTitleDto newTitleDto = TestUtils.createNewSimpleTitleDto();
    newTitleDto.getAuthors().add(TestUtils.createRefAuthorDto(authorId));
    newTitleDto.setPublisher(TestUtils.createRefPublisherDto(publisherId));
    Long titleId = createAndReturnId(TITLES_URI, newTitleDto, TITLE_ALERT_CREATED);

    UpdatebleTitleDto updatebleTitleDto = TestUtils.createSimpleUpdatebleTitleDto(titleId);
    updatebleTitleDto.getAuthors().add(TestUtils.createRefAuthorDto(authorId));
    updatebleTitleDto.setPublisher(TestUtils.createRefPublisherDto(publisherId));

    final String titlesIdUri = String.format(TITLES_ID_URI, titleId);

    update(titlesIdUri, updatebleTitleDto, ALERT_UPDATED);

    TitleDto titleDto = get(titlesIdUri, TitleDto.class, ALERT_GET);
    assertThat(titleDto.getId()).isEqualTo(updatebleTitleDto.getId());
    assertThat(titleDto.getTitle()).isEqualTo(updatebleTitleDto.getTitle());
    assertThat(titleDto.getCopyright()).isEqualTo(updatebleTitleDto.getCopyright());
    assertThat(titleDto.getEditionNumber()).isEqualTo(updatebleTitleDto.getEditionNumber());
    assertThat(titleDto.getImageFile()).isEqualTo(updatebleTitleDto.getImageFile());
    assertThat(titleDto.getLanguage()).isEqualTo(updatebleTitleDto.getLanguage());
    assertThat(titleDto.getPrice()).isEqualTo(updatebleTitleDto.getPrice());
    assertThat(titleDto.getPublisher().getId()).isEqualTo(publisherId);
    assertThat(titleDto.getAuthors().get(0).getId()).isEqualTo(authorId);

    Pageable pageable = PageRequest.of(1, 1);
    TitleDto[] titles = get(TITLES_URI, pageable, TitleDto[].class, ALERT_GET);
    assertThat(titles).isNotEmpty();
    Optional<TitleDto> option =
        Stream.of(titles).filter(title -> title.getId().equals(titleId)).findAny();
    assertThat(option).isPresent();
    assertThat(option.get()).isEqualTo(titleDto);

    final String authorsTitlesUri = String.format(AUTHORS_TITLES_URI, authorId);

    titles = get(authorsTitlesUri, pageable, TitleDto[].class, ALERT_GET);
    assertThat(titles).isNotEmpty();
    option = Stream.of(titles).filter(title -> title.getId().equals(titleId)).findAny();
    assertThat(option).isPresent();
    assertThat(option.get()).isEqualTo(titleDto);

    final String authorsIdUri = String.format(AUTHORS_ID_URI, authorId);

    assertDeleteBadRequest(authorsIdUri, AUTHOR_ALERT_BAD_REQUEST);
    assertDeleteBadRequest(String.format(PUBLISHERS_ID_URI, publisherId),
        PUBLISHER_ALERT_BAD_REQUEST);

    delete(titlesIdUri, TITLE_ALERT_DELETED);

    assertGetNotFound(titlesIdUri, TitleDto.class, TITLE_ALERT_NOT_FOUND);

    delete(authorsIdUri, AUTHOR_ALERT_DELETED);
    delete(String.format(PUBLISHERS_ID_URI, publisherId), PUBLISHER_ALERT_DELETED);

    assertGetNotFound(String.format(AUTHORS_TITLES_URI, authorId), pageable, TitleDto[].class,
        AUTHOR_ALERT_NOT_FOUND);
  }

  @Test
  public void testGetTitleBadRequest() throws Exception {
    assertGetBadRequest(TITLES_URI + "/999999999999999999999999", String.class, "id.badRequest");
  }

  @Test
  public void testUpdateTitleBadRequest() throws Exception {
    // id
    UpdatebleTitleDto titleDto = TestUtils.createUpdatebleTitleDto(null);
    assertUpdateBadRequest(String.format(TITLES_ID_URI, 1L), titleDto,
        TITLE_ALERT_BEAN_BAD_REQUEST);

    Long authorId =
        createAndReturnId(AUTHORS_URI, TestUtils.createNewAuthorDto(), AUTHOR_ALERT_CREATED);

    Long publisherId = createAndReturnId(PUBLISHERS_URI, TestUtils.createNewPublisherDto(),
        PUBLISHER_ALERT_CREATED);

    NewTitleDto newTitleDto = TestUtils.createNewSimpleTitleDto();
    newTitleDto.getAuthors().add(TestUtils.createRefAuthorDto(authorId));
    newTitleDto.setPublisher(TestUtils.createRefPublisherDto(publisherId));
    Long id = createAndReturnId(TITLES_URI, newTitleDto, TITLE_ALERT_CREATED);

    titleDto = TestUtils.createUpdatebleTitleDto(id);
    assertUpdateBadRequest(String.format(TITLES_ID_URI, id + 1), titleDto, TITLE_ALERT_BAD_REQUEST);

    final String path = String.format(TITLES_ID_URI, id);

    // Authors
    titleDto = TestUtils.createUpdatebleTitleDto(id);
    titleDto.getAuthors().get(0).setId(null);
    assertUpdateBadRequest(path, titleDto, TITLE_ALERT_BEAN_BAD_REQUEST);

    titleDto = TestUtils.createUpdatebleTitleDto(id);
    titleDto.getAuthors().clear();
    assertUpdateBadRequest(path, titleDto, TITLE_ALERT_BEAN_BAD_REQUEST);

    titleDto = TestUtils.createUpdatebleTitleDto(id);
    titleDto.setAuthors(null);
    assertUpdateBadRequest(path, titleDto, TITLE_ALERT_BEAN_BAD_REQUEST);

    // Publisher
    titleDto = TestUtils.createUpdatebleTitleDto(id);
    titleDto.getPublisher().setId(null);
    assertUpdateBadRequest(path, titleDto, TITLE_ALERT_BEAN_BAD_REQUEST);

    titleDto = TestUtils.createUpdatebleTitleDto(id);
    titleDto.setPublisher(null);
    assertUpdateBadRequest(path, titleDto, TITLE_ALERT_BEAN_BAD_REQUEST);

    // copyright
    titleDto = TestUtils.createUpdatebleTitleDto(id);
    titleDto.setCopyright(null);
    assertUpdateBadRequest(path, titleDto, TITLE_ALERT_BEAN_BAD_REQUEST);

    // editionNumber
    titleDto = TestUtils.createUpdatebleTitleDto(id);
    titleDto.setEditionNumber(null);
    assertUpdateBadRequest(path, titleDto, TITLE_ALERT_BEAN_BAD_REQUEST);

    // language
    titleDto = TestUtils.createUpdatebleTitleDto(id);
    titleDto.setLanguage(null);
    assertUpdateBadRequest(path, titleDto, TITLE_ALERT_BEAN_BAD_REQUEST);

    // price
    titleDto = TestUtils.createUpdatebleTitleDto(id);
    titleDto.setPrice(null);
    assertUpdateBadRequest(path, titleDto, TITLE_ALERT_BEAN_BAD_REQUEST);

    titleDto = TestUtils.createUpdatebleTitleDto(id);
    titleDto.setPrice(0L);
    assertUpdateBadRequest(path, titleDto, TITLE_ALERT_BEAN_BAD_REQUEST);

    // title
    titleDto = TestUtils.createUpdatebleTitleDto(id);
    titleDto.setTitle(null);
    assertUpdateBadRequest(path, titleDto, TITLE_ALERT_BEAN_BAD_REQUEST);

    titleDto = TestUtils.createUpdatebleTitleDto(id);
    titleDto.setTitle("  ");
    assertUpdateBadRequest(path, titleDto, TITLE_ALERT_BEAN_BAD_REQUEST);
  }

  @Test
  public void testWrongUri() throws Exception {
    assertGetNotFound(WRONG_URI, String.class, "404 NOT_FOUND");
  }
}
