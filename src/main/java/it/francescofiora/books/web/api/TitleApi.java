package it.francescofiora.books.web.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.francescofiora.books.service.TitleService;
import it.francescofiora.books.service.dto.NewTitleDto;
import it.francescofiora.books.service.dto.TitleDto;
import it.francescofiora.books.service.dto.UpdatebleTitleDto;
import it.francescofiora.books.web.errors.BadRequestAlertException;
import java.net.URISyntaxException;
import java.util.List;
import javax.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Title Api RestController.
 */
@RestController
@Tag(name = "title", description = "Title Rest API")
@RequestMapping("/books/api/v1")
public class TitleApi extends AbstractApi {

  private static final String ENTITY_NAME = "TitleDto";

  private final TitleService titleService;

  public TitleApi(TitleService titleService) {
    super(ENTITY_NAME);
    this.titleService = titleService;
  }

  /**
   * {@code POST  /titles} : Create a new title.
   *
   * @param titleDto the title to create
   * @return the {@link ResponseEntity} with status {@code 201 (Created)}
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @Operation(summary = "Add new Title", description = "Add a new Title to the system",
      tags = {"title"})
  @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Title created"),
      @ApiResponse(responseCode = "400", description = "Invalid input, object invalid"),
      @ApiResponse(responseCode = "409", description = "An existing Title already exists")})
  @PostMapping("/titles")
  public ResponseEntity<Void> createTitle(
      @Parameter(description = "Add new Title") @Valid @RequestBody NewTitleDto titleDto)
      throws URISyntaxException {
    var result = titleService.create(titleDto);
    return postResponse("/books/api/v1/titles/" + result.getId(), result.getId());
  }

  /**
   * {@code PUT  /titles:id} : Updates an existing title.
   *
   * @param titleDto the title to update
   * @param id the id of the publisher to update
   * @return the {@link ResponseEntity} with status {@code 200 (OK)}, or with status
   *         {@code 400 (Bad Request)} if the title is not valid, or with status
   *         {@code 500 (Internal Server Error)} if the title couldn't be updated.
   */
  @Operation(summary = "Update Title", description = "Update an Title to the system",
      tags = {"title"})
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Title updated"),
      @ApiResponse(responseCode = "400", description = "Invalid input, object invalid"),
      @ApiResponse(responseCode = "404", description = "Not found")})
  @PutMapping("/titles/{id}")
  public ResponseEntity<Void> updateTitle(
      @Parameter(description = "Title to update") @Valid @RequestBody UpdatebleTitleDto titleDto,
      @Parameter(description = "The id of the title to update", required = true,
          example = "1") @PathVariable("id") Long id) {
    if (!id.equals(titleDto.getId())) {
      throw new BadRequestAlertException(ENTITY_NAME, String.valueOf(titleDto.getId()),
          "Invalid id");
    }
    titleService.update(titleDto);
    return putResponse(titleDto.getId());
  }

  /**
   * {@code GET  /titles} : get all the titles.
   *
   * @param pageable the pagination information
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of titles in body
   */
  @Operation(summary = "Searches titles",
      description = "By passing in the appropriate options, "
          + "you can search for available titles in the system",
      tags = {"title"})
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Search results matching criteria",
          content = @Content(
              array = @ArraySchema(schema = @Schema(implementation = TitleDto.class)))),
      @ApiResponse(responseCode = "400", description = "Bad input parameter")})
  @GetMapping("/titles")
  public ResponseEntity<List<TitleDto>> getAllTitles(Pageable pageable) {
    return getResponse(titleService.findAll(pageable));
  }

  /**
   * {@code GET  /titles/:id} : get the "id" title.
   *
   * @param id the id of the title to retrieve
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the title, or
   *         with status {@code 404 (Not Found)}
   */
  @Operation(summary = "Searches title by 'id'", description = "Searches title by 'id'",
      tags = {"title"})
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Search results matching criteria",
          content = @Content(schema = @Schema(implementation = TitleDto.class))),
      @ApiResponse(responseCode = "400", description = "Bad input parameter"),
      @ApiResponse(responseCode = "404", description = "Not found")})
  @GetMapping("/titles/{id}")
  public ResponseEntity<TitleDto> getTitle(@PathVariable Long id) {
    return getResponse(titleService.findOne(id), id);
  }

  /**
   * {@code DELETE  /titles/:id} : delete the "id" title.
   *
   * @param id the id of the title to delete
   * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}
   */
  @Operation(summary = "Delete title by 'id'", description = "Delete an title by 'id'",
      tags = {"title"})
  @ApiResponses(value = {@ApiResponse(responseCode = "204", description = "Title deleted"),
      @ApiResponse(responseCode = "400", description = "Bad input parameter")})
  @DeleteMapping("/titles/{id}")
  public ResponseEntity<Void> deleteTitle(@PathVariable Long id) {
    titleService.delete(id);
    return deleteResponse(id);
  }
}
