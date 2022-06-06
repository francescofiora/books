package it.francescofiora.books.repository;

import it.francescofiora.books.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data repository for the Role entity.
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

}
