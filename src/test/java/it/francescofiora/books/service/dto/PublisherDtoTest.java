package it.francescofiora.books.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import it.francescofiora.books.TestUtil;

public class PublisherDtoTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PublisherDto.class);
        PublisherDto publisherDto1 = new PublisherDto();
        publisherDto1.setId(1L);
        PublisherDto publisherDto2 = new PublisherDto();
        assertThat(publisherDto1).isNotEqualTo(publisherDto2);
        publisherDto2.setId(publisherDto1.getId());
        assertThat(publisherDto1).isEqualTo(publisherDto2);
        publisherDto2.setId(2L);
        assertThat(publisherDto1).isNotEqualTo(publisherDto2);
        publisherDto1.setId(null);
        assertThat(publisherDto1).isNotEqualTo(publisherDto2);
    }
}
