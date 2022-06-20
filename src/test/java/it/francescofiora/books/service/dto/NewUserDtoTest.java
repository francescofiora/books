package it.francescofiora.books.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import it.francescofiora.books.util.TestUtils;
import it.francescofiora.books.util.UserUtils;
import java.util.List;
import org.junit.jupiter.api.Test;

class NewUserDtoTest {

  @Test
  void dtoEqualsVerifier() throws Exception {
    var userDto1 = UserUtils.createNewUserDto(UserUtils.USER);
    var userDto2 = new NewUserDto();
    assertThat(userDto1).isNotEqualTo(userDto2);

    userDto2 = UserUtils.createNewUserDto(UserUtils.USER);
    TestUtils.checkEqualHashAndToString(userDto1, userDto2);

    userDto2.setUsername("userDiff");
    TestUtils.checkNotEqualHashAndToString(userDto1, userDto2);

    userDto2 = UserUtils.createNewUserDto(UserUtils.USER);
    userDto2.setPassword("passwordDiff");
    TestUtils.checkNotEqualHashAndToString(userDto1, userDto2);

    userDto2 = UserUtils.createNewUserDto(UserUtils.USER);
    userDto2.setAccountNonExpired(false);
    TestUtils.checkNotEqualHashAndToString(userDto1, userDto2);

    userDto2 = UserUtils.createNewUserDto(UserUtils.USER);
    userDto2.setAccountNonLocked(false);
    TestUtils.checkNotEqualHashAndToString(userDto1, userDto2);

    userDto2 = UserUtils.createNewUserDto(UserUtils.USER);
    userDto2.setCredentialsNonExpired(false);
    TestUtils.checkNotEqualHashAndToString(userDto1, userDto2);

    userDto2 = UserUtils.createNewUserDto(UserUtils.USER);
    userDto2.setEnabled(false);
    TestUtils.checkNotEqualHashAndToString(userDto1, userDto2);

    userDto2 = UserUtils.createNewUserDto(UserUtils.USER);
    userDto2.setRoles(List.of(new RefRoleDto()));
    TestUtils.checkNotEqualHashAndToString(userDto1, userDto2);
  }
}
