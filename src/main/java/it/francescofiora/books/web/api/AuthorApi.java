package it.francescofiora.books.web.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import it.francescofiora.books.service.AuthorService;
import it.francescofiora.books.service.dto.AuthorDto;
import it.francescofiora.books.service.dto.NewAuthorDto;
import it.francescofiora.books.service.dto.TitleDto;
import it.francescofiora.books.web.errors.BadRequestAlertException;
import it.francescofiora.books.web.util.HeaderUtil;
import it.francescofiora.books.web.util.PaginationUtil;
import it.francescofiora.books.web.util.ResponseUtil;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@Tag(name = "author", description = "Author Rest API")
@RequestMapping("/api")
public class AuthorApi {

  private static final String ENTITY_NAME = "Author";

  private final Logger log = LoggerFactory.getLogger(this.getClass());

  private final AuthorService authorService;

  public AuthorApi(AuthorService authorService) {
    this.authorService = authorService;
  }

  /**
   * {@code POST  /authors} : Create a new author.
   *
   * @param authorDto the authorDto to create.
   * @return the {@link ResponseEntity} with status {@code 201 (Created)}.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @Operation(
      summary = "add new Author", description = "add a new Author to the system",
      tags = { "author" })
  @ApiResponses(
      value = { @ApiResponse(responseCode = "201", description = "Author created"),
          @ApiResponse(responseCode = "400", description = "invalid input, object invalid"),
          @ApiResponse(responseCode = "409", description = "an existing Author already exists") })
  @PostMapping("/authors")
  public ResponseEntity<Void> createAuthor(
      @Parameter(description = "add new Author") @Valid @RequestBody NewAuthorDto authorDto)
      throws URISyntaxException {
    log.debug("REST request to create Author : {}", authorDto);
    AuthorDto result = authorService.create(authorDto);
    return ResponseEntity.created(new URI("/api/authors/" + result.getId()))
        .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
        .build();
  }

  /**
   * {@code PUT  /authors} : Updates an existing author.
   *
   * @param authorDto the authorDto to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)}, or with
   *         status {@code 400 (Bad Request)} if the authorDto is not valid, or
   *         with status {@code 500 (Internal Server Error)} if the authorDto
   *         couldn't be updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @Operation(
      summary = "update Author", description = "update an Author to the system",
      tags = { "author" })
  @ApiResponses(
      value = { @ApiResponse(responseCode = "200", description = "Author updated"),
          @ApiResponse(responseCode = "400", description = "invalid input, object invalid"),
          @ApiResponse(responseCode = "404", description = "not found") })
  @PutMapping("/authors")
  public ResponseEntity<Void> updateAuthor(
      @Parameter(description = "author to update") @Valid @RequestBody AuthorDto authorDto)
      throws URISyntaxException {
    log.debug("REST request to update Author : {}", authorDto);
    if (authorDto.getId() == null) {
      throw new BadRequestAlertException(ENTITY_NAME, "idnull", "Invalid id");
    }
    authorService.update(authorDto);
    return ResponseEntity.ok()
        .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, authorDto.getId().toString()))
        .build();
  }

  /**
   * {@code GET  /authors} : get all the authors.
   *
   * @param pageable the pagination information.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
   *         of authors in body.
   */
  @Operation(
      summary = "searches authors", description = "By passing in the appropriate options, "
          + "you can search for available authors in the system",
      tags = { "author" })
  @ApiResponses(
      value = {
          @ApiResponse(
              responseCode = "200", description = "search results matching criteria",
              content = @Content(
                  array = @ArraySchema(schema = @Schema(implementation = AuthorDto.class)))),
          @ApiResponse(responseCode = "400", description = "bad input parameter") })
  @GetMapping("/authors")
  public ResponseEntity<List<AuthorDto>> getAllAuthors(Pageable pageable) {
    log.debug("REST request to get a page of Authors");
    Page<AuthorDto> page = authorService.findAll(pageable);
    HttpHeaders headers = PaginationUtil
        .generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
    return ResponseEntity.ok().headers(headers).body(page.getContent());
  }

  /**
   * {@code GET  /authors/:id} : get the "id" author.
   *
   * @param id the id of the authorDto to retrieve.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
   *         the authorDto, or with status {@code 404 (Not Found)}.
   */
  @Operation(
      summary = "searches author by 'id'", description = "searches author by 'id'",
      tags = { "author" })
  @ApiResponses(
      value = {
          @ApiResponse(
              responseCode = "200", description = "search results matching criteria",
              content = @Content(schema = @Schema(implementation = AuthorDto.class))),
          @ApiResponse(responseCode = "400", description = "bad input parameter"),
          @ApiResponse(responseCode = "404", description = "not found") })
  @GetMapping("/authors/{id}")
  public ResponseEntity<AuthorDto> getAuthor(@Parameter(
      description = "id of the author to get", required = true,
      example = "1") @PathVariable("id") Long id) {
    log.debug("REST request to get Author : {}", id);
    Optional<AuthorDto> authorDto = authorService.findOne(id);
    return ResponseUtil.wrapOrNotFound(ENTITY_NAME, authorDto);
  }

  /**
   * {@code GET  /authors/:id/titles} : get titles the "id" author.
   *
   * @param id the id of the author of the TitleDto to retrieve.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
   *         the list of the titleDto of author, or with status
   *         {@code 404 (Not Found)}.
   */
  @Operation(
      summary = "searches titles of the by author 'id'",
      description = "searches titles by author 'id'", tags = { "author" })
  @ApiResponses(
      value = {
          @ApiResponse(
              responseCode = "200", description = "search results matching criteria",
              content = @Content(
                  array = @ArraySchema(schema = @Schema(implementation = TitleDto.class)))),
          @ApiResponse(responseCode = "400", description = "bad input parameter"),
          @ApiResponse(responseCode = "404", description = "not found") })
  @GetMapping("/authors/{id}/titles")
  public ResponseEntity<List<TitleDto>> getTitlesByAuthor(Pageable pageable, @Parameter(
      description = "id of the author", required = true,
      example = "1") @PathVariable("id") Long id) {
    log.debug("REST request to get Titles of Author : {}", id);
    Page<TitleDto> page = authorService.findTitlesByAuthorId(pageable, id);
    HttpHeaders headers = PaginationUtil
        .generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
    return ResponseEntity.ok().headers(headers).body(page.getContent());
  }

  /**
   * {@code DELETE  /authors/:id} : delete the "id" author.
   *
   * @param id the id of the authorDto to delete.
   * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
   */
  @Operation(
      summary = "delete author by 'id'", description = "delete an author by 'id'",
      tags = { "author" })
  @ApiResponses(
      value = { @ApiResponse(responseCode = "204", description = "Author deleted"),
          @ApiResponse(responseCode = "400", description = "bad input parameter") })
  @DeleteMapping("/authors/{id}")
  public ResponseEntity<Void> deleteAuthor(@Parameter(
      description = "id of the author to delete", required = true,
      example = "1") @PathVariable Long id) {
    log.debug("REST request to delete Author : {}", id);
    authorService.delete(id);
    return ResponseEntity.noContent()
        .headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
  }
}
