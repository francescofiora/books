package it.francescofiora.books.web.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import it.francescofiora.books.config.SecurityConfig;
import it.francescofiora.books.service.TitleService;
import it.francescofiora.books.service.dto.NewTitleDto;
import it.francescofiora.books.service.dto.TitleDto;
import it.francescofiora.books.util.TestUtils;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@Import({BuildProperties.class, SecurityConfig.class})
@WebMvcTest(controllers = TitleApi.class)
@TestPropertySource(locations = {"classpath:application_test.properties"})
class TitleApiTest extends AbstractTestApi {

  private static final Long ID = 1L;
  private static final String TITLES_URI = "/api/v1/titles";
  private static final String TITLES_ID_URI = "/api/v1/titles/{id}";
  private static final String WRONG_URI = "/api/v1/wrong";

  @MockitoBean
  private TitleService titleService;

  @Test
  void testCreateTitle() throws Exception {
    var newTitleDto = TestUtils.createNewTitleDto();
    var titleDto = TestUtils.createTitleDto(ID);
    given(titleService.create(any(NewTitleDto.class))).willReturn(titleDto);

    var result = performPost(TITLES_URI, newTitleDto).andExpect(status().isCreated()).andReturn();

    assertThat(result.getResponse().getHeaderValue(HttpHeaders.LOCATION))
        .isEqualTo(TITLES_URI + "/" + ID);
  }

  @Test
  void testCreateTitleBadRequest() throws Exception {
    // Authors
    var titleDto = TestUtils.createNewTitleDto();
    titleDto.getAuthors().get(0).setId(null);
    performPost(TITLES_URI, titleDto).andExpect(status().isBadRequest());

    titleDto = TestUtils.createNewTitleDto();
    titleDto.getAuthors().clear();
    performPost(TITLES_URI, titleDto).andExpect(status().isBadRequest());

    titleDto = TestUtils.createNewTitleDto();
    titleDto.setAuthors(null);
    performPost(TITLES_URI, titleDto).andExpect(status().isBadRequest());

    // Publisher
    titleDto = TestUtils.createNewTitleDto();
    titleDto.getPublisher().setId(null);
    performPost(TITLES_URI, titleDto).andExpect(status().isBadRequest());

    titleDto = TestUtils.createNewTitleDto();
    titleDto.setPublisher(null);
    performPost(TITLES_URI, titleDto).andExpect(status().isBadRequest());

    // copyright
    titleDto = TestUtils.createNewTitleDto();
    titleDto.setCopyright(null);
    performPost(TITLES_URI, titleDto).andExpect(status().isBadRequest());

    // editionNumber
    titleDto = TestUtils.createNewTitleDto();
    titleDto.setEditionNumber(null);
    performPost(TITLES_URI, titleDto).andExpect(status().isBadRequest());

    // language
    titleDto = TestUtils.createNewTitleDto();
    titleDto.setLanguage(null);
    performPost(TITLES_URI, titleDto).andExpect(status().isBadRequest());

    // price
    titleDto = TestUtils.createNewTitleDto();
    titleDto.setPrice(null);
    performPost(TITLES_URI, titleDto).andExpect(status().isBadRequest());

    titleDto = TestUtils.createNewTitleDto();
    titleDto.setPrice(0L);
    performPost(TITLES_URI, titleDto).andExpect(status().isBadRequest());

    // title
    titleDto = TestUtils.createNewTitleDto();
    titleDto.setName(null);
    performPost(TITLES_URI, titleDto).andExpect(status().isBadRequest());

    titleDto = TestUtils.createNewTitleDto();
    titleDto.setName("  ");
    performPost(TITLES_URI, titleDto).andExpect(status().isBadRequest());
  }

