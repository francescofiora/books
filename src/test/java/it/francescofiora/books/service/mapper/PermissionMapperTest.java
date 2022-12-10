package it.francescofiora.books.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import it.francescofiora.books.service.dto.RefPermissionDto;
import org.junit.jupiter.api.Test;

class PermissionMapperTest {

  @Test
  void testNullObject() {
    var permissionMapper = new PermissionMapperImpl();
    assertThat(permissionMapper.toDto(null)).isNull();

    RefPermissionDto refPermDto = null;
    assertThat(permissionMapper.toEntity(refPermDto)).isNull();
  }
}
