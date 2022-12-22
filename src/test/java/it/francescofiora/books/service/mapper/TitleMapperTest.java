package it.francescofiora.books.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import it.francescofiora.books.domain.Title;
import it.francescofiora.books.service.dto.RefPublisherDto;
import it.francescofiora.books.service.dto.UpdatebleTitleDto;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

class TitleMapperTest {

  @Test
  void testEntityFromId() {
    var id = 1L;
    var titleMapper = createTitleMapper();
    assertThat(titleMapper.fromId(id).getId()).isEqualTo(id);
    assertThat(titleMapper.fromId(null)).isNull();
  }

  @Test
  void testNullObject() {
    var titleMapper = createTitleMapper();
    assertThat(titleMapper.toEntity(null)).isNull();

    Title title = null;
    assertThat(titleMapper.toDto(title)).isNull();

    List<Title> list = null;
    assertThat(titleMapper.toDto(list)).isNull();

    assertDoesNotThrow(() -> titleMapper.updateEntityFromDto(null, new Title()));
  }

  @Test
  void testToDto() {
    var title = new Title();
    title.setAuthors(null);
    var titleMapper = createTitleMapper();
    var titleDto = titleMapper.toDto(title);
    assertThat(titleDto.getAuthors()).isNull();
  }

  @Test
  void testUpdateEntityFromDto1() {
    var titleDto = new UpdatebleTitleDto();
    titleDto.setPublisher(new RefPublisherDto());
    var title = new Title();
    title.setAuthors(null);
    var titleMapper = createTitleMapper();
    assertDoesNotThrow(() -> titleMapper.updateEntityFromDto(titleDto, title));
    assertThat(title.getAuthors()).isEmpty();
  }

  @Test
  void testUpdateEntityFromDto2() {
    var titleDto = new UpdatebleTitleDto();
    titleDto.setPublisher(new RefPublisherDto());
    titleDto.setAuthors(null);
    var title = new Title();
    var titleMapper = createTitleMapper();
    assertDoesNotThrow(() -> titleMapper.updateEntityFromDto(titleDto, title));
    assertThat(title.getAuthors()).isNull();
  }

  @Test
  void testUpdateEntityFromDto3() {
    var titleDto = new UpdatebleTitleDto();
    titleDto.setPublisher(new RefPublisherDto());
    titleDto.setAuthors(null);
    var title = new Title();
    title.setAuthors(null);
    var titleMapper = createTitleMapper();
    assertDoesNotThrow(() -> titleMapper.updateEntityFromDto(titleDto, title));
    assertThat(title.getAuthors()).isNull();
  }

  private TitleMapper createTitleMapper() {
    var titleMapper = new TitleMapperImpl();
    ReflectionTestUtils.setField(titleMapper, "authorMapper", new AuthorMapperImpl());
    ReflectionTestUtils.setField(titleMapper, "publisherMapper", new PublisherMapperImpl());
    return titleMapper;
  }
}
