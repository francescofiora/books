package it.francescofiora.books.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import it.francescofiora.books.TestUtil;

public class TitleDtoTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TitleDto.class);
        TitleDto titleDto1 = new TitleDto();
        titleDto1.setId(1L);
        TitleDto titleDto2 = new TitleDto();
        assertThat(titleDto1).isNotEqualTo(titleDto2);
        titleDto2.setId(titleDto1.getId());
        assertThat(titleDto1).isEqualTo(titleDto2);
        titleDto2.setId(2L);
        assertThat(titleDto1).isNotEqualTo(titleDto2);
        titleDto1.setId(null);
        assertThat(titleDto1).isNotEqualTo(titleDto2);
    }
}
