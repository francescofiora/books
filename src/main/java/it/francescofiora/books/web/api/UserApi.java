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
import it.francescofiora.books.service.UserService;
import it.francescofiora.books.service.dto.NewUserDto;
import it.francescofiora.books.service.dto.UserDto;
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
 * User Api RestController.
 */
@RestController
@Tag(name = "user", description = "User Rest API")
@RequestMapping("/api/v1")
@SecurityRequirement(name = "Bearer Authentication")
public class UserApi extends AbstractApi {

  private static final String ENTITY_NAME = "UserDto";
  private static final String TAG = "user";

  private final UserService userService;

  public UserApi(UserService userService) {
    super(ENTITY_NAME);
    this.userService = userService;
  }

  /**
   * Create a new user.
   *
   * @param userDto the user to create
   * @return the Response
   */
  @Operation(summary = "Add new User", description = "Add a new User to the system", tags = {TAG})
  @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "User created"),
      @ApiResponse(responseCode = "400", description = "Invalid input, object invalid")})
  @PostMapping("/users")
  @PreAuthorize(AUTHORIZE_USER_UPDATE)
  public ResponseEntity<Void> createUser(
      @Parameter(description = "Add new User") @Valid @RequestBody NewUserDto userDto) {
    var result = userService.create(userDto);
    return postResponse("/api/v1/users/" + result.getId(), result.getId());
  }

  /**
   * Updates an existing user.
   *
   * @param userDto the user to update
   * @param id the id of the user to update
   * @return the Response
   */
  @Operation(summary = "Update User", description = "Update an User to the system", tags = {TAG})
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "User updated"),
      @ApiResponse(responseCode = "400", description = "Invalid input, object invalid"),
      @ApiResponse(responseCode = "404", description = "Not found")})
  @PutMapping("/users/{id}")
  @PreAuthorize(AUTHORIZE_USER_UPDATE)
  public ResponseEntity<Void> updateUser(
      @Parameter(description = "User to update") @Valid @RequestBody UserDto userDto,
      @Parameter(description = "The id of the user to update", required = true, example = "1")
      @PathVariable("id") Long id) {
    if (!id.equals(userDto.getId())) {
      throw new BadRequestAlertException(ENTITY_NAME, String.valueOf(userDto.getId()),
          "Invalid id");
    }
    userService.update(userDto);
    return putResponse(id);
  }

  /**
   * Get all the users.
   *
   * @param username the username
   * @param pageable the pagination information
   * @return the list of users
   */
  @Operation(summary = "Searches users",
      description = "By passing in the appropriate options, "
          + "you can search for available users in the system",
      tags = {TAG})
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Search results matching criteria",
          content = @Content(
              array = @ArraySchema(schema = @Schema(implementation = UserDto.class)))),
      @ApiResponse(responseCode = "400", description = "Bad input parameter")})
  @GetMapping("/users")
  @PreAuthorize(AUTHORIZE_USER_READ)
  public ResponseEntity<List<UserDto>> getAllUsers(
      @Parameter(description = "User Name", example = "user", in = ParameterIn.QUERY)
      @RequestParam(value = "username", required = false) String username,
      @Parameter(example = "{\n  \"page\": 0,  \"size\": 10}", in = ParameterIn.QUERY)
      Pageable pageable) {
    return getResponse(userService.findAll(username, pageable));
  }

  /**
   * Get the user by id.
   *
   * @param id the id of the user to retrieve
   * @return the user
   */
  @Operation(summary = "Searches user by 'id'", description = "Searches user by 'id'", tags = {TAG})
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Search results matching criteria",
          content = @Content(schema = @Schema(implementation = UserDto.class))),
      @ApiResponse(responseCode = "400", description = "Bad input parameter"),
      @ApiResponse(responseCode = "404", description = "Not found")})
  @GetMapping("/users/{id}")
  @PreAuthorize(AUTHORIZE_USER_READ)
  public ResponseEntity<UserDto> getUser(
      @Parameter(description = "The id of the user to get", required = true, example = "1")
      @PathVariable("id") Long id) {
    return getResponse(userService.findOne(id), id);
  }

  /**
   * Delete the user by id.
   *
   * @param id the id of the user to delete
   * @return the Response
   */
  @Operation(summary = "Delete user by 'id'", description = "Delete an user by 'id'", tags = {TAG})
  @ApiResponses(value = {@ApiResponse(responseCode = "204", description = "User deleted"),
      @ApiResponse(responseCode = "400", description = "Bad input parameter")})
  @DeleteMapping("/users/{id}")
  @PreAuthorize(AUTHORIZE_USER_UPDATE)
  public ResponseEntity<Void> deleteUser(
      @Parameter(description = "The id of the user to delete", required = true, example = "1")
      @PathVariable("id") Long id) {
    userService.delete(id);
    return deleteResponse(id);
  }
}
