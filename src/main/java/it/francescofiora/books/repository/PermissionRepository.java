package it.francescofiora.books.repository;

import it.francescofiora.books.domain.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data repository for the Permission entity.
 */
@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {

}
