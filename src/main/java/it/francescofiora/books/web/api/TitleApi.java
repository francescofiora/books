package it.francescofiora.books.web.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.francescofiora.books.service.TitleService;
import it.francescofiora.books.service.dto.NewTitleDto;
import it.francescofiora.books.service.dto.TitleDto;
import it.francescofiora.books.service.dto.UpdatebleTitleDto;
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
 * Title Api RestController.
 */
@RestController
@Tag(name = "title", description = "Title Rest API")
@RequestMapping("/api/v1")
@SecurityRequirement(name = "Bearer Authentication")
public class TitleApi extends AbstractApi {

  private static final String ENTITY_NAME = "TitleDto";
  private static final String TAG = "title";

  private final TitleService titleService;

  public TitleApi(TitleService titleService) {
    super(ENTITY_NAME);
    this.titleService = titleService;
  }

  /**
   * {@code POST  /titles} : Create a new title.
   *
   * @param titleDto the title to create
   * @return the {@link ResponseEntity}
   */
  @Operation(summary = "Add new Title", description = "Add a new Title to the system", tags = {TAG})
  @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Title created"),
      @ApiResponse(responseCode = "400", description = "Invalid input, object invalid")})
  @PostMapping("/titles")
  @PreAuthorize(AUTHORIZE_BOOK_UPDATE)
  public ResponseEntity<Void> createTitle(
      @Parameter(description = "Add new Title") @Valid @RequestBody NewTitleDto titleDto) {
    var result = titleService.create(titleDto);
    return postResponse("/api/v1/titles/" + result.getId(), result.getId());
  }

  /**
   * {@code PUT  /titles:id} : Updates an existing title.
   *
   * @param titleDto the title to update
   * @param id the id of the title to update
   * @return the {@link ResponseEntity}
   */
  @Operation(summary = "Update Title", description = "Update an Title to the system", tags = {TAG})
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Title updated"),
      @ApiResponse(responseCode = "400", description = "Invalid input, object invalid"),
      @ApiResponse(responseCode = "404", description = "Not found")})
  @PutMapping("/titles/{id}")
  @PreAuthorize(AUTHORIZE_BOOK_UPDATE)
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
   * @param name the name
   * @param pageable the pagination information
   * @return the {@link ResponseEntity} with the list of titles
   */
  @Operation(summary = "Searches titles",
      description = "By passing in the appropriate options, "
          + "you can search for available titles in the system",
      tags = {TAG})
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Search results matching criteria",
          content = @Content(
              array = @ArraySchema(schema = @Schema(implementation = TitleDto.class)))),
      @ApiResponse(responseCode = "400", description = "Bad input parameter")})
  @GetMapping("/titles")
  @PreAuthorize(AUTHORIZE_BOOK_READ)
  public ResponseEntity<List<TitleDto>> getAllTitles(
      @Parameter(description = "Book's Name", example = "My prefer Book",
          in = ParameterIn.QUERY) @RequestParam(required = false) String name,
      @Parameter(example = "{\n  \"page\": 0,  \"size\": 10}",
          in = ParameterIn.QUERY) Pageable pageable) {
    return getResponse(titleService.findAll(name, pageable));
  }

  /**
   * {@code GET  /titles/:id} : get the "id" title.
   *
   * @param id the id of the title to retrieve
   * @return the {@link ResponseEntity} with the title
   */
  @Operation(summary = "Searches title by 'id'", description = "Searches title by 'id'",
      tags = {TAG})
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Search results matching criteria",
          content = @Content(schema = @Schema(implementation = TitleDto.class))),
      @ApiResponse(responseCode = "400", description = "Bad input parameter"),
      @ApiResponse(responseCode = "404", description = "Not found")})
  @GetMapping("/titles/{id}")
  @PreAuthorize(AUTHORIZE_BOOK_READ)
  public ResponseEntity<TitleDto> getTitle(@PathVariable Long id) {
    return getResponse(titleService.findOne(id), id);
  }

  /**
   * {@code DELETE  /titles/:id} : delete the "id" title.
   *
   * @param id the id of the title to delete
   * @return the {@link ResponseEntity}
   */
  @Operation(summary = "Delete title by 'id'", description = "Delete an title by 'id'",
      tags = {TAG})
  @ApiResponses(value = {@ApiResponse(responseCode = "204", description = "Title deleted"),
      @ApiResponse(responseCode = "400", description = "Bad input parameter")})
  @DeleteMapping("/titles/{id}")
  @PreAuthorize(AUTHORIZE_BOOK_UPDATE)
  public ResponseEntity<Void> deleteTitle(@PathVariable Long id) {
    titleService.delete(id);
    return deleteResponse(id);
  }
}
