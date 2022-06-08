package it.francescofiora.books.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import it.francescofiora.books.service.dto.RefPermissionDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PermissionMapperTest {

  private PermissionMapper permissionMapper;

  @BeforeEach
  void setUp() {
    permissionMapper = new PermissionMapperImpl();
  }

  @Test
  void testNullObject() {
    assertThat(permissionMapper.toDto(null)).isNull();

    RefPermissionDto refPermDto = null;
    assertThat(permissionMapper.toEntity(refPermDto)).isNull();
  }
}
