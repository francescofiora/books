package it.francescofiora.books.repository;

import static org.assertj.core.api.Assertions.assertThat;

import it.francescofiora.books.domain.User;
import it.francescofiora.books.util.UserUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

class UserRepositoryTest extends AbstractTestRepository {

  @Autowired
  private UserRepository userRepository;

  private void create(User user) {
    for (var role : user.getRoles()) {
      role.getPermissions().forEach(permission -> getEntityManager().persist(permission));
      getEntityManager().persist(role);
    }
    userRepository.save(user);
  }

  @Test
  void testCrud() {
    var expected1 = UserUtils.createUserRoleAdmin();
    create(expected1);

    var expected2 = UserUtils.createUserAdmin();
    create(expected2);

    var users = userRepository.findAll(PageRequest.of(0, 10));
    assertThat(users).isNotNull().isNotNull();
    for (var actual : users) {
      assertThat(actual).isNotNull();
      assertThat(UserUtils.dataEquals(expected1, actual) || UserUtils.dataEquals(expected2, actual))
          .isTrue();
    }

    var expected3 = UserUtils.createUserBookAdmin();
    var user = users.getContent().get(0);
    user.setUsername(expected3.getUsername());
    user.setPassword(expected3.getPassword());
    userRepository.save(user);

    var optional = userRepository.findById(user.getId());
    assertThat(optional).isPresent();
    user = optional.get();
    assertThat(UserUtils.dataEquals(expected3, user)).isTrue();

    for (var actual : users) {
      userRepository.delete(actual);
    }

    users = userRepository.findAll(PageRequest.of(0, 10));
    assertThat(users).isNotNull().isEmpty();
  }

  @Test
  void testFindBy() throws Exception {
    var expected = UserUtils.createUserRoleAdmin();
    create(expected);

    var optional = userRepository.findByUsername(expected.getUsername());
    assertThat(optional).isPresent();
    var user = optional.get();
    assertThat(UserUtils.dataEquals(expected, user)).isTrue();

    optional =
        userRepository.findOneWithRolesRelationships(expected.getRoles().iterator().next().getId());
    assertThat(optional).isPresent();
    user = optional.get();
    assertThat(UserUtils.dataEquals(expected, user)).isTrue();
  }
}
