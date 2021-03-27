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

  @Test
  public void testAuth() throws Exception {
    testUnauthorized(PUBLISHERS_URI);
  }

  @Test
  public void testCreate() throws Exception {
    Long publisherId =
        createResource(PUBLISHERS_URI, TestUtils.createNewPublisherDto(), "Publisher.created");

    final String publishersIdUri = String.format(PUBLISHERS_ID_URI, publisherId);

    PublisherDto publisherDto = TestUtils.createPublisherDto(publisherId);
    updateResource(publishersIdUri, publisherDto, "Publisher.updated");

    PublisherDto actual = getResource(publishersIdUri, PublisherDto.class);
    assertThat(actual).isEqualTo(publisherDto);

    PublisherDto[] publishers =
        getResource(PUBLISHERS_URI, PageRequest.of(1, 1), PublisherDto[].class);
    assertThat(publishers).isNotEmpty();
    Optional<PublisherDto> option =
        Stream.of(publishers).filter(publisher -> publisher.getId().equals(publisherId)).findAny();
    assertThat(option).isPresent();
    assertThat(option.get()).isEqualTo(publisherDto);

    deleteResource(publishersIdUri);

    getResourceNotFound(publishersIdUri, PublisherDto.class);
  }

  @Test
  public void testUpdatePublisherBadRequest() throws Exception {
    updateResourceBadRequest(String.format(PUBLISHERS_ID_URI, 1L),
        TestUtils.createPublisherDto(null));

    Long id =
        createResource(PUBLISHERS_URI, TestUtils.createNewPublisherDto(), "Publisher.created");

    updateResourceBadRequest(String.format(PUBLISHERS_ID_URI, id + 1),
        TestUtils.createPublisherDto(id));

    final String path = String.format(PUBLISHERS_ID_URI, id + 1);

    PublisherDto publisherDto = TestUtils.createPublisherDto(id);
    publisherDto.setPublisherName(null);
    updateResourceBadRequest(path, publisherDto);

    publisherDto = TestUtils.createPublisherDto(id);
    publisherDto.setPublisherName("");
    updateResourceBadRequest(path, publisherDto);

    publisherDto = TestUtils.createPublisherDto(id);
    publisherDto.setPublisherName("  ");
    updateResourceBadRequest(path, publisherDto);
  }
}
