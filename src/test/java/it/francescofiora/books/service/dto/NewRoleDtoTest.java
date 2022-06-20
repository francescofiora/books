package it.francescofiora.books.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import it.francescofiora.books.util.TestUtils;
import it.francescofiora.books.util.UserUtils;
import java.util.List;
import org.junit.jupiter.api.Test;

class NewRoleDtoTest {

  @Test
  void dtoEqualsVerifier() throws Exception {
    var roleDto1 =
        UserUtils.createNewRoleDto(UserUtils.ROLE_BOOK_ADMIN, UserUtils.ROLE_BOOK_ADMIN_DESCR);
    var roleDto2 = new NewRoleDto();
    assertThat(roleDto1).isNotEqualTo(roleDto2);

    roleDto2 =
        UserUtils.createNewRoleDto(UserUtils.ROLE_BOOK_ADMIN, UserUtils.ROLE_BOOK_ADMIN_DESCR);
    TestUtils.checkEqualHashAndToString(roleDto1, roleDto2);

    roleDto2.setName("NameDiff");
    TestUtils.checkNotEqualHashAndToString(roleDto1, roleDto2);

    roleDto2 =
        UserUtils.createNewRoleDto(UserUtils.ROLE_BOOK_ADMIN, UserUtils.ROLE_BOOK_ADMIN_DESCR);
    roleDto2.setDescription("DescriptionDiff");
    TestUtils.checkNotEqualHashAndToString(roleDto1, roleDto2);

    roleDto2 =
        UserUtils.createNewRoleDto(UserUtils.ROLE_BOOK_ADMIN, UserUtils.ROLE_BOOK_ADMIN_DESCR);
    roleDto2.setPermissions(List.of(new RefPermissionDto()));
    TestUtils.checkNotEqualHashAndToString(roleDto1, roleDto2);
  }
}
