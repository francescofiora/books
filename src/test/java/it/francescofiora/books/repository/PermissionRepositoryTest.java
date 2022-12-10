package it.francescofiora.books.repository;

import static org.assertj.core.api.Assertions.assertThat;

import it.francescofiora.books.util.UserUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

class PermissionRepositoryTest extends AbstractTestRepository {

  @Autowired
  private PermissionRepository permissionRepository;

  @Test
  void testCrud() {
    var expected1 =
        UserUtils.createPermission(UserUtils.OP_USER_UPDATE, UserUtils.OP_USER_UPDATE_DESCR);
    var expected2 =
        UserUtils.createPermission(UserUtils.OP_USER_READ, UserUtils.OP_USER_READ_DESCR);
    permissionRepository.save(expected1);
    permissionRepository.save(expected2);

    var permissions = permissionRepository.findAll(PageRequest.of(0, 10));
    assertThat(permissions).isNotNull().isNotEmpty();

    for (var actual : permissions) {
      assertThat(actual).isNotNull();
      assertThat(UserUtils.dataEquals(expected1, actual) || UserUtils.dataEquals(expected2, actual))
          .isTrue();
    }

    var expected3 =
        UserUtils.createPermission(UserUtils.OP_USER_UPDATE, UserUtils.OP_USER_UPDATE_DESCR);
    var permission = permissions.getContent().get(0);
    permission.setName(expected3.getName());
    permission.setDescription(expected3.getDescription());
    permissionRepository.save(permission);

    var optional = permissionRepository.findById(permission.getId());
    assertThat(optional).isPresent();
    permission = optional.get();
    assertThat(UserUtils.dataEquals(expected3, permission)).isTrue();

    for (var actual : permissions) {
      permissionRepository.delete(actual);
    }

    permissions = permissionRepository.findAll(PageRequest.of(0, 10));
    assertThat(permissions).isNotNull().isEmpty();
  }
}
