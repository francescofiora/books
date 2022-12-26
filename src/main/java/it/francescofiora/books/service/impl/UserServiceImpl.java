package it.francescofiora.books.service.impl;

import it.francescofiora.books.domain.User;
import it.francescofiora.books.repository.RoleRepository;
import it.francescofiora.books.repository.UserRepository;
import it.francescofiora.books.service.RoleService;
import it.francescofiora.books.service.UserService;
import it.francescofiora.books.service.dto.NewUserDto;
import it.francescofiora.books.service.dto.RefRoleDto;
import it.francescofiora.books.service.dto.UserDto;
import it.francescofiora.books.service.mapper.UserMapper;
import it.francescofiora.books.web.errors.NotFoundAlertException;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.GenericPropertyMatcher;
import org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * User Service Impl.
 */
@Slf4j
@Service
@Transactional
@AllArgsConstructor
public class UserServiceImpl implements UserService {

  private static final GenericPropertyMatcher PROPERTY_MATCHER_DEFAULT =
      GenericPropertyMatchers.contains().ignoreCase();

  private final UserMapper userMapper;
  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final BCryptPasswordEncoder passwordEncoder;

  private void validateRefRoleDto(List<RefRoleDto> refRoleDtoList) {
    for (var roleDto : refRoleDtoList) {
      if (!roleRepository.findById(roleDto.getId()).isPresent()) {
        final var id = String.valueOf(roleDto.getId());
        throw new NotFoundAlertException(RoleService.ENTITY_NAME, id, String
            .format(NotFoundAlertException.MSG_NOT_FOUND_WITH_ID, RoleService.ENTITY_NAME, id));
      }
    }
  }

  @Override
  public UserDto create(NewUserDto userDto) {
    log.debug("Request to create a new User : {}", userDto);
    validateRefRoleDto(userDto.getRoles());
    var user = userMapper.toEntity(userDto);
    user.setPassword(passwordEncoder.encode(userDto.getPassword()));
    user = userRepository.save(user);
    return userMapper.toDto(user);
  }

  @Override
  public void update(UserDto userDto) {
    log.debug("Request to update User : {}", userDto);
    var userOpt = userRepository.findById(userDto.getId());
    if (!userOpt.isPresent()) {
      var id = String.valueOf(userDto.getId());
      throw new NotFoundAlertException(ENTITY_NAME, id,
          String.format(NotFoundAlertException.MSG_NOT_FOUND_WITH_ID, ENTITY_NAME, id));
    }
    validateRefRoleDto(userDto.getRoles());
    var user = userOpt.get();
    userMapper.updateEntityFromDto(userDto, user);
    userRepository.save(user);
  }

  @Override
  public Page<UserDto> findAll(String username, Pageable pageable) {
    log.debug("Request to get all Users");
    var user = new User();
    user.setUsername(username);
    var exampleMatcher =
        ExampleMatcher.matchingAll().withMatcher("username", PROPERTY_MATCHER_DEFAULT);
    var example = Example.of(user, exampleMatcher);
    return userRepository.findAll(example, pageable).map(userMapper::toDto);
  }

  @Override
  public Optional<UserDto> findOne(Long id) {
    log.debug("Request to get User : {}", id);
    return userRepository.findById(id).map(userMapper::toDto);
  }

  @Override
  public void delete(Long id) {
    log.debug("Request to delete User : {}", id);
    userRepository.deleteById(id);
  }
}
