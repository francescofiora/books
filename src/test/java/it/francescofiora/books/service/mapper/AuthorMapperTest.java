package it.francescofiora.books.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AuthorMapperTest {

  private AuthorMapper authorMapper;

  @BeforeEach
  void setUp() {
    authorMapper = new AuthorMapperImpl();
  }

  @Test
  void testEntityFromId() {
    Long id = 1L;
    assertThat(authorMapper.fromId(id).getId()).isEqualTo(id);
    assertThat(authorMapper.fromId(null)).isNull();
  }
}
