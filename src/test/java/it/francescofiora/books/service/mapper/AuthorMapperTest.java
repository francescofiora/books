package it.francescofiora.books.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import it.francescofiora.books.service.dto.NewAuthorDto;
import it.francescofiora.books.service.dto.RefAuthorDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AuthorMapperTest {

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

  @Test
  void testNullObject() {
    assertThat(authorMapper.toDto(null)).isNull();

    NewAuthorDto authorDto = null;
    assertThat(authorMapper.toEntity(authorDto)).isNull();

    RefAuthorDto dto = null;
    assertThat(authorMapper.toEntity(dto)).isNull();
  }
}
