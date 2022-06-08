package it.francescofiora.books.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import it.francescofiora.books.domain.Role;
import it.francescofiora.books.service.dto.NewRoleDto;
import it.francescofiora.books.service.dto.RefRoleDto;
import it.francescofiora.books.service.dto.RoleDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@TestPropertySource(locations = {"classpath:application_test.properties"})
class RoleMapperTest {

  @Autowired
  private RoleMapper roleMapper;

  @Test
  void testNullObject() {
    assertThat(roleMapper.toDto(null)).isNull();

    NewRoleDto roleDto = null;
    assertThat(roleMapper.toEntity(roleDto)).isNull();

    RefRoleDto dto = null;
    assertThat(roleMapper.toEntity(dto)).isNull();

    assertDoesNotThrow(() -> roleMapper.updateEntityFromDto(null, new Role()));
  }

  @Test
  void testToDto() {
    var role = new Role();
    role.setPermissions(null);
    var roleDto = roleMapper.toDto(role);
    assertThat(roleDto.getPermissions()).isNull();
  }

  @Test
  void testUpdateEntityFromDto1() {
    var roleDto = new RoleDto();
    var role = new Role();
    role.setPermissions(null);
    assertDoesNotThrow(() -> roleMapper.updateEntityFromDto(roleDto, role));
    assertThat(role.getPermissions()).isEmpty();
  }

  @Test
  void testUpdateEntityFromDto2() {
    var roleDto = new RoleDto();
    roleDto.setPermissions(null);
    var role = new Role();
    assertDoesNotThrow(() -> roleMapper.updateEntityFromDto(roleDto, role));
    assertThat(role.getPermissions()).isNull();
  }

  @Test
  void testUpdateEntityFromDto3() {
    var roleDto = new RoleDto();
    roleDto.setPermissions(null);
    var role = new Role();
    role.setPermissions(null);
    assertDoesNotThrow(() -> roleMapper.updateEntityFromDto(roleDto, role));
    assertThat(role.getPermissions()).isNull();
  }

  @TestConfiguration
  static class TestContextConfiguration {
    @Bean
    RoleMapper roleMapper() {
      return new RoleMapperImpl();
    }

    @Bean
    PermissionMapper permissionMapper() {
      return new PermissionMapperImpl();
    }
  }
}
