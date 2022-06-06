package it.francescofiora.books.repository;

import it.francescofiora.books.domain.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data repository for the User entity.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByUsername(String username);

  @Query(" select user from User user join user.roles role where role.id =:id")
  Optional<User> findOneWithRolesRelationships(@Param("id") Long id);

}
