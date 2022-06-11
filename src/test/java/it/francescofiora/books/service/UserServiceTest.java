package it.francescofiora.books.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import it.francescofiora.books.domain.User;
import it.francescofiora.books.repository.RoleRepository;
import it.francescofiora.books.repository.UserRepository;
import it.francescofiora.books.service.dto.NewUserDto;
import it.francescofiora.books.service.dto.UserDto;
import it.francescofiora.books.service.impl.UserServiceImpl;
import it.francescofiora.books.service.mapper.UserMapper;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class UserServiceTest {

  private static final Long ID = 1L;

  @MockBean
  private UserMapper userMapper;

  @MockBean
  private UserRepository userRepository;

  @MockBean
  private RoleRepository roleRepository;

  @MockBean
  private BCryptPasswordEncoder passwordEncoder;

  private UserService userService;

  @BeforeEach
  void setUp() {
    userService = new UserServiceImpl(userMapper, userRepository, roleRepository, passwordEncoder);
  }

  @Test
  void testCreate() throws Exception {
    var user = new User();
    when(userMapper.toEntity(any(NewUserDto.class))).thenReturn(user);
    when(userRepository.save(any(User.class))).thenReturn(user);

    var expected = new UserDto();
    when(userMapper.toDto(any(User.class))).thenReturn(expected);

    var userDto = new NewUserDto();
    var actual = userService.create(userDto);
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void testUpdateNotFound() throws Exception {
    var userDto = new UserDto();
    assertThrows(NotFoundAlertException.class, () -> userService.update(userDto));
  }

  @Test
  void testUpdate() throws Exception {
    var user = new User();
    when(userRepository.findById(ID)).thenReturn(Optional.of(user));

    var userDto = new UserDto();
    userDto.setId(ID);
    userService.update(userDto);
    verify(userMapper).updateEntityFromDto(userDto, user);
    verify(userRepository).save(user);
  }

  @Test
  void testFindAll() throws Exception {
    var user = new User();
    when(userRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<User>(List.of(user)));
    var expected = new UserDto();
    when(userMapper.toDto(any(User.class))).thenReturn(expected);
    var pageable = PageRequest.of(1, 1);
    var page = userService.findAll(pageable);
    assertThat(page.getContent().get(0)).isEqualTo(expected);
  }

  @Test
  void testFindOneNotFound() throws Exception {
    var userOpt = userService.findOne(ID);
    assertThat(userOpt).isNotPresent();
  }

  @Test
  void testFindOne() throws Exception {
    var user = new User();
    user.setId(ID);
    when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
    var expected = new UserDto();
    when(userMapper.toDto(any(User.class))).thenReturn(expected);

    var userOpt = userService.findOne(ID);
    assertThat(userOpt).isPresent();
    var actual = userOpt.get();
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void testDelete() throws Exception {
    userService.delete(ID);
    verify(userRepository).deleteById(ID);
  }
}
