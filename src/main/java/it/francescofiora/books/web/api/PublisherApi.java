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
import it.francescofiora.books.service.PublisherService;
import it.francescofiora.books.service.dto.NewPublisherDto;
import it.francescofiora.books.service.dto.PublisherDto;
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
 * Publisher Api RestController.
 */
@RestController
@Tag(name = "publisher", description = "Publisher Rest API")
@RequestMapping("/api/v1")
@SecurityRequirement(name = "Bearer Authentication")
public class PublisherApi extends AbstractApi {

  private static final String ENTITY_NAME = "PublisherDto";
  private static final String TAG = "publisher";

  private final PublisherService publisherService;

  public PublisherApi(PublisherService publisherService) {
    super(ENTITY_NAME);
    this.publisherService = publisherService;
  }

  /**
   * {@code POST  /publishers} : Create a new publisher.
   *
   * @param publisherDto the publisher to create
   * @return the {@link ResponseEntity}
   */
  @Operation(summary = "Add new Publisher", description = "Add a new Publisher to the system",
      tags = {TAG})
  @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Publisher created"),
      @ApiResponse(responseCode = "400", description = "Invalid input, object invalid")})
  @PostMapping("/publishers")
  @PreAuthorize(AUTHORIZE_BOOK_UPDATE)
  public ResponseEntity<Void> createPublisher(@Parameter(
      description = "Add new Publisher") @Valid @RequestBody NewPublisherDto publisherDto) {
    var result = publisherService.create(publisherDto);
    return postResponse("/api/v1/publishers/" + result.getId(), result.getId());
  }

  /**
   * {@code PUT  /publishers:id} : Updates an existing publisher.
   *
   * @param publisherDto the publisher to update
   * @param id the id of the publisher to update
   * @return the {@link ResponseEntity}
   */
  @Operation(summary = "Update Publisher", description = "Update an Publisher to the system",
      tags = {TAG})
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Publisher updated"),
      @ApiResponse(responseCode = "400", description = "Invalid input, object invalid"),
      @ApiResponse(responseCode = "404", description = "Not found")})
  @PutMapping("/publishers/{id}")
  @PreAuthorize(AUTHORIZE_BOOK_UPDATE)
  public ResponseEntity<Void> updatePublisher(
      @Parameter(description = "Publisher to update") @Valid @RequestBody PublisherDto publisherDto,
      @Parameter(description = "The id of the publisher to update", required = true,
          example = "1") @PathVariable("id") Long id) {
    if (!id.equals(publisherDto.getId())) {
      throw new BadRequestAlertException(ENTITY_NAME, String.valueOf(publisherDto.getId()),
          "Invalid id");
    }
    publisherService.update(publisherDto);
    return putResponse(id);
  }

  /**
   * {@code GET  /publishers} : get all the publishers.
   *
   * @param publisherName the publisher name
   * @param pageable the pagination information
   * @return the {@link ResponseEntity} with the list of publishers
   */
  @Operation(summary = "Searches publishers",
      description = "By passing in the appropriate options, "
          + "you can search for available publishers in the system",
      tags = {TAG})
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Search results matching criteria",
          content = @Content(
              array = @ArraySchema(schema = @Schema(implementation = PublisherDto.class)))),
      @ApiResponse(responseCode = "400", description = "Bad input parameter")})
  @GetMapping("/publishers")
  @PreAuthorize(AUTHORIZE_BOOK_READ)
  public ResponseEntity<List<PublisherDto>> getAllPublishers(
      @Parameter(description = "Publisher Name", example = "Publisher Ltd",
          in = ParameterIn.QUERY) @RequestParam(required = false) String publisherName,
      @Parameter(example = "{\n  \"page\": 0,  \"size\": 10}",
          in = ParameterIn.QUERY) Pageable pageable) {
    return getResponse(publisherService.findAll(publisherName, pageable));
  }

  /**
   * {@code GET  /publishers/:id} : get the "id" publisher.
   *
   * @param id the id of the publisher to retrieve
   * @return the {@link ResponseEntity} with the publisher
   */
  @Operation(summary = "Searches publisher by 'id'", description = "Searches publisher by 'id'",
      tags = {TAG})
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Search results matching criteria",
          content = @Content(schema = @Schema(implementation = PublisherDto.class))),
      @ApiResponse(responseCode = "400", description = "Bad input parameter"),
      @ApiResponse(responseCode = "404", description = "not found")})
  @GetMapping("/publishers/{id}")
  @PreAuthorize(AUTHORIZE_BOOK_READ)
  public ResponseEntity<PublisherDto> getPublisher(
      @Parameter(description = "The id of the publisher to get", required = true,
          example = "1") @PathVariable("id") Long id) {
    return getResponse(publisherService.findOne(id), id);
  }

  /**
   * {@code DELETE  /publishers/:id} : delete the "id" publisher.
   *
   * @param id the id of the publisher to delete
   * @return the {@link ResponseEntity}
   */
  @Operation(summary = "Delete publisher by 'id'", description = "Delete an publisher by 'id'",
      tags = {TAG})
  @ApiResponses(value = {@ApiResponse(responseCode = "204", description = "Publisher deleted"),
      @ApiResponse(responseCode = "400", description = "Bad input parameter")})
  @DeleteMapping("/publishers/{id}")
  @PreAuthorize(AUTHORIZE_BOOK_UPDATE)
  public ResponseEntity<Void> deletePublisher(
      @Parameter(description = "The id of the publisher to delete", required = true,
          example = "1") @PathVariable Long id) {
    publisherService.delete(id);
    return deleteResponse(id);
  }
}
