package it.francescofiora.books.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import it.francescofiora.books.domain.User;
import it.francescofiora.books.repository.RoleRepository;
import it.francescofiora.books.repository.UserRepository;
import it.francescofiora.books.service.dto.NewUserDto;
import it.francescofiora.books.service.dto.RefRoleDto;
import it.francescofiora.books.service.dto.UserDto;
import it.francescofiora.books.service.impl.UserServiceImpl;
import it.francescofiora.books.service.mapper.UserMapper;
import it.francescofiora.books.web.errors.NotFoundAlertException;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

class UserServiceTest {

  private static final Long ID = 1L;
  private static final String SECURE_PWD = "securepwd";

  @Test
  void testCreate() {
    var user = new User();
    var userMapper = mock(UserMapper.class);
    when(userMapper.toEntity(any(NewUserDto.class))).thenReturn(user);

    var userRepository = mock(UserRepository.class);
    when(userRepository.save(any(User.class))).thenReturn(user);

    var expected = new UserDto();
    when(userMapper.toDto(any(User.class))).thenReturn(expected);

    var passwordEncoder = mock(BCryptPasswordEncoder.class);
    var userService = new UserServiceImpl(userMapper, userRepository, mock(RoleRepository.class),
        passwordEncoder);

    var userDto = new NewUserDto();
    userDto.setPassword(SECURE_PWD);
    var actual = userService.create(userDto);
    assertThat(actual).isEqualTo(expected);
    verify(passwordEncoder).encode(SECURE_PWD);
  }

  @Test
  void testCreateNotFound() {
    var userDto = new NewUserDto();
    userDto.getRoles().add(new RefRoleDto());
    var userService = new UserServiceImpl(mock(UserMapper.class), mock(UserRepository.class),
        mock(RoleRepository.class), mock(BCryptPasswordEncoder.class));
    assertThrows(NotFoundAlertException.class, () -> userService.create(userDto));
  }

  @Test
  void testUpdateNotFound() {
    var userDto = new UserDto();
    var userService = new UserServiceImpl(mock(UserMapper.class), mock(UserRepository.class),
        mock(RoleRepository.class), mock(BCryptPasswordEncoder.class));
    assertThrows(NotFoundAlertException.class, () -> userService.update(userDto));
  }

  @Test
  void testUpdateWithRoleNotFound() {
    var user = new User();
    var userRepository = mock(UserRepository.class);
    when(userRepository.findById(ID)).thenReturn(Optional.of(user));

    var userDto = new UserDto();
    userDto.setId(ID);
    userDto.getRoles().add(new RefRoleDto());
    var userService = new UserServiceImpl(mock(UserMapper.class), userRepository,
        mock(RoleRepository.class), mock(BCryptPasswordEncoder.class));
    assertThrows(NotFoundAlertException.class, () -> userService.update(userDto));
  }

  @Test
  void testUpdate() {
    var user = new User();
    var userRepository = mock(UserRepository.class);
    when(userRepository.findById(ID)).thenReturn(Optional.of(user));

    var userMapper = mock(UserMapper.class);
    var userService = new UserServiceImpl(userMapper, userRepository, mock(RoleRepository.class),
        mock(BCryptPasswordEncoder.class));

    var userDto = new UserDto();
    userDto.setId(ID);
    userService.update(userDto);
    verify(userMapper).updateEntityFromDto(userDto, user);
    verify(userRepository).save(user);
  }

  @Test
  void testFindAll() {
    var user = new User();
    var userRepository = mock(UserRepository.class);
    when(userRepository.findAll(ArgumentMatchers.<Example<User>>any(), any(Pageable.class)))
        .thenReturn(new PageImpl<>(List.of(user)));

    var expected = new UserDto();
    var userMapper = mock(UserMapper.class);
    when(userMapper.toDto(any(User.class))).thenReturn(expected);

    var userService = new UserServiceImpl(userMapper, userRepository, mock(RoleRepository.class),
        mock(BCryptPasswordEncoder.class));

    var pageable = PageRequest.of(1, 1);
    var page = userService.findAll(null, pageable);
    assertThat(page.getContent().get(0)).isEqualTo(expected);
  }

  @Test
  void testFindOneNotFound() {
    var userService = new UserServiceImpl(mock(UserMapper.class), mock(UserRepository.class),
        mock(RoleRepository.class), mock(BCryptPasswordEncoder.class));
    var userOpt = userService.findOne(ID);
    assertThat(userOpt).isNotPresent();
  }

  @Test
  void testFindOne() {
    var user = new User();
    user.setId(ID);
    var userRepository = mock(UserRepository.class);
    when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

    var expected = new UserDto();
    var userMapper = mock(UserMapper.class);
    when(userMapper.toDto(any(User.class))).thenReturn(expected);

    var userService = new UserServiceImpl(userMapper, userRepository, mock(RoleRepository.class),
        mock(BCryptPasswordEncoder.class));

    var userOpt = userService.findOne(ID);
    assertThat(userOpt).isPresent();
    var actual = userOpt.get();
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void testDelete() {
    var userRepository = mock(UserRepository.class);
    var userService = new UserServiceImpl(mock(UserMapper.class), userRepository,
        mock(RoleRepository.class), mock(BCryptPasswordEncoder.class));
    userService.delete(ID);
    verify(userRepository).deleteById(ID);
  }
}
