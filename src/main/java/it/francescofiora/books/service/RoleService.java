package it.francescofiora.books.service;

import it.francescofiora.books.service.dto.NewRoleDto;
import it.francescofiora.books.service.dto.PermissionDto;
import it.francescofiora.books.service.dto.RoleDto;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Role Service.
 */
public interface RoleService {

  String ENTITY_NAME = "RoleDto";

  /**
   * Get all the permissions.
   *
   * @param pageable the pagination information.
   * @return the list of entities.
   */
  Page<PermissionDto> findPermissions(Pageable pageable);

  /**
   * Create a new role.
   *
   * @param roleDto the entity to save.
   * @return the persisted entity.
   */
  RoleDto createRole(NewRoleDto roleDto);

  /**
   * Update an role.
   *
   * @param roleDto the entity to update.
   */
  void updateRole(RoleDto roleDto);

  /**
   * Get all the roles.
   *
   * @param pageable the pagination information.
   * @return the list of entities.
   */
  Page<RoleDto> findRoles(Pageable pageable);

  /**
   * Get by "id" role.
   *
   * @param id the id of the entity.
   * @return the entity.
   */
  Optional<RoleDto> findOneRole(Long id);

  /**
   * Delete the "id" role.
   *
   * @param id the id of the entity.
   */
  void deleteRole(Long id);

}
