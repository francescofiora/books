package it.francescofiora.books.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import it.francescofiora.books.repository.UserRepository;
import it.francescofiora.books.service.impl.UserDetailsServiceImpl;
import it.francescofiora.books.util.UserUtils;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

class UserDetailsServiceTest {

  private static final String USERNAME_NOT_PRESENT = "NOT_PRESENT";

  @Test
  void testLoadUserByUsername() {
    var expected = UserUtils.createUserAdmin();
    var userRepository = mock(UserRepository.class);
    when(userRepository.findByUsername(expected.getUsername())).thenReturn(Optional.of(expected));
    var userDetailsService = new UserDetailsServiceImpl(userRepository);
    var actual = userDetailsService.loadUserByUsername(expected.getUsername());
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void testUsernameNotFoundException() {
    var userRepository = mock(UserRepository.class);
    when(userRepository.findByUsername(USERNAME_NOT_PRESENT)).thenReturn(Optional.empty());
    var userDetailsService = new UserDetailsServiceImpl(userRepository);
    assertThrows(UsernameNotFoundException.class,
        () -> userDetailsService.loadUserByUsername(USERNAME_NOT_PRESENT));
  }
}
