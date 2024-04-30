package it.francescofiora.books.itt.cucumber;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import io.cucumber.java.AfterAll;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import it.francescofiora.books.itt.api.AbstractTestContainer;
import it.francescofiora.books.itt.container.SpringAplicationContainer;
import it.francescofiora.books.itt.container.StartStopContainers;
import it.francescofiora.books.itt.util.ContainerGenerator;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.testcontainers.containers.output.Slf4jLogConsumer;

/**
 * Cucumber Step Definitions.
 */
@Slf4j
public class StepDefinitions extends AbstractTestContainer {

  private static final String DATASOURCE_URL = "jdbc:mysql://book-mysql:3306/books";

  private static SpringAplicationContainer bookApi;
  private static StartStopContainers containers = new StartStopContainers();
  private static ContainerGenerator containerGenerator = new ContainerGenerator();

  private static Long userId;
  private static Long userReaderId;
  private static ResponseEntity<String> resultString;
  private static ResponseEntity<Void> resultVoid;
  private static JSONObject role;
  private static JSONObject author;
  private static JSONObject publisher;
  private static JSONObject title;
  private static Long roleId;
  private static Long authorId;
  private static Long publisherId;
  private static Long titleId;

  /**
   * Start all containers.
   *
   * @throws JSONException if errors occur
   */
  @BeforeAll
  public static void init() throws JSONException {
    var mySql = containerGenerator.createMySqlContainer();
    containers.add(mySql);

    // @formatter:off
    bookApi = containerGenerator
        .createSpringAplicationContainer("francescofiora-book")
        .withEnv("SPRING_PROFILES_ACTIVE", "Logging")
        .withEnv("DATASOURCE_URL", DATASOURCE_URL)
        .withEnv("DATASOURCE_ADMIN_USERNAME", ContainerGenerator.MYSQL_USER_ADMIN)
        .withEnv("DATASOURCE_ADMIN_PASSWORD", ContainerGenerator.MYSQL_PASSWORD_ADMIN)
        .withLogConsumer(new Slf4jLogConsumer(log))
        .withExposedPorts(8081);
    // @formatter:on
    containers.add(bookApi);

    setUser(USER_ADMIN, PASSWORD);

    userId = createUser(USER_TEST, PASSWORD_TEST, List.of("ROLE_BOOK_ADMIN"));
    userReaderId = createUser(USER_READER_TEST, PASSWORD_TEST, List.of("ROLE_BOOK_READ"));
  }

  private static void setUser(String user, String password) {
    try {
      var signin = new JSONObject();
      signin.put("username", user);
      signin.put("password", password);
      var result = bookApi.performLogin(signin.toString());
      var tokenObj = new JSONObject(result.getBody());
      bookApi.setToken(tokenObj.getString("token"));
    } catch (JSONException e) {
      throw new IllegalArgumentException(e);
    }
  }

  private static Long createUser(String user, String password, List<String> listRole)
      throws JSONException {
    var result = bookApi.performGet(ROLE_URL);
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    var mapRole = listToMap(new JSONArray(result.getBody()), NAME);

    var roles = createRefs(mapRole, listRole);

    var newUser = new JSONObject();
    newUser.put("roles", roles);
    newUser.put("password", password);
    newUser.put("username", user);
    newUser.put("accountNonExpired", true);
    newUser.put("accountNonLocked", true);
    newUser.put("credentialsNonExpired", true);
    newUser.put("enabled", true);

    return bookApi.createAndReturnId("/api/v1/users", newUser.toString());
  }

  /**
   * Given the Admin User.
   */
  @Given("the admin user")
  public void givenTheAdminUser() {
    setUser(ROLE_ADMIN, PASSWORD);
  }

  /**
   * Given the Book Admin User.
   */
  @Given("the book admin user")
  public void givenTheBookAdminUser() {
    setUser(USER_TEST, PASSWORD_TEST);
  }

  /**
   * Given the Reader User.
   */
  @Given("the reader user")
  public void givenTheReaderUser() {
    setUser(USER_READER_TEST, PASSWORD_TEST);
  }

  /**
   * Perform GET endpoint of entities.
   *
   * @param entities the entities
   */
  @When("^get the (\\w+)$")
  public void getList(String entities) {
    resultString = switch (entities) {
      case "Roles" -> bookApi.performGet(ROLE_URL);
      case "Permissions" -> bookApi.performGet(PERMISSION_URL);
      case "Authors" -> bookApi.performGet(AUTHOR_URL);
      case "Publishers" -> bookApi.performGet(PUBLISHER_URL);
      case "Titles" -> bookApi.performGet(TITLE_URL);
      default -> throw new IllegalArgumentException("Unexpected value: " + entities);
    };
  }

