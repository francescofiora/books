package it.francescofiora.books.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import it.francescofiora.books.domain.Permission;
import it.francescofiora.books.domain.Role;
import it.francescofiora.books.repository.PermissionRepository;
import it.francescofiora.books.repository.RoleRepository;
import it.francescofiora.books.repository.UserRepository;
import it.francescofiora.books.service.dto.NewRoleDto;
import it.francescofiora.books.service.dto.PermissionDto;
import it.francescofiora.books.service.dto.RoleDto;
import it.francescofiora.books.service.impl.RoleServiceImpl;
import it.francescofiora.books.service.mapper.PermissionMapper;
import it.francescofiora.books.service.mapper.RoleMapper;
import it.francescofiora.books.web.errors.NotFoundAlertException;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class RoleServiceTest {

  private static final Long ID = 1L;

  @MockBean
  private PermissionMapper permissionMapper;

  @MockBean
  private RoleMapper roleMapper;

  @MockBean
  private UserRepository userRepository;

  @MockBean
  private PermissionRepository permissionRepository;

  @MockBean
  private RoleRepository roleRepository;

  private RoleService roleService;

  @BeforeEach
  void setUp() {
    roleService = new RoleServiceImpl(permissionMapper, roleMapper, userRepository,
        permissionRepository, roleRepository);
  }

  @Test
  void testFindPermissions() throws Exception {
    var permission = new Permission();
    when(permissionRepository.findAll(any(Pageable.class)))
        .thenReturn(new PageImpl<Permission>(List.of(permission)));
    var expected = new PermissionDto();
    when(permissionMapper.toDto(any(Permission.class))).thenReturn(expected);
    var pageable = PageRequest.of(1, 1);
    var page = roleService.findPermissions(pageable);
    assertThat(page.getContent().get(0)).isEqualTo(expected);
  }

  @Test
  void testCreateRole() throws Exception {
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
  void testUpdateNotFound() throws Exception {
    var roleDto = new RoleDto();
    assertThrows(NotFoundAlertException.class, () -> roleService.updateRole(roleDto));
  }

  @Test
  void testUpdate() throws Exception {
    var role = new Role();
    when(roleRepository.findById(ID)).thenReturn(Optional.of(role));

    var roleDto = new RoleDto();
    roleDto.setId(ID);
    roleService.updateRole(roleDto);
    verify(roleMapper).updateEntityFromDto(roleDto, role);
    verify(roleRepository).save(role);
  }

  @Test
  void testFindAll() throws Exception {
    var role = new Role();
    when(roleRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<Role>(List.of(role)));
    var expected = new RoleDto();
    when(roleMapper.toDto(any(Role.class))).thenReturn(expected);
    var pageable = PageRequest.of(1, 1);
    var page = roleService.findRoles(pageable);
    assertThat(page.getContent().get(0)).isEqualTo(expected);
  }

  @Test
  void testFindOneNotFound() throws Exception {
    var roleOpt = roleService.findOneRole(ID);
    assertThat(roleOpt).isNotPresent();
  }

  @Test
  void testFindOne() throws Exception {
    var role = new Role();
    role.setId(ID);
    when(roleRepository.findById(role.getId())).thenReturn(Optional.of(role));
    var expected = new RoleDto();
    when(roleMapper.toDto(any(Role.class))).thenReturn(expected);

    var roleOpt = roleService.findOneRole(ID);
    assertThat(roleOpt).isPresent();
    var actual = roleOpt.get();
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void testDelete() throws Exception {
    roleService.deleteRole(ID);
    verify(roleRepository).deleteById(ID);
  }
}
