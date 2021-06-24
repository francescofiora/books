package it.francescofiora.books.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import it.francescofiora.books.domain.Title;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TitleMapperTest {

  private TitleMapper titleMapper;

  @BeforeEach
  void setUp() {
    titleMapper = new TitleMapperImpl();
  }

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
  }
}
