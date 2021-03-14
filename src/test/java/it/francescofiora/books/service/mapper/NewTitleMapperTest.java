package it.francescofiora.books.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class NewTitleMapperTest {

  private NewTitleMapper titleMapper;

  @BeforeEach
  public void setUp() {
    titleMapper = new NewTitleMapperImpl();
  }

  @Test
  public void testEntityFromId() {
    Long id = 1L;
    assertThat(titleMapper.fromId(id).getId()).isEqualTo(id);
    assertThat(titleMapper.fromId(null)).isNull();
  }
}
