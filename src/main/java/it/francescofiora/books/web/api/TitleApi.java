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
@Tag(name = "title", description = "Title Rest API")
@RequestMapping("/api")
public class TitleApi {

  private static final String ENTITY_NAME = "Title";

  private final Logger log = LoggerFactory.getLogger(this.getClass());

  private final TitleService titleService;

  public TitleApi(TitleService titleService) {
    this.titleService = titleService;
  }

  /**
   * {@code POST  /titles} : Create a new title.
   *
   * @param titleDto the titleDto to create.
   * @return the {@link ResponseEntity} with status {@code 201 (Created)}.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @Operation(
      summary = "add new Title", description = "add a new Title to the system", tags = { "title" })
  @ApiResponses(
      value = { @ApiResponse(responseCode = "201", description = "Title created"),
          @ApiResponse(responseCode = "400", description = "invalid input, object invalid"),
          @ApiResponse(responseCode = "409", description = "an existing Title already exists") })
  @PostMapping("/titles")
  public ResponseEntity<Void> createTitle(
      @Parameter(description = "add new Title") @Valid @RequestBody NewTitleDto titleDto)
      throws URISyntaxException {
    log.debug("REST request to save Title : {}", titleDto);
    TitleDto result = titleService.create(titleDto);
    return ResponseEntity.created(new URI("/api/titles/" + result.getId()))
        .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
        .build();
  }

  /**
   * {@code PUT  /titles} : Updates an existing title.
   *
   * @param titleDto the titleDto to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)}, or with
   *         status {@code 400 (Bad Request)} if the titleDto is not valid, or
   *         with status {@code 500 (Internal Server Error)} if the titleDto
   *         couldn't be updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @Operation(
      summary = "update Title", description = "update an Title to the system", tags = { "title" })
  @ApiResponses(
      value = { @ApiResponse(responseCode = "200", description = "Title updated"),
          @ApiResponse(responseCode = "400", description = "invalid input, object invalid"),
          @ApiResponse(responseCode = "404", description = "not found") })
  @PutMapping("/titles")
  public ResponseEntity<Void> updateTitle(
      @Parameter(description = "title to update") @Valid @RequestBody UpdatebleTitleDto titleDto)
      throws URISyntaxException {
    log.debug("REST request to update Title : {}", titleDto);
    if (titleDto.getId() == null) {
      throw new BadRequestAlertException(ENTITY_NAME, "idnull", "Invalid id");
    }
    titleService.update(titleDto);
    return ResponseEntity.ok()
        .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, titleDto.getId().toString()))
        .build();
  }

  /**
   * {@code GET  /titles} : get all the titles.
   *
   * @param pageable the pagination information.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
   *         of titles in body.
   */
  @Operation(
      summary = "searches titles", description = "By passing in the appropriate options, "
          + "you can search for available titles in the system",
      tags = { "title" })
  @ApiResponses(
      value = {
          @ApiResponse(
              responseCode = "200", description = "search results matching criteria",
              content = @Content(
                  array = @ArraySchema(schema = @Schema(implementation = TitleDto.class)))),
          @ApiResponse(responseCode = "400", description = "bad input parameter") })
  @GetMapping("/titles")
  public ResponseEntity<List<TitleDto>> getAllTitles(Pageable pageable) {
    log.debug("REST request to get a page of Titles");
    Page<TitleDto> page = titleService.findAll(pageable);
    HttpHeaders headers = PaginationUtil
        .generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
    return ResponseEntity.ok().headers(headers).body(page.getContent());
  }

  /**
   * {@code GET  /titles/:id} : get the "id" title.
   *
   * @param id the id of the titleDto to retrieve.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
   *         the titleDto, or with status {@code 404 (Not Found)}.
   */
  @Operation(
      summary = "searches title by 'id'", description = "searches title by 'id'",
      tags = { "title" })
  @ApiResponses(
      value = {
          @ApiResponse(
              responseCode = "200", description = "search results matching criteria",
              content = @Content(schema = @Schema(implementation = TitleDto.class))),
          @ApiResponse(responseCode = "400", description = "bad input parameter"),
          @ApiResponse(responseCode = "404", description = "not found") })
  @GetMapping("/titles/{id}")
  public ResponseEntity<TitleDto> getTitle(@PathVariable Long id) {
    log.debug("REST request to get Title : {}", id);
    Optional<TitleDto> titleDto = titleService.findOne(id);
    return ResponseUtil.wrapOrNotFound(ENTITY_NAME, titleDto);
  }

  /**
   * {@code DELETE  /titles/:id} : delete the "id" title.
   *
   * @param id the id of the titleDto to delete.
   * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
   */
  @Operation(
      summary = "delete title by 'id'", description = "delete an title by 'id'", tags = { "title" })
  @ApiResponses(
      value = { @ApiResponse(responseCode = "204", description = "Title deleted"),
          @ApiResponse(responseCode = "400", description = "bad input parameter") })
  @DeleteMapping("/titles/{id}")
  public ResponseEntity<Void> deleteTitle(@PathVariable Long id) {
    log.debug("REST request to delete Title : {}", id);
    titleService.delete(id);
    return ResponseEntity.noContent()
        .headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
  }
}
