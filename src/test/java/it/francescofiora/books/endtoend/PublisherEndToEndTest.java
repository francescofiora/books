package it.francescofiora.books.endtoend;

import static org.assertj.core.api.Assertions.assertThat;

import it.francescofiora.books.service.dto.PublisherDto;
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
class PublisherEndToEndTest extends AbstractTestEndToEnd {

  private static final String PUBLISHERS_URI = "/api/v1/publishers";
  private static final String PUBLISHERS_ID_URI = "/api/v1/publishers/%d";

  private static final String ALERT_CREATED = "PublisherDto.created";
  private static final String ALERT_UPDATED = "PublisherDto.updated";
  private static final String ALERT_DELETED = "PublisherDto.deleted";
  private static final String ALERT_GET = "PublisherDto.get";
  private static final String ALERT_BAD_REQUEST = "PublisherDto.badRequest";
  private static final String ALERT_NOT_FOUND = "PublisherDto.notFound";

  private static final String PARAM_PAGE_20 = "0 20";
  private static final String PARAM_NOT_VALID_LONG =
      "'id' should be a valid 'Long' and '999999999999999999999999' isn't";

  private static final String PARAM_ID_NOT_NULL = "[publisherDto.id - NotNull]";
  private static final String PARAM_NAME_NOT_BLANK = "[publisherDto.publisherName - NotBlank]";

  @Test
  void testAuth() {
    testUnauthorized(PUBLISHERS_URI);
  }

  @Test
  void testCreate() {
    var newPublisherDto = TestUtils.createNewPublisherDto();
    var publisherId =
        createAndReturnId(UserUtils.BOOK_ADMIN, PUBLISHERS_URI, newPublisherDto, ALERT_CREATED);

    final var publishersIdUri = String.format(PUBLISHERS_ID_URI, publisherId);

    var actual = get(UserUtils.BOOK_ADMIN, publishersIdUri, PublisherDto.class, ALERT_GET,
        String.valueOf(publisherId));
    assertThat(actual.getPublisherName()).isEqualTo(newPublisherDto.getPublisherName());

    var publisherDto = TestUtils.createPublisherDto(publisherId);
    update(UserUtils.BOOK_ADMIN, publishersIdUri, publisherDto, ALERT_UPDATED,
        String.valueOf(publisherId));

    actual = get(UserUtils.BOOK_ADMIN, publishersIdUri, PublisherDto.class, ALERT_GET,
        String.valueOf(publisherId));
    assertThat(actual).isEqualTo(publisherDto);
    assertThat(actual.getPublisherName()).isEqualTo(publisherDto.getPublisherName());

    var publishers = get(UserUtils.BOOK_ADMIN, PUBLISHERS_URI, PageRequest.of(1, 1),
        PublisherDto[].class, ALERT_GET, PARAM_PAGE_20);
    assertThat(publishers).isNotEmpty();
    var option =
        Stream.of(publishers).filter(publisher -> publisher.getId().equals(publisherId)).findAny();
    assertThat(option).isPresent().contains(publisherDto);

    delete(UserUtils.BOOK_ADMIN, publishersIdUri, ALERT_DELETED, String.valueOf(publisherId));

    assertGetNotFound(UserUtils.BOOK_ADMIN, publishersIdUri, PublisherDto.class, ALERT_NOT_FOUND,
        String.valueOf(publisherId));
  }

  @Test
  void testGetBadRequest() {
    assertGetBadRequest(UserUtils.BOOK_ADMIN, PUBLISHERS_URI + "/999999999999999999999999",
        String.class, "id.badRequest", PARAM_NOT_VALID_LONG);
  }

  @Test
  void testUpdateBadRequest() {
    assertUpdateBadRequest(UserUtils.BOOK_ADMIN, String.format(PUBLISHERS_ID_URI, 1L),
        TestUtils.createPublisherDto(null), ALERT_BAD_REQUEST, PARAM_ID_NOT_NULL);

    var id = createAndReturnId(UserUtils.BOOK_ADMIN, PUBLISHERS_URI,
        TestUtils.createNewPublisherDto(), ALERT_CREATED);

    assertUpdateBadRequest(UserUtils.BOOK_ADMIN, String.format(PUBLISHERS_ID_URI, id + 1),
        TestUtils.createPublisherDto(id), ALERT_BAD_REQUEST, String.valueOf(id));

    final var path = String.format(PUBLISHERS_ID_URI, id + 1);

    var publisherDto = TestUtils.createPublisherDto(id);
    publisherDto.setPublisherName(null);
    assertUpdateBadRequest(UserUtils.BOOK_ADMIN, path, publisherDto, ALERT_BAD_REQUEST,
        PARAM_NAME_NOT_BLANK);

    publisherDto = TestUtils.createPublisherDto(id);
    publisherDto.setPublisherName("");
    assertUpdateBadRequest(UserUtils.BOOK_ADMIN, path, publisherDto, ALERT_BAD_REQUEST,
        PARAM_NAME_NOT_BLANK);

    publisherDto = TestUtils.createPublisherDto(id);
    publisherDto.setPublisherName("  ");
    assertUpdateBadRequest(UserUtils.BOOK_ADMIN, path, publisherDto, ALERT_BAD_REQUEST,
        PARAM_NAME_NOT_BLANK);
  }
}