  /**
   * Check then Status of last operation saved in resultString.
   *
   * @param op GET, DELETE or PUT
   * @param statusCode the aspected statusCode
   */
  @Then("^the (\\w+) status should be (\\w+)$")
  public void thenStatusIs(final String op, final String statusCode) {
    var status = HttpStatus.valueOf(statusCode);
    switch (op) {
      case "GET" -> assertThat(resultString.getStatusCode()).isEqualTo(status);
      case "DELETE", "PUT" -> assertThat(resultVoid.getStatusCode()).isEqualTo(status);
      default -> throw new IllegalArgumentException("Unexpected value: " + op);
    }
  }

  @Then("the list of Role and Permission should be not empty")
  public void thenRolePermissionNotEmpty() throws JSONException {
    checkRoleAndPermissionList(new JSONArray(resultString.getBody()));
  }

  /**
   * Check permission of the user that should be not able to GET the entities.
   *
   * @param entities the entities
   */
  @Then("^should be not able to get the (\\w+)$")
  public void thenForbiddenGet(final String entities) {
    switch (entities) {
      case "Permissions" -> assertThrows(HttpClientErrorException.class,
          () -> bookApi.performGet(PERMISSION_URL));
      case "Roles" -> assertThrows(HttpClientErrorException.class,
          () -> bookApi.performGet(ROLE_URL));
      default -> throw new IllegalArgumentException("Unexpected value: " + entities);
    }
  }

  /**
   * Check permission of the user that should be not able to GET the entity.
   *
   * @param entity the entity
   */
  @Then("^should be not able to get that (\\w+)$")
  public void thenForbiddenGetBy(final String entity) {
    switch (entity) {
      case "Role" -> assertThrows(HttpClientErrorException.class,
          () -> bookApi.performGet(ROLE_URL + "/" + roleId));
      case "Author" -> assertThrows(HttpClientErrorException.class,
          () -> bookApi.performGet(AUTHOR_URL + "/" + authorId));
      case "Publisher" -> assertThrows(HttpClientErrorException.class,
          () -> bookApi.performGet(PUBLISHER_URL + "/" + publisherId));
      case "Title" -> assertThrows(HttpClientErrorException.class,
          () -> bookApi.performGet(TITLE_URL + "/" + titleId));
      default -> throw new IllegalArgumentException("Unexpected value: " + entity);
    }
  }

  /**
   * Fetch entity from GET.
   *
   * @param entity the entity
   */
  @Then("^should be able to get that (\\w+)$")
  public void thenGet(final String entity) {
    resultString = switch (entity) {
      case "Role" -> bookApi.performGet(ROLE_URL + "/" + roleId);
      case "Author" -> bookApi.performGet(AUTHOR_URL + "/" + authorId);
      case "Publisher" -> bookApi.performGet(PUBLISHER_URL + "/" + publisherId);
      case "Title" -> bookApi.performGet(TITLE_URL + "/" + titleId);
      default -> throw new IllegalArgumentException("Unexpected value: " + entity);
    };
  }

  /**
   * Delete entity.
   *
   * @param entity the entity
   */
  @When("^delete the (\\w+)$")
  public void thenDelete(final String entity) {
    resultVoid = switch (entity) {
      case "Role" -> bookApi.performDelete(ROLE_URL + "/" + roleId);
      case "Author" -> bookApi.performDelete(AUTHOR_URL + "/" + authorId);
      case "Publisher" -> bookApi.performDelete(PUBLISHER_URL + "/" + publisherId);
      case "Title" -> bookApi.performDelete(TITLE_URL + "/" + titleId);
      default -> throw new IllegalArgumentException("Unexpected value: " + entity);
    };
  }

  /**
   * Compare the object used from creation with the object from fetching GET.
   *
   * @param entity the entity
   * @param op POST or PUT
   * @throws JSONException if errors occur
   */
  @Then("^the (\\w+) from (\\w+) should the same as from GET$")
  public void thenCompare(final String entity, final String op) throws JSONException {
    switch (entity) {
      case "Role":
        var roleFromGet = new JSONObject(resultString.getBody());
        checkRole(roleFromGet, role);
        role = roleFromGet;
        break;
      case "Author":
        var authorFromGet = new JSONObject(resultString.getBody());
        checkAuthor(authorFromGet, author);
        author = authorFromGet;
        break;
      case "Publisher":
        var publisherFromGet = new JSONObject(resultString.getBody());
        checkPublisher(publisherFromGet, publisher);
        publisher = publisherFromGet;
        break;
      case "Title":
        var titleFromGet = new JSONObject(resultString.getBody());
        checkTitle(titleFromGet, title);
        title = titleFromGet;
        break;
      default:
        throw new IllegalArgumentException("Unexpected value: " + entity);
    }
  }

