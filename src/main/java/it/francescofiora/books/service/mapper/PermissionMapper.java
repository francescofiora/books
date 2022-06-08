package it.francescofiora.books.service.mapper;

import it.francescofiora.books.domain.Permission;
import it.francescofiora.books.service.dto.PermissionDto;
import it.francescofiora.books.service.dto.RefPermissionDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity {@link Permission} and its DTO {@link PermissionDto}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PermissionMapper {

  PermissionDto toDto(Permission entity);

  @Mapping(target = "name", ignore = true)
  @Mapping(target = "description", ignore = true)
  Permission toEntity(RefPermissionDto dto);
}
