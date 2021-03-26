package it.francescofiora.books.repository;

import it.francescofiora.books.domain.Title;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data repository for the Title entity.
 */
@Repository
public interface TitleRepository extends JpaRepository<Title, Long> {

  @Query("select title from Title title where title.publisher.id =:id")
  Optional<Title> findOneWithPublisherRelationships(@Param("id") Long id);
}
