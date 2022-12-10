package it.francescofiora.books.repository;

import static org.assertj.core.api.Assertions.assertThat;

import it.francescofiora.books.util.UserUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

class RoleRepositoryTest extends AbstractTestRepository {

  @Autowired
  private RoleRepository roleRepository;

  @Test
  void testCrud() {
    var expected1 = UserUtils.createRoleUserAdmin();
    expected1.getPermissions().forEach(permission -> getEntityManager().persist(permission));
    roleRepository.save(expected1);

    var expected2 = UserUtils.createRolePermissionAdmin();
    expected2.getPermissions().forEach(permission -> getEntityManager().persist(permission));
    roleRepository.save(expected2);

    var roles = roleRepository.findAll(PageRequest.of(0, 10));
    assertThat(roles).isNotNull().isNotNull();
    for (var actual : roles) {
      assertThat(actual).isNotNull();
      assertThat(UserUtils.dataEquals(expected1, actual) || UserUtils.dataEquals(expected2, actual))
          .isTrue();
    }

    var expected3 = UserUtils.createRoleBookAdmin();
    var role = roles.getContent().get(0);
    role.setName(expected3.getName());
    role.setDescription(expected3.getDescription());
    roleRepository.save(role);

    var optional = roleRepository.findById(role.getId());
    assertThat(optional).isPresent();
    role = optional.get();
    assertThat(UserUtils.dataEquals(expected3, role)).isTrue();

    for (var actual : roles) {
      roleRepository.delete(actual);
    }

    roles = roleRepository.findAll(PageRequest.of(0, 10));
    assertThat(roles).isNotNull().isEmpty();
  }
}
