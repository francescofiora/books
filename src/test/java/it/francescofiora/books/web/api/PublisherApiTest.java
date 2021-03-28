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
import java.util.Collections;
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
import org.springframework.test.web.servlet.MvcResult;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = PublisherApi.class)
@TestPropertySource(locations = {"classpath:application_test.properties"})
public class PublisherApiTest extends AbstractTestApi {

  private static final Long ID = 1L;
  private static final String PUBLISHERS_URI = "/api/publishers";
  private static final String PUBLISHERS_ID_URI = "/api/publishers/{id}";
  private static final String WRONG_URI = "/api/wrong";

  @MockBean
  private PublisherService publisherService;

  @Test
  public void testCreatePublisher() throws Exception {
    NewPublisherDto newPublisherDto = TestUtils.createNewPublisherDto();
    PublisherDto publisherDto = TestUtils.createPublisherDto(ID);
    given(publisherService.create(any(NewPublisherDto.class))).willReturn(publisherDto);
    MvcResult result =
        performPost(PUBLISHERS_URI, newPublisherDto).andExpect(status().isCreated()).andReturn();
    assertThat(result.getResponse().getHeaderValue(HttpHeaders.LOCATION))
        .isEqualTo(PUBLISHERS_URI + "/" + ID);
  }

  @Test
  public void testUpdatePublisherBadRequest() throws Exception {
    PublisherDto publisherDto = TestUtils.createPublisherDto(null);
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
  public void testUpdatePublisher() throws Exception {
    PublisherDto publisherDto = TestUtils.createPublisherDto(ID);
    performPut(PUBLISHERS_ID_URI, ID, publisherDto).andExpect(status().isOk());
  }

  @Test
  public void testGetAllPublishers() throws Exception {
    Pageable pageable = PageRequest.of(1, 1);
    PublisherDto expected = TestUtils.createPublisherDto(ID);
    given(publisherService.findAll(any(Pageable.class)))
        .willReturn(new PageImpl<PublisherDto>(Collections.singletonList(expected)));
    MvcResult result = performGet(PUBLISHERS_URI, pageable).andExpect(status().isOk()).andReturn();
    List<PublisherDto> list = readValue(result, new TypeReference<List<PublisherDto>>() {});
    assertThat(list).isNotNull().isNotEmpty();
    assertThat(list.get(0)).isEqualTo(expected);
  }

  @Test
  public void testGetPublisher() throws Exception {
    PublisherDto expected = TestUtils.createPublisherDto(ID);
    given(publisherService.findOne(eq(ID))).willReturn(Optional.of(expected));
    MvcResult result = performGet(PUBLISHERS_ID_URI, ID).andExpect(status().isOk()).andReturn();
    PublisherDto actual = readValue(result, new TypeReference<PublisherDto>() {});
    assertThat(actual).isNotNull().isEqualTo(expected);
  }

  @Test
  public void testDeletePublisher() throws Exception {
    performDelete(PUBLISHERS_ID_URI, ID).andExpect(status().isNoContent()).andReturn();
  }

  @Test
  public void testWrongUri() throws Exception {
    performGet(WRONG_URI).andExpect(status().isNotFound()).andReturn();
  }
}
