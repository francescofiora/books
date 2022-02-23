package it.francescofiora.books.web.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import it.francescofiora.books.service.PublisherService;
import it.francescofiora.books.service.dto.NewPublisherDto;
import it.francescofiora.books.service.dto.PublisherDto;
import it.francescofiora.books.util.TestUtils;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = PublisherApi.class)
@TestPropertySource(locations = {"classpath:application_test.properties"})
class PublisherApiTest extends AbstractTestApi {

  private static final Long ID = 1L;
  private static final String PUBLISHERS_URI = "/books/api/v1/publishers";
  private static final String PUBLISHERS_ID_URI = "/books/api/v1/publishers/{id}";
  private static final String WRONG_URI = "/books/api/v1/wrong";

  @MockBean
  private PublisherService publisherService;

  @Test
  void testCreatePublisher() throws Exception {
    var newPublisherDto = TestUtils.createNewPublisherDto();
    var publisherDto = TestUtils.createPublisherDto(ID);
    given(publisherService.create(any(NewPublisherDto.class))).willReturn(publisherDto);
    var result =
        performPost(PUBLISHERS_URI, newPublisherDto).andExpect(status().isCreated()).andReturn();
    assertThat(result.getResponse().getHeaderValue(HttpHeaders.LOCATION))
        .isEqualTo(PUBLISHERS_URI + "/" + ID);
  }

  @Test
  void testUpdatePublisherBadRequest() throws Exception {
    var publisherDto = TestUtils.createPublisherDto(null);
    performPut(PUBLISHERS_ID_URI, ID, publisherDto).andExpect(status().isBadRequest());

    publisherDto = TestUtils.createPublisherDto(ID);
    publisherDto.setPublisherName(null);
    performPut(PUBLISHERS_ID_URI, ID, publisherDto).andExpect(status().isBadRequest());

    publisherDto = TestUtils.createPublisherDto(ID);
    publisherDto.setPublisherName("");
    performPut(PUBLISHERS_ID_URI, ID, publisherDto).andExpect(status().isBadRequest());

    publisherDto = TestUtils.createPublisherDto(ID);
    publisherDto.setPublisherName("  ");
    performPut(PUBLISHERS_ID_URI, ID, publisherDto).andExpect(status().isBadRequest());
  }

  @Test
  void testUpdatePublisher() throws Exception {
    var publisherDto = TestUtils.createPublisherDto(ID);
    performPut(PUBLISHERS_ID_URI, ID, publisherDto).andExpect(status().isOk());
  }

  @Test
  void testGetAllPublishers() throws Exception {
    var pageable = PageRequest.of(1, 1);
    var expected = TestUtils.createPublisherDto(ID);
    given(publisherService.findAll(any(Pageable.class)))
        .willReturn(new PageImpl<PublisherDto>(List.of(expected)));
    var result = performGet(PUBLISHERS_URI, pageable).andExpect(status().isOk()).andReturn();
    var list = readValue(result, new TypeReference<List<PublisherDto>>() {});
    assertThat(list).isNotNull().isNotEmpty();
    assertThat(list.get(0)).isEqualTo(expected);
  }

  @Test
  void testGetPublisher() throws Exception {
    var expected = TestUtils.createPublisherDto(ID);
    given(publisherService.findOne(ID)).willReturn(Optional.of(expected));
    var result = performGet(PUBLISHERS_ID_URI, ID).andExpect(status().isOk()).andReturn();
    var actual = readValue(result, new TypeReference<PublisherDto>() {});
    assertThat(actual).isNotNull().isEqualTo(expected);
  }

  @Test
  void testDeletePublisher() throws Exception {
    performDelete(PUBLISHERS_ID_URI, ID).andExpect(status().isNoContent()).andReturn();
  }

  @Test
  void testWrongUri() throws Exception {
    performGet(WRONG_URI).andExpect(status().isNotFound()).andReturn();
  }
}
