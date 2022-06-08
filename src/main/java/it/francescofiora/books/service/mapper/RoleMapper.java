package it.francescofiora.books.service.mapper;

import it.francescofiora.books.domain.Role;
import it.francescofiora.books.service.dto.NewRoleDto;
import it.francescofiora.books.service.dto.RefRoleDto;
import it.francescofiora.books.service.dto.RoleDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * Mapper for the entity {@link Role} and its DTO {@link RoleDto}.
 */
@Mapper(componentModel = "spring", uses = {PermissionMapper.class})
public interface RoleMapper {

  RoleDto toDto(Role entity);

  @Mapping(target = "id", ignore = true)
  void updateEntityFromDto(RoleDto roleDto, @MappingTarget Role role);

  @Mapping(target = "id", ignore = true)
  Role toEntity(NewRoleDto roleDto);

  @Mapping(target = "name", ignore = true)
  @Mapping(target = "description", ignore = true)
  @Mapping(target = "permissions", ignore = true)
  Role toEntity(RefRoleDto dto);
}
