package it.francescofiora.books.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import it.francescofiora.books.service.dto.NewUserDto;
import org.junit.jupiter.api.Test;

class UserMapperTest {

  @Test
  void testNullObject() {
    var userMapper = new UserMapperImpl();
    assertThat(userMapper.toDto(null)).isNull();

    NewUserDto userDto = null;
    assertThat(userMapper.toEntity(userDto)).isNull();
  }
}
