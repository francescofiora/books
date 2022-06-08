package it.francescofiora.books.service.mapper;

import it.francescofiora.books.domain.User;
import it.francescofiora.books.service.dto.NewUserDto;
import it.francescofiora.books.service.dto.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * Mapper for the entity {@link User} and its DTO {@link UserDto}.
 */
@Mapper(componentModel = "spring", uses = {RoleMapper.class})
public interface UserMapper {

  UserDto toDto(User entity);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "password", ignore = true)
  @Mapping(target = "authorities", ignore = true)
  void updateEntityFromDto(UserDto userDto, @MappingTarget User user);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "authorities", ignore = true)
  User toEntity(NewUserDto userDto);
}
