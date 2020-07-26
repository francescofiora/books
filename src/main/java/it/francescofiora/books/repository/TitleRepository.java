package it.francescofiora.books.repository;

import it.francescofiora.books.domain.Title;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data repository for the Title entity.
 */
@Repository
public interface TitleRepository extends JpaRepository<Title, Long> {

  @Query(
      value = "select distinct title from Title title left join fetch title.authors",
      countQuery = "select count(distinct title) from Title title")
  Page<Title> findAllWithEagerRelationships(Pageable pageable);

  @Query("select distinct title from Title title left join fetch title.authors")
  List<Title> findAllWithEagerRelationships();

  @Query("select title from Title title left join fetch title.authors where title.id =:id")
  Optional<Title> findOneWithEagerRelationships(@Param("id") Long id);
}