  @Test
  void testUpdateTitleBadRequest() throws Exception {
    // Authors
    var titleDto = TestUtils.createUpdatebleTitleDto(ID);
    titleDto.getAuthors().get(0).setId(null);
    performPut(TITLES_ID_URI, ID, titleDto).andExpect(status().isBadRequest());

    titleDto = TestUtils.createUpdatebleTitleDto(ID);
    titleDto.getAuthors().clear();
    performPut(TITLES_ID_URI, ID, titleDto).andExpect(status().isBadRequest());

    titleDto = TestUtils.createUpdatebleTitleDto(ID);
    titleDto.setAuthors(null);
    performPut(TITLES_ID_URI, ID, titleDto).andExpect(status().isBadRequest());

    // Publisher
    titleDto = TestUtils.createUpdatebleTitleDto(ID);
    titleDto.getPublisher().setId(null);
    performPut(TITLES_ID_URI, ID, titleDto).andExpect(status().isBadRequest());

    titleDto = TestUtils.createUpdatebleTitleDto(ID);
    titleDto.setPublisher(null);
    performPut(TITLES_ID_URI, ID, titleDto).andExpect(status().isBadRequest());

    // copyright
    titleDto = TestUtils.createUpdatebleTitleDto(ID);
    titleDto.setCopyright(null);
    performPut(TITLES_ID_URI, ID, titleDto).andExpect(status().isBadRequest());

    // editionNumber
    titleDto = TestUtils.createUpdatebleTitleDto(ID);
    titleDto.setEditionNumber(null);
    performPut(TITLES_ID_URI, ID, titleDto).andExpect(status().isBadRequest());

    // id
    titleDto = TestUtils.createUpdatebleTitleDto(ID);
    titleDto.setId(null);
    performPut(TITLES_ID_URI, ID, titleDto).andExpect(status().isBadRequest());

    // language
    titleDto = TestUtils.createUpdatebleTitleDto(ID);
    titleDto.setLanguage(null);
    performPut(TITLES_ID_URI, ID, titleDto).andExpect(status().isBadRequest());

    // price
    titleDto = TestUtils.createUpdatebleTitleDto(ID);
    titleDto.setPrice(null);
    performPut(TITLES_ID_URI, ID, titleDto).andExpect(status().isBadRequest());

    titleDto = TestUtils.createUpdatebleTitleDto(ID);
    titleDto.setPrice(0L);
    performPut(TITLES_ID_URI, ID, titleDto).andExpect(status().isBadRequest());

    // title
    titleDto = TestUtils.createUpdatebleTitleDto(ID);
    titleDto.setName(null);
    performPut(TITLES_ID_URI, ID, titleDto).andExpect(status().isBadRequest());

    titleDto = TestUtils.createUpdatebleTitleDto(ID);
    titleDto.setName("  ");
    performPut(TITLES_ID_URI, ID, titleDto).andExpect(status().isBadRequest());
  }

  @Test
  void testUpdateTitle() throws Exception {
    var titleDto = TestUtils.createUpdatebleTitleDto(ID);
    performPut(TITLES_ID_URI, ID, titleDto).andExpect(status().isOk());
  }

  @Test
  void testGetAllTitles() throws Exception {
    var pageable = PageRequest.of(1, 1);
    var expected = new TitleDto();
    expected.setId(ID);
    given(titleService.findAll(any(), any(Pageable.class)))
        .willReturn(new PageImpl<TitleDto>(List.of(expected)));
    var result = performGet(TITLES_URI, pageable).andExpect(status().isOk()).andReturn();
    var list = readValue(result, new TypeReference<List<TitleDto>>() {});
    assertThat(list).isNotNull().isNotEmpty();
    assertThat(list.get(0)).isEqualTo(expected);
  }

  @Test
  void testGetTitle() throws Exception {
    var expected = new TitleDto();
    expected.setId(ID);
    given(titleService.findOne(ID)).willReturn(Optional.of(expected));
    var result = performGet(TITLES_ID_URI, ID).andExpect(status().isOk()).andReturn();
    var actual = readValue(result, new TypeReference<TitleDto>() {});
    assertThat(actual).isNotNull().isEqualTo(expected);
  }

  @Test
  void testDeleteTitle() throws Exception {
    performDelete(TITLES_ID_URI, ID).andExpect(status().isNoContent()).andReturn();
  }

  @Test
  void testWrongUri() throws Exception {
    performGet(WRONG_URI).andExpect(status().isNotFound()).andReturn();
  }
}
