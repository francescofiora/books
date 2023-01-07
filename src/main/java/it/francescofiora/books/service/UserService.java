package it.francescofiora.books.service;

import it.francescofiora.books.service.dto.NewUserDto;
import it.francescofiora.books.service.dto.UserDto;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * User Service.
 */
public interface UserService {

  String ENTITY_NAME = "UserDto";

  /**
   * Create a new user.
   *
   * @param userDto the entity to save
   * @return the persisted entity
   */
  UserDto create(NewUserDto userDto);

  /**
   * Update an user.
   *
   * @param userDto the entity to update
   */
  void update(UserDto userDto);

  /**
   * Get all the users.
   *
   * @param username the user name
   * @param pageable the pagination information
   * @return the list of entities
   */
  Page<UserDto> findAll(String username, Pageable pageable);

  /**
   * Get by "id" user.
   *
   * @param id the id of the entity
   * @return the entity
   */
  Optional<UserDto> findOne(Long id);

  /**
   * Delete the "id" user.
   *
   * @param id the id of the entity
   */
  void delete(Long id);
}
