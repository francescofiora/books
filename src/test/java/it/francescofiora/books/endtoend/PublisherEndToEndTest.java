package it.francescofiora.books.endtoend;

import static org.assertj.core.api.Assertions.assertThat;

import it.francescofiora.books.service.dto.PublisherDto;
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
public class PublisherEndToEndTest extends AbstractTestEndToEnd {

  private static final String PUBLISHERS_URI = "/api/publishers";
  private static final String PUBLISHERS_ID_URI = "/api/publishers/%d";

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
  public void testAuth() throws Exception {
    testUnauthorized(PUBLISHERS_URI);
  }

  @Test
  public void testCreate() throws Exception {
    Long publisherId =
        createAndReturnId(PUBLISHERS_URI, TestUtils.createNewPublisherDto(), ALERT_CREATED);

    final String publishersIdUri = String.format(PUBLISHERS_ID_URI, publisherId);

    PublisherDto publisherDto = TestUtils.createPublisherDto(publisherId);
    update(publishersIdUri, publisherDto, ALERT_UPDATED, String.valueOf(publisherId));

    PublisherDto actual =
        get(publishersIdUri, PublisherDto.class, ALERT_GET, String.valueOf(publisherId));
    assertThat(actual).isEqualTo(publisherDto);
    assertThat(actual.getPublisherName()).isEqualTo(publisherDto.getPublisherName());

    PublisherDto[] publishers =
        get(PUBLISHERS_URI, PageRequest.of(1, 1), PublisherDto[].class, ALERT_GET, PARAM_PAGE_20);
    assertThat(publishers).isNotEmpty();
    Optional<PublisherDto> option =
        Stream.of(publishers).filter(publisher -> publisher.getId().equals(publisherId)).findAny();
    assertThat(option).isPresent();
    assertThat(option.get()).isEqualTo(publisherDto);

    delete(publishersIdUri, ALERT_DELETED, String.valueOf(publisherId));

    assertGetNotFound(publishersIdUri, PublisherDto.class, ALERT_NOT_FOUND,
        String.valueOf(publisherId));
  }

  @Test
  public void testGetPublisherBadRequest() throws Exception {
    assertGetBadRequest(PUBLISHERS_URI + "/999999999999999999999999", String.class, "id.badRequest",
        PARAM_NOT_VALID_LONG);
  }

  @Test
  public void testUpdatePublisherBadRequest() throws Exception {
    assertUpdateBadRequest(String.format(PUBLISHERS_ID_URI, 1L), TestUtils.createPublisherDto(null),
        ALERT_BAD_REQUEST, PARAM_ID_NOT_NULL);

    Long id = createAndReturnId(PUBLISHERS_URI, TestUtils.createNewPublisherDto(), ALERT_CREATED);

    assertUpdateBadRequest(String.format(PUBLISHERS_ID_URI, id + 1),
        TestUtils.createPublisherDto(id), ALERT_BAD_REQUEST, String.valueOf(id));

    final String path = String.format(PUBLISHERS_ID_URI, id + 1);

    PublisherDto publisherDto = TestUtils.createPublisherDto(id);
    publisherDto.setPublisherName(null);
    assertUpdateBadRequest(path, publisherDto, ALERT_BAD_REQUEST, PARAM_NAME_NOT_BLANK);

    publisherDto = TestUtils.createPublisherDto(id);
    publisherDto.setPublisherName("");
    assertUpdateBadRequest(path, publisherDto, ALERT_BAD_REQUEST, PARAM_NAME_NOT_BLANK);

    publisherDto = TestUtils.createPublisherDto(id);
    publisherDto.setPublisherName("  ");
    assertUpdateBadRequest(path, publisherDto, ALERT_BAD_REQUEST, PARAM_NAME_NOT_BLANK);
  }
}
