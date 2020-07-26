package it.francescofiora.books.web.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import it.francescofiora.books.service.PublisherService;
import it.francescofiora.books.service.dto.NewPublisherDto;
import it.francescofiora.books.service.dto.PublisherDto;
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
@Tag(name = "publisher", description = "Publisher Rest API")
@RequestMapping("/api")
public class PublisherApi {

  private static final String ENTITY_NAME = "Publisher";

  private final Logger log = LoggerFactory.getLogger(this.getClass());

  private final PublisherService publisherService;

  public PublisherApi(PublisherService publisherService) {
    this.publisherService = publisherService;
  }

  /**
   * {@code POST  /publishers} : Create a new publisher.
   *
   * @param publisherDto the publisherDto to create.
   * @return the {@link ResponseEntity} with status {@code 201 (Created)}.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @Operation(
      summary = "add new Publisher", description = "add a new Publisher to the system",
      tags = { "publisher" })
  @ApiResponses(
      value = { @ApiResponse(responseCode = "201", description = "Publisher created"),
          @ApiResponse(responseCode = "400", description = "invalid input, object invalid"),
          @ApiResponse(
              responseCode = "409", description = "an existing Publisher already exists") })
  @PostMapping("/publishers")
  public ResponseEntity<Void> createPublisher(
      @Parameter(
          description = "add new Publisher") @Valid @RequestBody NewPublisherDto publisherDto)
      throws URISyntaxException {
    log.debug("REST request to create Publisher : {}", publisherDto);
    PublisherDto result = publisherService.create(publisherDto);
    return ResponseEntity.created(new URI("/api/publishers/" + result.getId()))
        .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
        .build();
  }

  /**
   * {@code PUT  /publishers} : Updates an existing publisher.
   *
   * @param publisherDto the publisherDto to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)}, or with
   *         status {@code 400 (Bad Request)} if the publisherDto is not valid, or
   *         with status {@code 500 (Internal Server Error)} if the publisherDto
   *         couldn't be updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @Operation(
      summary = "update Publisher", description = "update an Publisher to the system",
      tags = { "publisher" })
  @ApiResponses(
      value = { @ApiResponse(responseCode = "200", description = "Publisher updated"),
          @ApiResponse(responseCode = "400", description = "invalid input, object invalid"),
          @ApiResponse(responseCode = "404", description = "not found") })
  @PutMapping("/publishers")
  public ResponseEntity<Void> updatePublisher(
      @Parameter(description = "publisher to update") @Valid @RequestBody PublisherDto publisherDto)
      throws URISyntaxException {
    log.debug("REST request to update Publisher : {}", publisherDto);
    if (publisherDto.getId() == null) {
      throw new BadRequestAlertException(ENTITY_NAME, "idnull", "Invalid id");
    }
    publisherService.update(publisherDto);
    return ResponseEntity.ok()
        .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, publisherDto.getId().toString()))
        .build();
  }

  /**
   * {@code GET  /publishers} : get all the publishers.
   *
   * @param pageable the pagination information.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
   *         of publishers in body.
   */
  @Operation(
      summary = "searches publishers", description = "By passing in the appropriate options, "
          + "you can search for available publishers in the system",
      tags = { "publisher" })
  @ApiResponses(
      value = {
          @ApiResponse(
              responseCode = "200", description = "search results matching criteria",
              content = @Content(
                  array = @ArraySchema(schema = @Schema(implementation = PublisherDto.class)))),
          @ApiResponse(responseCode = "400", description = "bad input parameter") })
  @GetMapping("/publishers")
  public ResponseEntity<List<PublisherDto>> getAllPublishers(Pageable pageable) {
    log.debug("REST request to get a page of Publishers");
    Page<PublisherDto> page = publisherService.findAll(pageable);
    HttpHeaders headers = PaginationUtil
        .generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
    return ResponseEntity.ok().headers(headers).body(page.getContent());
  }

  /**
   * {@code GET  /publishers/:id} : get the "id" publisher.
   *
   * @param id the id of the publisherDto to retrieve.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
   *         the publisherDto, or with status {@code 404 (Not Found)}.
   */
  @Operation(
      summary = "searches publisher by 'id'", description = "searches publisher by 'id'",
      tags = { "publisher" })
  @ApiResponses(
      value = {
          @ApiResponse(
              responseCode = "200", description = "search results matching criteria",
              content = @Content(schema = @Schema(implementation = PublisherDto.class))),
          @ApiResponse(responseCode = "400", description = "bad input parameter"),
          @ApiResponse(responseCode = "404", description = "not found") })
  @GetMapping("/publishers/{id}")
  public ResponseEntity<PublisherDto> getPublisher(@Parameter(
      description = "id of the publisher to get", required = true,
      example = "1") @PathVariable("id") Long id) {
    log.debug("REST request to get Publisher : {}", id);
    Optional<PublisherDto> publisherDto = publisherService.findOne(id);
    return ResponseUtil.wrapOrNotFound(ENTITY_NAME, publisherDto);
  }

  /**
   * {@code DELETE  /publishers/:id} : delete the "id" publisher.
   *
   * @param id the id of the publisherDto to delete.
   * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
   */
  @Operation(
      summary = "delete publisher by 'id'", description = "delete an publisher by 'id'",
      tags = { "publisher" })
  @ApiResponses(
      value = { @ApiResponse(responseCode = "204", description = "Publisher deleted"),
          @ApiResponse(responseCode = "400", description = "bad input parameter") })
  @DeleteMapping("/publishers/{id}")
  public ResponseEntity<Void> deletePublisher(@Parameter(
      description = "id of the publisher to delete", required = true,
      example = "1") @PathVariable Long id) {
    log.debug("REST request to delete Publisher : {}", id);
    publisherService.delete(id);
    return ResponseEntity.noContent()
        .headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
  }
}
