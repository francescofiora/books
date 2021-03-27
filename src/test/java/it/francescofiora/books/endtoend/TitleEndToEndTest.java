package it.francescofiora.books.endtoend;

import static org.assertj.core.api.Assertions.assertThat;

import it.francescofiora.books.service.dto.NewPublisherDto;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

  @Test
  public void testAuth() throws Exception {
    testUnauthorized(TITLES_URI);
  }

  @Test
  public void testCreate() throws Exception {
    Long authorId = createResource(AUTHORS_URI, TestUtils.createNewAuthorDto(), "Author.created");

    Long publisherId =
        createResource(PUBLISHERS_URI, TestUtils.createNewPublisherDto(), "Publisher.created");

    NewTitleDto newTitleDto = TestUtils.createNewSimpleTitleDto();
    newTitleDto.getAuthors().add(TestUtils.createRefAuthorDto(authorId));
    newTitleDto.setPublisher(TestUtils.createRefPublisherDto(publisherId));
    Long titleId = createResource(TITLES_URI, newTitleDto, "Title.created");

    UpdatebleTitleDto updatebleTitleDto = TestUtils.createSimpleUpdatebleTitleDto(titleId);
    updatebleTitleDto.getAuthors().add(TestUtils.createRefAuthorDto(authorId));
    updatebleTitleDto.setPublisher(TestUtils.createRefPublisherDto(publisherId));

    final String titlesIdUri = String.format(TITLES_ID_URI, titleId);

    updateResource(titlesIdUri, updatebleTitleDto, "Title.updated");

    ResponseEntity<TitleDto> resultById = performGet(titlesIdUri, TitleDto.class);
    assertThat(resultById.getStatusCode()).isEqualTo(HttpStatus.OK);
    TitleDto titleDto = resultById.getBody();
    assertThat(titleDto).isNotNull();
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
    TitleDto[] titles = getResource(TITLES_URI, pageable, TitleDto[].class);
    assertThat(titles).isNotEmpty();
    Optional<TitleDto> option =
        Stream.of(titles).filter(title -> title.getId().equals(titleId)).findAny();
    assertThat(option).isPresent();
    assertThat(option.get()).isEqualTo(titleDto);

    final String authorsTitlesUri = String.format(AUTHORS_TITLES_URI, authorId);

    titles = getResource(authorsTitlesUri, pageable, TitleDto[].class);
    assertThat(titles).isNotEmpty();
    option = Stream.of(titles).filter(title -> title.getId().equals(titleId)).findAny();
    assertThat(option).isPresent();
    assertThat(option.get()).isEqualTo(titleDto);

    final String authorsIdUri = String.format(AUTHORS_ID_URI, authorId);

    ResponseEntity<Void> resAuthor = performDelete(authorsIdUri);
    assertThat(resAuthor.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

    ResponseEntity<Void> resPublisher =
        performDelete(String.format(PUBLISHERS_ID_URI, publisherId));
    assertThat(resPublisher.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

    deleteResource(titlesIdUri);

    getResourceNotFound(titlesIdUri, TitleDto.class);

    deleteResource(authorsIdUri);

    deleteResource(String.format(PUBLISHERS_ID_URI, publisherId));

    getResourceNotFound(String.format(AUTHORS_TITLES_URI, authorId), pageable, TitleDto[].class);
  }

  @Test
  public void testUpdateTitleBadRequest() throws Exception {
    // id
    UpdatebleTitleDto titleDto = TestUtils.createUpdatebleTitleDto(null);
    ResponseEntity<Void> result = performPut(String.format(TITLES_ID_URI, 1L), titleDto);
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

    Long authorId = createResource(AUTHORS_URI, TestUtils.createNewAuthorDto(), "Author.created");

    NewPublisherDto newPublisherDto = TestUtils.createNewPublisherDto();
    ResponseEntity<Void> resPublisher = performPost(PUBLISHERS_URI, newPublisherDto);
    Long publisherId = getIdFormHttpHeaders(resPublisher.getHeaders());

    NewTitleDto newTitleDto = TestUtils.createNewSimpleTitleDto();
    newTitleDto.getAuthors().add(TestUtils.createRefAuthorDto(authorId));
    newTitleDto.setPublisher(TestUtils.createRefPublisherDto(publisherId));
    Long id = createResource(TITLES_URI, newTitleDto, "Title.created");

    titleDto = TestUtils.createUpdatebleTitleDto(id);
    result = performPut(String.format(TITLES_ID_URI, id + 1), titleDto);
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

    final String path = String.format(TITLES_ID_URI, id);

    // Authors
    titleDto = TestUtils.createUpdatebleTitleDto(id);
    titleDto.getAuthors().get(0).setId(null);
    result = performPut(path, titleDto);
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

    titleDto = TestUtils.createUpdatebleTitleDto(id);
    titleDto.getAuthors().clear();
    result = performPut(path, titleDto);
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

    titleDto = TestUtils.createUpdatebleTitleDto(id);
    titleDto.setAuthors(null);
    result = performPut(path, titleDto);
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

    // Publisher
    titleDto = TestUtils.createUpdatebleTitleDto(id);
    titleDto.getPublisher().setId(null);
    result = performPut(path, titleDto);
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

    titleDto = TestUtils.createUpdatebleTitleDto(id);
    titleDto.setPublisher(null);
    result = performPut(path, titleDto);
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

    // copyright
    titleDto = TestUtils.createUpdatebleTitleDto(id);
    titleDto.setCopyright(null);
    result = performPut(path, titleDto);
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

    // editionNumber
    titleDto = TestUtils.createUpdatebleTitleDto(id);
    titleDto.setEditionNumber(null);
    result = performPut(path, titleDto);
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

    // language
    titleDto = TestUtils.createUpdatebleTitleDto(id);
    titleDto.setLanguage(null);
    result = performPut(path, titleDto);
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

    // price
    titleDto = TestUtils.createUpdatebleTitleDto(id);
    titleDto.setPrice(null);
    result = performPut(path, titleDto);
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

    titleDto = TestUtils.createUpdatebleTitleDto(id);
    titleDto.setPrice(0L);
    result = performPut(path, titleDto);
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

    // title
    titleDto = TestUtils.createUpdatebleTitleDto(id);
    titleDto.setTitle(null);
    result = performPut(path, titleDto);
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

    titleDto = TestUtils.createUpdatebleTitleDto(id);
    titleDto.setTitle("  ");
    result = performPut(path, titleDto);
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
  }

  @Test
  public void testWrongUri() throws Exception {
    ResponseEntity<String> result = performGet(WRONG_URI, String.class);
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  }
}