  /**
   * Check permission of the user that should be not able to create new entity.
   *
   * @param entity the entity
   */
  @Then("^should be not able to create new (\\w+)$")
  public void thenForbiddenCreate(final String entity) {
    switch (entity) {
      case "Author" -> assertThrows(HttpClientErrorException.class,
          () -> bookApi.performPost(AUTHOR_URL, createAuthor().toString()));
      case "Publisher" -> assertThrows(HttpClientErrorException.class,
          () -> bookApi.performPost(PUBLISHER_URL, createPublisher().toString()));
      default -> throw new IllegalArgumentException("Unexpected value: " + entity);
    }
  }

  /**
   * Create the entity.
   *
   * @param entity the entity
   * @throws JSONException if errors occur
   */
  @When("^create a new (\\w+)$")
  public void thenCreate(final String entity) throws JSONException {
    switch (entity) {
      case "Role" -> roleId = createRole();
      case "Author" -> authorId = createTheAuthor();
      case "Publisher" -> publisherId = createThePublisher();
      case "Title" -> titleId = createTheTitle();
      default -> throw new IllegalArgumentException("Unexpected value: " + entity);
    }
  }

  private Long createThePublisher() throws JSONException {
    publisher = createPublisher();
    return bookApi.createAndReturnId(PUBLISHER_URL, publisher.toString());
  }

  private Long createTheAuthor() throws JSONException {
    author = createAuthor();
    return bookApi.createAndReturnId(AUTHOR_URL, author.toString());
  }

  private Long createTheTitle() throws JSONException {
    title = createTitle();
    title.put("publisher", createRef(publisherId));
    title.put("authors", createRefs(List.of(authorId)));
    return bookApi.createAndReturnId(TITLE_URL, title.toString());
  }

  private Long createRole() throws JSONException {
    var mapPermission = listToMap(new JSONArray(resultString.getBody()), NAME);
    var permissions =
        createRefs(mapPermission, List.of("OP_ROLE_READ", "OP_USER_READ", "OP_BOOK_READ"));
    role = new JSONObject();
    role.put("permissions", permissions);
    role.put(NAME, "newRole");
    role.put(DESCRIPTION, "New Role");

    return bookApi.createAndReturnId(ROLE_URL, role.toString());
  }

  /**
   * Update the entity.
   *
   * @param entity the entity
   * @throws JSONException if errors occur
   */
  @When("^update the (\\w+)$")
  public void whenUpdate(final String entity) throws JSONException {
    switch (entity) {
      case "Role":
        role.put(NAME, "updateRole");
        role.put(DESCRIPTION, "Update Role");
        resultVoid = bookApi.performPut(ROLE_URL + "/" + roleId, role.toString());
        break;
      case "Author":
        author.put(FIRST_NAME, "Updatename");
        author.put(LAST_NAME, "Updatelast");
        resultVoid = bookApi.performPut(AUTHOR_URL + "/" + authorId, author.toString());
        break;
      case "Publisher":
        publisher.put(PUBLISHER_NAME, "Update Publisher Name");
        resultVoid = bookApi.performPut(PUBLISHER_URL + "/" + publisherId, publisher.toString());
        break;
      case "Title":
        title.put(NAME, "Title Name Update");
        title.put(EDITION_NUMBER, 20L);
        title.put(LANGUAGE, "ITALIAN");
        title.put(COPYRIGHT, 2001);
        title.put(IMAGE_FILE, "image file update");
        title.put(PRICE, 60);
        resultVoid = bookApi.performPut(TITLE_URL + "/" + titleId, title.toString());
        break;
      default:
        throw new IllegalArgumentException("Unexpected value: " + entity);
    }
  }

  /**
   * Stop and close all containers.
   */
  @AfterAll
  public static void endAll() {
    setUser(USER_ADMIN, PASSWORD);

    var voidResult = bookApi.performDelete("/api/v1/users/" + userId);
    assertThat(voidResult.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

    voidResult = bookApi.performDelete("/api/v1/users/" + userReaderId);
    assertThat(voidResult.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

    containers.stopAndCloseAll();
  }
}
