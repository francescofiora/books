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
    update(publishersIdUri, publisherDto, ALERT_UPDATED);

    PublisherDto actual = get(publishersIdUri, PublisherDto.class, ALERT_GET);
    assertThat(actual).isEqualTo(publisherDto);
    assertThat(actual.getPublisherName()).isEqualTo(publisherDto.getPublisherName());

    PublisherDto[] publishers =
        get(PUBLISHERS_URI, PageRequest.of(1, 1), PublisherDto[].class, ALERT_GET);
    assertThat(publishers).isNotEmpty();
    Optional<PublisherDto> option =
        Stream.of(publishers).filter(publisher -> publisher.getId().equals(publisherId)).findAny();
    assertThat(option).isPresent();
    assertThat(option.get()).isEqualTo(publisherDto);

    delete(publishersIdUri, ALERT_DELETED);

    assertGetNotFound(publishersIdUri, PublisherDto.class, ALERT_NOT_FOUND);
  }

  @Test
  public void testUpdatePublisherBadRequest() throws Exception {
    assertUpdateBadRequest(String.format(PUBLISHERS_ID_URI, 1L), TestUtils.createPublisherDto(null),
        ALERT_BAD_REQUEST);

    Long id = createAndReturnId(PUBLISHERS_URI, TestUtils.createNewPublisherDto(), ALERT_CREATED);

    assertUpdateBadRequest(String.format(PUBLISHERS_ID_URI, id + 1),
        TestUtils.createPublisherDto(id), ALERT_BAD_REQUEST);

    final String path = String.format(PUBLISHERS_ID_URI, id + 1);

    PublisherDto publisherDto = TestUtils.createPublisherDto(id);
    publisherDto.setPublisherName(null);
    assertUpdateBadRequest(path, publisherDto, ALERT_BAD_REQUEST);

    publisherDto = TestUtils.createPublisherDto(id);
    publisherDto.setPublisherName("");
    assertUpdateBadRequest(path, publisherDto, ALERT_BAD_REQUEST);

    publisherDto = TestUtils.createPublisherDto(id);
    publisherDto.setPublisherName("  ");
    assertUpdateBadRequest(path, publisherDto, ALERT_BAD_REQUEST);
  }
}
