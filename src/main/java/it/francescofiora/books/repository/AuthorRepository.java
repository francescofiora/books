package it.francescofiora.books.repository;

import it.francescofiora.books.domain.Author;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data repository for the Author entity.
 */
@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
}
