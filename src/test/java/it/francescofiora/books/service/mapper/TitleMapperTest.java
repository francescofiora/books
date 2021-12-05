package it.francescofiora.books.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.mock;

import it.francescofiora.books.domain.Title;
import it.francescofiora.books.service.dto.RefPublisherDto;
import it.francescofiora.books.service.dto.UpdatebleTitleDto;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@TestPropertySource(locations = {"classpath:application_test.properties"})
class TitleMapperTest {

  @Autowired
  private TitleMapper titleMapper;


  @Test
  void testEntityFromId() {
    var id = 1L;
    assertThat(titleMapper.fromId(id).getId()).isEqualTo(id);
    assertThat(titleMapper.fromId(null)).isNull();
  }

  @Test
  void testNullObject() {
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
    var titleDto = titleMapper.toDto(title);
    assertThat(titleDto.getAuthors()).isNull();
  }

  @Test
  void testUpdateEntityFromDto1() {
    var titleDto = new UpdatebleTitleDto();
    titleDto.setPublisher(new RefPublisherDto());
    var title = new Title();
    title.setAuthors(null);
    assertDoesNotThrow(() -> titleMapper.updateEntityFromDto(titleDto, title));
    assertThat(title.getAuthors()).isEmpty();
  }

  @Test
  void testUpdateEntityFromDto2() {
    var titleDto = new UpdatebleTitleDto();
    titleDto.setPublisher(new RefPublisherDto());
    titleDto.setAuthors(null);
    var title = new Title();
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
    assertDoesNotThrow(() -> titleMapper.updateEntityFromDto(titleDto, title));
    assertThat(title.getAuthors()).isNull();
  }

  @TestConfiguration
  static class TestContextConfiguration {

    @Bean
    TitleMapper titleMapper() {
      return new TitleMapperImpl();
    }

    @Bean
    AuthorMapper authorMapper() {
      return mock(AuthorMapper.class);
    }

    @Bean
    PublisherMapper publisherMapper() {
      return mock(PublisherMapper.class);
    }
  }
}
