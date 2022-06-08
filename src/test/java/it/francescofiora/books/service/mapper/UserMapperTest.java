package it.francescofiora.books.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import it.francescofiora.books.service.dto.NewUserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserMapperTest {

  private UserMapper userMapper;

  @BeforeEach
  void setUp() {
    userMapper = new UserMapperImpl();
  }

  @Test
  void testNullObject() {
    assertThat(userMapper.toDto(null)).isNull();

    NewUserDto userDto = null;
    assertThat(userMapper.toEntity(userDto)).isNull();
  }
}
