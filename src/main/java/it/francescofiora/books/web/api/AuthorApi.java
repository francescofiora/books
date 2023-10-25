package it.francescofiora.books.web.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
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
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Author Api RestController.
 */
@RestController
@Tag(name = "author", description = "Author Rest API")
@RequestMapping("/api/v1")
public class AuthorApi extends AbstractApi {

  private static final String ENTITY_NAME = "AuthorDto";
  private static final String TAG = "author";

  private final AuthorService authorService;

  public AuthorApi(AuthorService authorService) {
    super(ENTITY_NAME);
    this.authorService = authorService;
  }

  /**
   * {@code POST  /authors} : Create a new author.
   *
   * @param authorDto the author to create
   * @return the {@link ResponseEntity}
   */
  @Operation(summary = "Add new Author", description = "Add a new Author to the system",
      tags = {TAG})
  @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Author created"),
      @ApiResponse(responseCode = "400", description = "Invalid input, object invalid")})
  @PostMapping("/authors")
  @PreAuthorize(AUTHORIZE_BOOK_UPDATE)
  public ResponseEntity<Void> createAuthor(
      @Parameter(description = "Add new Author") @Valid @RequestBody NewAuthorDto authorDto) {
    var result = authorService.create(authorDto);
    return postResponse("/api/v1/authors/" + result.getId(), result.getId());
  }

  /**
   * {@code PUT  /authors:id} : Updates an existing author.
   *
   * @param authorDto the author to update
   * @param id the id of the author to update
   * @return the {@link ResponseEntity}
   */
  @Operation(summary = "Update Author", description = "Update an Author to the system",
      tags = {TAG})
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Author updated"),
      @ApiResponse(responseCode = "400", description = "Invalid input, object invalid"),
      @ApiResponse(responseCode = "404", description = "Not found")})
  @PutMapping("/authors/{id}")
  @PreAuthorize(AUTHORIZE_BOOK_UPDATE)
  public ResponseEntity<Void> updateAuthor(
      @Parameter(description = "Author to update") @Valid @RequestBody AuthorDto authorDto,
      @Parameter(description = "The id of the author to update", required = true,
          example = "1") @PathVariable("id") Long id) {
    if (!id.equals(authorDto.getId())) {
      throw new BadRequestAlertException(ENTITY_NAME, String.valueOf(authorDto.getId()),
          "Invalid id");
    }
    authorService.update(authorDto);
    return putResponse(id);
  }

  /**
   * {@code GET  /authors} : get all the authors.
   *
   * @param firstName the first name
   * @param lastName the last name
   * @param pageable the pagination information
   * @return the {@link ResponseEntity} with the list of authors
   */
  @Operation(summary = "Searches authors",
      description = "By passing in the appropriate options, "
          + "you can search for available authors in the system",
      tags = {TAG})
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Search results matching criteria",
          content = @Content(
              array = @ArraySchema(schema = @Schema(implementation = AuthorDto.class)))),
      @ApiResponse(responseCode = "400", description = "Bad input parameter")})
  @GetMapping("/authors")
  @PreAuthorize(AUTHORIZE_BOOK_READ)
  public ResponseEntity<List<AuthorDto>> getAllAuthors(
      @Parameter(description = "First Name", example = "John",
          in = ParameterIn.QUERY) @RequestParam(required = false) String firstName,
      @Parameter(description = "Last Name", example = "Smith",
          in = ParameterIn.QUERY) @RequestParam(required = false) String lastName,
      @Parameter(example = "{\n  \"page\": 0,  \"size\": 10}",
          in = ParameterIn.QUERY) Pageable pageable) {
    return getResponse(authorService.findAll(firstName, lastName, pageable));
  }

  /**
   * {@code GET  /authors/:id} : get the "id" author.
   *
   * @param id the id of the author to retrieve
   * @return the {@link ResponseEntity} with the author
   */
  @Operation(summary = "Searches author by 'id'", description = "Searches author by 'id'",
      tags = {TAG})
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Search results matching criteria",
          content = @Content(schema = @Schema(implementation = AuthorDto.class))),
      @ApiResponse(responseCode = "400", description = "Bad input parameter"),
      @ApiResponse(responseCode = "404", description = "Not found")})
  @GetMapping("/authors/{id}")
  @PreAuthorize(AUTHORIZE_BOOK_READ)
  public ResponseEntity<AuthorDto> getAuthor(@Parameter(description = "The id of the author to get",
      required = true, example = "1") @PathVariable("id") Long id) {
    return getResponse(authorService.findOne(id), id);
  }

  /**
   * {@code GET  /authors/:id/titles} : get titles the "id" author.
   *
   * @param id the id of the author of the titles to retrieve
   * @return the {@link ResponseEntity} with the list of the titles of author
   */
  @Operation(summary = "Searches titles of the by author 'id'",
      description = "Searches titles by author 'id'", tags = {TAG})
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Search results matching criteria",
          content = @Content(
              array = @ArraySchema(schema = @Schema(implementation = TitleDto.class)))),
      @ApiResponse(responseCode = "400", description = "Bad input parameter"),
      @ApiResponse(responseCode = "404", description = "Not found")})
  @GetMapping("/authors/{id}/titles")
  @PreAuthorize(AUTHORIZE_BOOK_READ)
  public ResponseEntity<List<TitleDto>> getTitlesByAuthor(Pageable pageable,
      @Parameter(description = "The id of the author", required = true,
          example = "1") @PathVariable("id") Long id) {
    return getResponse("TitleDto", authorService.findTitlesByAuthorId(pageable, id));
  }

  /**
   * {@code DELETE  /authors/:id} : delete the "id" author.
   *
   * @param id the id of the author to delete
   * @return the {@link ResponseEntity}
   */
  @Operation(summary = "Delete author by 'id'", description = "Delete an author by 'id'",
      tags = {TAG})
  @ApiResponses(value = {@ApiResponse(responseCode = "204", description = "Author deleted"),
      @ApiResponse(responseCode = "400", description = "Bad input parameter")})
  @DeleteMapping("/authors/{id}")
  @PreAuthorize(AUTHORIZE_BOOK_UPDATE)
  public ResponseEntity<Void> deleteAuthor(
      @Parameter(description = "The id of the author to delete", required = true,
          example = "1") @PathVariable Long id) {
    authorService.delete(id);
    return deleteResponse(id);
  }
}
