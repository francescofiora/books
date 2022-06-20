package it.francescofiora.books.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import it.francescofiora.books.repository.UserRepository;
import it.francescofiora.books.service.impl.UserDetailsServiceImpl;
import it.francescofiora.books.util.UserUtils;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class UserDetailsServiceTest {

  private static final String USERNAME_NOT_PRESENT = "NOT_PRESENT";

  @MockBean
  private UserRepository userRepository;

  private UserDetailsService userDetailsService;

  @BeforeEach
  void setUp() {
    userDetailsService = new UserDetailsServiceImpl(userRepository);
  }

  @Test
  void testLoadUserByUsername() throws Exception {
    var expected = UserUtils.createUserAdmin();
    when(userRepository.findByUsername(expected.getUsername())).thenReturn(Optional.of(expected));
    var actual = userDetailsService.loadUserByUsername(expected.getUsername());
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void testUsernameNotFoundException() throws Exception {
    when(userRepository.findByUsername(USERNAME_NOT_PRESENT)).thenReturn(Optional.empty());
    assertThrows(UsernameNotFoundException.class,
        () -> userDetailsService.loadUserByUsername(USERNAME_NOT_PRESENT));
  }
}
