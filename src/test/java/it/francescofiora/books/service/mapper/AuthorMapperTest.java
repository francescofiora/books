package it.francescofiora.books.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import it.francescofiora.books.domain.Author;
import it.francescofiora.books.service.dto.NewAuthorDto;
import it.francescofiora.books.service.dto.RefAuthorDto;
import org.junit.jupiter.api.Test;

class AuthorMapperTest {

  @Test
  void testNullObject() {
    var authorMapper = new AuthorMapperImpl();
    assertThat(authorMapper.toDto(null)).isNull();

    NewAuthorDto authorDto = null;
    assertThat(authorMapper.toEntity(authorDto)).isNull();

    RefAuthorDto dto = null;
    assertThat(authorMapper.toEntity(dto)).isNull();

    assertDoesNotThrow(() -> authorMapper.updateEntityFromDto(null, new Author()));
  }
}
