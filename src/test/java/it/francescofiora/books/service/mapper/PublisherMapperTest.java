package it.francescofiora.books.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import it.francescofiora.books.service.dto.NewPublisherDto;
import it.francescofiora.books.service.dto.RefPublisherDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PublisherMapperTest {

  private PublisherMapper publisherMapper;

  @BeforeEach
  void setUp() {
    publisherMapper = new PublisherMapperImpl();
  }

  @Test
  void testEntityFromId() {
    Long id = 1L;
    assertThat(publisherMapper.fromId(id).getId()).isEqualTo(id);
    assertThat(publisherMapper.fromId(null)).isNull();
  }

  @Test
  void testNullObject() {
    assertThat(publisherMapper.toDto(null)).isNull();

    NewPublisherDto publisherDto = null;
    assertThat(publisherMapper.toEntity(publisherDto)).isNull();

    RefPublisherDto dto = null;
    assertThat(publisherMapper.toEntity(dto)).isNull();
  }
}
