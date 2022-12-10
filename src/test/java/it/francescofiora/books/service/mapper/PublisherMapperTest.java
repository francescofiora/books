package it.francescofiora.books.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import it.francescofiora.books.domain.Publisher;
import it.francescofiora.books.service.dto.NewPublisherDto;
import it.francescofiora.books.service.dto.RefPublisherDto;
import org.junit.jupiter.api.Test;

class PublisherMapperTest {

  @Test
  void testEntityFromId() {
    var id = 1L;
    var publisherMapper = new PublisherMapperImpl();
    assertThat(publisherMapper.fromId(id).getId()).isEqualTo(id);
    assertThat(publisherMapper.fromId(null)).isNull();
  }

  @Test
  void testNullObject() {
    var publisherMapper = new PublisherMapperImpl();
    assertThat(publisherMapper.toDto(null)).isNull();

    NewPublisherDto publisherDto = null;
    assertThat(publisherMapper.toEntity(publisherDto)).isNull();

    RefPublisherDto dto = null;
    assertThat(publisherMapper.toEntity(dto)).isNull();

    assertDoesNotThrow(() -> publisherMapper.updateEntityFromDto(null, new Publisher()));
  }
}
