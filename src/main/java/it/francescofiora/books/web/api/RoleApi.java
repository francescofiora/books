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
import it.francescofiora.books.service.RoleService;
import it.francescofiora.books.service.dto.NewRoleDto;
import it.francescofiora.books.service.dto.PermissionDto;
import it.francescofiora.books.service.dto.RoleDto;
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
 * Role Api RestController.
 */
@RestController
@Tag(name = "role", description = "Role Rest API")
@RequestMapping("/api/v1")
@SecurityRequirement(name = "Bearer Authentication")
public class RoleApi extends AbstractApi {

  private static final String ENTITY_NAME = "RoleDto";
  private static final String TAG = "role";

  private final RoleService roleService;

  public RoleApi(RoleService roleService) {
    super(ENTITY_NAME);
    this.roleService = roleService;
  }

  /**
   * Get all the permissions.
   *
   * @param pageable the pagination information
   * @return the list of permissions
   */
  @Operation(summary = "Searches permissions",
      description = "By passing in the appropriate options, "
          + "you can search for available permissions in the system",
      tags = {"permission"})
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Search results matching criteria",
          content = @Content(
              array = @ArraySchema(schema = @Schema(implementation = PermissionDto.class)))),
      @ApiResponse(responseCode = "400", description = "Bad input parameter")})
  @GetMapping("/permissions")
  @PreAuthorize(AUTHORIZE_ROLE_READ)
  public ResponseEntity<List<PermissionDto>> getAllPermissions(
      @Parameter(example = "{\n  \"page\": 0,  \"size\": 10}", in = ParameterIn.QUERY)
      Pageable pageable) {
    return getResponse(roleService.findPermissions(pageable));
  }

  /**
   * Create a new role.
   *
   * @param roleDto the role to create
   * @return the Response
   */
  @Operation(summary = "Add new Role", description = "Add a new Role to the system", tags = {TAG})
  @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Role created"),
      @ApiResponse(responseCode = "400", description = "Invalid input, object invalid")})
  @PostMapping("/roles")
  @PreAuthorize(AUTHORIZE_ROLE_UPDATE)
  public ResponseEntity<Void> createRole(
      @Parameter(description = "Add new Role") @Valid @RequestBody NewRoleDto roleDto) {
    var result = roleService.createRole(roleDto);
    return postResponse("/api/v1/roles/" + result.getId(), result.getId());
  }

  /**
   * Updates an existing role.
   *
   * @param roleDto the role to update
   * @param id the id of the role to update
   * @return the Response
   */
  @Operation(summary = "Update Role", description = "Update an Role to the system", tags = {TAG})
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Role updated"),
      @ApiResponse(responseCode = "400", description = "Invalid input, object invalid"),
      @ApiResponse(responseCode = "404", description = "Not found")})
  @PutMapping("/roles/{id}")
  @PreAuthorize(AUTHORIZE_ROLE_UPDATE)
  public ResponseEntity<Void> updateRole(
      @Parameter(description = "Role to update") @Valid @RequestBody RoleDto roleDto,
      @Parameter(description = "The id of the role to update", required = true, example = "1")
      @PathVariable("id") Long id) {
    if (!id.equals(roleDto.getId())) {
      throw new BadRequestAlertException(ENTITY_NAME, String.valueOf(roleDto.getId()),
          "Invalid id");
    }
    roleService.updateRole(roleDto);
    return putResponse(id);
  }

  /**
   * Get all the roles.
   *
   * @param name the name
   * @param description the description
   * @param pageable the pagination information
   * @return the list of roles
   */
  @Operation(summary = "Searches roles",
      description = "By passing in the appropriate options, "
          + "you can search for available roles in the system",
      tags = {TAG})
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Search results matching criteria",
          content = @Content(
              array = @ArraySchema(schema = @Schema(implementation = RoleDto.class)))),
      @ApiResponse(responseCode = "400", description = "Bad input parameter")})
  @GetMapping("/roles")
  @PreAuthorize(AUTHORIZE_ROLE_READ)
  public ResponseEntity<List<RoleDto>> getAllRoles(
      @Parameter(description = "Role's Name", example = "book_read", in = ParameterIn.QUERY)
      @RequestParam(value = "name", required = false) String name,
      @Parameter(description = "Description of the Role", example = "Read books",
          in = ParameterIn.QUERY)
      @RequestParam(value = "description", required = false) String description,
      @Parameter(example = "{\n  \"page\": 0,  \"size\": 10}", in = ParameterIn.QUERY)
      Pageable pageable) {
    return getResponse(roleService.findRoles(name, description, pageable));
  }

  /**
   * Get the role by id.
   *
   * @param id the id of the role to retrieve
   * @return the role
   */
  @Operation(summary = "Searches role by 'id'", description = "Searches role by 'id'", tags = {TAG})
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Search results matching criteria",
          content = @Content(schema = @Schema(implementation = RoleDto.class))),
      @ApiResponse(responseCode = "400", description = "Bad input parameter"),
      @ApiResponse(responseCode = "404", description = "Not found")})
  @GetMapping("/roles/{id}")
  @PreAuthorize(AUTHORIZE_ROLE_READ)
  public ResponseEntity<RoleDto> getRole(
      @Parameter(description = "The id of the role to get", required = true, example = "1")
      @PathVariable("id") Long id) {
    return getResponse(roleService.findOneRole(id), id);
  }

  /**
   * Delete the role by id.
   *
   * @param id the id of the role to delete
   * @return the Response
   */
  @Operation(summary = "Delete role by 'id'", description = "Delete an role by 'id'", tags = {TAG})
  @ApiResponses(value = {@ApiResponse(responseCode = "204", description = "Role deleted"),
      @ApiResponse(responseCode = "400", description = "Bad input parameter")})
  @DeleteMapping("/roles/{id}")
  @PreAuthorize(AUTHORIZE_ROLE_UPDATE)
  public ResponseEntity<Void> deleteRole(
      @Parameter(description = "The id of the role to delete", required = true, example = "1")
      @PathVariable("id") Long id) {
    roleService.deleteRole(id);
    return deleteResponse(id);
  }
}
