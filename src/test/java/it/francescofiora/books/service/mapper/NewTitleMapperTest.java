package it.francescofiora.books.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

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
