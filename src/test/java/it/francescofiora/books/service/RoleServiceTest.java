package it.francescofiora.books.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import it.francescofiora.books.domain.Permission;
import it.francescofiora.books.domain.Role;
import it.francescofiora.books.repository.PermissionRepository;
import it.francescofiora.books.repository.RoleRepository;
import it.francescofiora.books.repository.UserRepository;
import it.francescofiora.books.service.dto.NewRoleDto;
import it.francescofiora.books.service.dto.PermissionDto;
import it.francescofiora.books.service.dto.RefPermissionDto;
import it.francescofiora.books.service.dto.RoleDto;
import it.francescofiora.books.service.impl.RoleServiceImpl;
import it.francescofiora.books.service.mapper.PermissionMapper;
import it.francescofiora.books.service.mapper.RoleMapper;
import it.francescofiora.books.web.errors.NotFoundAlertException;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

class RoleServiceTest {

  private static final Long ID = 1L;

  @Test
  void testFindPermissions() {
    var permissionMapper = mock(PermissionMapper.class);
    var permissionRepository = mock(PermissionRepository.class);
    var roleService = new RoleServiceImpl(permissionMapper, mock(RoleMapper.class),
        mock(UserRepository.class), permissionRepository, mock(RoleRepository.class));

    var permission = new Permission();
    when(permissionRepository.findAll(any(Pageable.class)))
        .thenReturn(new PageImpl<>(List.of(permission)));
    var expected = new PermissionDto();
    when(permissionMapper.toDto(any(Permission.class))).thenReturn(expected);
    var pageable = PageRequest.of(1, 1);
    var page = roleService.findPermissions(pageable);
    assertThat(page.getContent().get(0)).isEqualTo(expected);
  }

  @Test
  void testCreateRole() {
    var roleMapper = mock(RoleMapper.class);
    var roleRepository = mock(RoleRepository.class);
    var roleService = new RoleServiceImpl(mock(PermissionMapper.class), roleMapper,
        mock(UserRepository.class), mock(PermissionRepository.class), roleRepository);

    var role = new Role();
    when(roleMapper.toEntity(any(NewRoleDto.class))).thenReturn(role);
    when(roleRepository.save(any(Role.class))).thenReturn(role);

    var expected = new RoleDto();
    when(roleMapper.toDto(any(Role.class))).thenReturn(expected);

    var roleDto = new NewRoleDto();
    var actual = roleService.createRole(roleDto);
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void testUpdateNotFound() {
    var roleService = new RoleServiceImpl(mock(PermissionMapper.class), mock(RoleMapper.class),
        mock(UserRepository.class), mock(PermissionRepository.class), mock(RoleRepository.class));

    var roleDto = new RoleDto();
    assertThrows(NotFoundAlertException.class, () -> roleService.updateRole(roleDto));
  }

  @Test
  void testUpdateWithPermissionNotFound() {
    var role = new Role();
    var roleRepository = mock(RoleRepository.class);
    when(roleRepository.findById(ID)).thenReturn(Optional.of(role));

    var roleService = new RoleServiceImpl(mock(PermissionMapper.class), mock(RoleMapper.class),
        mock(UserRepository.class), mock(PermissionRepository.class), roleRepository);

    var roleDto = new RoleDto();
    roleDto.setId(ID);
    roleDto.getPermissions().add(new RefPermissionDto());
    assertThrows(NotFoundAlertException.class, () -> roleService.updateRole(roleDto));
  }

  @Test
  void testUpdate() {
    var roleMapper = mock(RoleMapper.class);
    var roleRepository = mock(RoleRepository.class);
    var roleService = new RoleServiceImpl(mock(PermissionMapper.class), roleMapper,
        mock(UserRepository.class), mock(PermissionRepository.class), roleRepository);

    var role = new Role();
    when(roleRepository.findById(ID)).thenReturn(Optional.of(role));

    var roleDto = new RoleDto();
    roleDto.setId(ID);
    roleService.updateRole(roleDto);
    verify(roleMapper).updateEntityFromDto(roleDto, role);
    verify(roleRepository).save(role);
  }

  @Test
  void testFindAll() {
    var role = new Role();
    var roleRepository = mock(RoleRepository.class);
    when(roleRepository.findAll(ArgumentMatchers.<Example<Role>>any(), any(Pageable.class)))
        .thenReturn(new PageImpl<>(List.of(role)));

    var expected = new RoleDto();
    var roleMapper = mock(RoleMapper.class);
    when(roleMapper.toDto(any(Role.class))).thenReturn(expected);
    var pageable = PageRequest.of(1, 1);

    var roleService = new RoleServiceImpl(mock(PermissionMapper.class), roleMapper,
        mock(UserRepository.class), mock(PermissionRepository.class), roleRepository);

    var page = roleService.findRoles(null, null, pageable);
    assertThat(page.getContent().get(0)).isEqualTo(expected);
  }

  @Test
  void testFindOneNotFound() {
    var roleService = new RoleServiceImpl(mock(PermissionMapper.class), mock(RoleMapper.class),
        mock(UserRepository.class), mock(PermissionRepository.class), mock(RoleRepository.class));

    var roleOpt = roleService.findOneRole(ID);
    assertThat(roleOpt).isNotPresent();
  }

  @Test
  void testFindOne() {
    var role = new Role();
    role.setId(ID);
    var roleRepository = mock(RoleRepository.class);
    when(roleRepository.findById(role.getId())).thenReturn(Optional.of(role));

    var expected = new RoleDto();
    var roleMapper = mock(RoleMapper.class);
    when(roleMapper.toDto(any(Role.class))).thenReturn(expected);

    var roleService = new RoleServiceImpl(mock(PermissionMapper.class), roleMapper,
        mock(UserRepository.class), mock(PermissionRepository.class), roleRepository);

    var roleOpt = roleService.findOneRole(ID);
    assertThat(roleOpt).isPresent();
    var actual = roleOpt.get();
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void testDelete() {
    var roleRepository = mock(RoleRepository.class);
    var roleService = new RoleServiceImpl(mock(PermissionMapper.class), mock(RoleMapper.class),
        mock(UserRepository.class), mock(PermissionRepository.class), roleRepository);

    roleService.deleteRole(ID);
    verify(roleRepository).deleteById(ID);
  }
}
