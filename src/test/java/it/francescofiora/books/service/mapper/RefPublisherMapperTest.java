package it.francescofiora.books.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class RefPublisherMapperTest {

  private RefPublisherMapper publisherMapper;

  @BeforeEach
  public void setUp() {
    publisherMapper = new RefPublisherMapperImpl();
  }

  @Test
  public void testEntityFromId() {
    Long id = 1L;
    assertThat(publisherMapper.fromId(id).getId()).isEqualTo(id);
    assertThat(publisherMapper.fromId(null)).isNull();
  }
}
