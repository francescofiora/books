package it.francescofiora.books.repository;

import it.francescofiora.books.domain.Publisher;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data repository for the Publisher entity.
 */
@Repository
public interface PublisherRepository extends JpaRepository<Publisher, Long> {
}
