package it.francescofiora.books.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import it.francescofiora.books.domain.Role;
import it.francescofiora.books.service.dto.NewRoleDto;
import it.francescofiora.books.service.dto.RefRoleDto;
import it.francescofiora.books.service.dto.RoleDto;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

class RoleMapperTest {

  @Test
  void testNullObject() {
    var roleMapper = new RoleMapperImpl();
    ReflectionTestUtils.setField(roleMapper, "permissionMapper", new PermissionMapperImpl());
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
    var roleMapper = new RoleMapperImpl();
    ReflectionTestUtils.setField(roleMapper, "permissionMapper", new PermissionMapperImpl());
    var roleDto = roleMapper.toDto(role);
    assertThat(roleDto.getPermissions()).isNull();
  }

  @Test
  void testUpdateEntityFromDto1() {
    var roleDto = new RoleDto();
    var role = new Role();
    role.setPermissions(null);
    var roleMapper = new RoleMapperImpl();
    ReflectionTestUtils.setField(roleMapper, "permissionMapper", new PermissionMapperImpl());
    assertDoesNotThrow(() -> roleMapper.updateEntityFromDto(roleDto, role));
    assertThat(role.getPermissions()).isEmpty();
  }

  @Test
  void testUpdateEntityFromDto2() {
    var roleDto = new RoleDto();
    roleDto.setPermissions(null);
    var role = new Role();
    var roleMapper = new RoleMapperImpl();
    ReflectionTestUtils.setField(roleMapper, "permissionMapper", new PermissionMapperImpl());
    assertDoesNotThrow(() -> roleMapper.updateEntityFromDto(roleDto, role));
    assertThat(role.getPermissions()).isNull();
  }

  @Test
  void testUpdateEntityFromDto3() {
    var roleDto = new RoleDto();
    roleDto.setPermissions(null);
    var role = new Role();
    role.setPermissions(null);
    var roleMapper = new RoleMapperImpl();
    ReflectionTestUtils.setField(roleMapper, "permissionMapper", new PermissionMapperImpl());
    assertDoesNotThrow(() -> roleMapper.updateEntityFromDto(roleDto, role));
    assertThat(role.getPermissions()).isNull();
  }
}
