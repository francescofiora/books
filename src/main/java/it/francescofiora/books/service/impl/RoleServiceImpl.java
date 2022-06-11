package it.francescofiora.books.service.impl;

import it.francescofiora.books.repository.PermissionRepository;
import it.francescofiora.books.repository.RoleRepository;
import it.francescofiora.books.repository.UserRepository;
import it.francescofiora.books.service.PublisherService;
import it.francescofiora.books.service.RoleService;
import it.francescofiora.books.service.dto.NewRoleDto;
import it.francescofiora.books.service.dto.PermissionDto;
import it.francescofiora.books.service.dto.RefPermissionDto;
import it.francescofiora.books.service.dto.RefPublisherDto;
import it.francescofiora.books.service.dto.RoleDto;
import it.francescofiora.books.service.mapper.PermissionMapper;
import it.francescofiora.books.service.mapper.RoleMapper;
import it.francescofiora.books.web.errors.BadRequestAlertException;
import it.francescofiora.books.web.errors.NotFoundAlertException;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Role Service Impl.
 */
@Slf4j
@Service
@Transactional
@AllArgsConstructor
public class RoleServiceImpl implements RoleService {

  private static final String PERMISSION_ENTITY_NAME = "PermissionDto";

  private final PermissionMapper permissionMapper;
  private final RoleMapper roleMapper;
  private final UserRepository userRepository;
  private final PermissionRepository permissionRepository;
  private final RoleRepository roleRepository;

  @Override
  @Transactional(readOnly = true)
  public Page<PermissionDto> findPermissions(Pageable pageable) {
    log.debug("Request to get all Titles");
    return permissionRepository.findAll(pageable).map(permissionMapper::toDto);
  }

  private void validateRefPermissionDto(List<RefPermissionDto> refPermDtoList) {
    for (var permissionDto : refPermDtoList) {
      if (!permissionRepository.findById(permissionDto.getId()).isPresent()) {
        final var id = String.valueOf(permissionDto.getId());
        throw new NotFoundAlertException(PERMISSION_ENTITY_NAME, id, String
            .format(NotFoundAlertException.MSG_NOT_FOUND_WITH_ID, PERMISSION_ENTITY_NAME, id));
      }
    }
  }

  @Override
  public RoleDto createRole(NewRoleDto roleDto) {
    log.debug("Request to create a new Role : {}", roleDto);
    validateRefPermissionDto(roleDto.getPermissions());

    var role = roleMapper.toEntity(roleDto);
    role = roleRepository.save(role);
    return roleMapper.toDto(role);
  }

  @Override
  public void updateRole(RoleDto roleDto) {
    log.debug("Request to update Role : {}", roleDto);
    var roleOpt = roleRepository.findById(roleDto.getId());
    if (!roleOpt.isPresent()) {
      var id = String.valueOf(roleDto.getId());
      throw new NotFoundAlertException(ENTITY_NAME, id,
          String.format(NotFoundAlertException.MSG_NOT_FOUND_WITH_ID, ENTITY_NAME, id));
    }
    validateRefPermissionDto(roleDto.getPermissions());

    var role = roleOpt.get();
    roleMapper.updateEntityFromDto(roleDto, role);
    roleRepository.save(role);
  }

  @Override
  public Page<RoleDto> findRoles(Pageable pageable) {
    log.debug("Request to get all Roles");
    return roleRepository.findAll(pageable).map(roleMapper::toDto);
  }

  @Override
  public Optional<RoleDto> findOneRole(Long id) {
    log.debug("Request to get Role : {}", id);
    return roleRepository.findById(id).map(roleMapper::toDto);
  }

  @Override
  public void deleteRole(Long id) {
    log.debug("Request to delete Role : {}", id);
    var users = userRepository.findOneWithRolesRelationships(id);
    if (!users.isEmpty()) {
      throw new BadRequestAlertException(ENTITY_NAME, String.valueOf(id),
          "Almost a User is using this role");
    }
    roleRepository.deleteById(id);
  }
}
