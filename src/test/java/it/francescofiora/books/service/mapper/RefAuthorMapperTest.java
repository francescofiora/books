package it.francescofiora.books.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RefAuthorMapperTest {
  private RefAuthorMapper authorMapper;

  @BeforeEach
  public void setUp() {
    authorMapper = new RefAuthorMapperImpl();
  }

  @Test
  public void testEntityFromId() {
    Long id = 1L;
    assertThat(authorMapper.fromId(id).getId()).isEqualTo(id);
    assertThat(authorMapper.fromId(null)).isNull();
  }

}
