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
import it.francescofiora.books.itt.context.AuthorContext;
import it.francescofiora.books.itt.context.PublisherContext;
import it.francescofiora.books.itt.context.RoleContext;
import it.francescofiora.books.itt.context.TitleContext;
import it.francescofiora.books.itt.util.ContainerGenerator;
import it.francescofiora.books.itt.util.UtilTests;
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
  private static final StartStopContainers containers = new StartStopContainers();
  private static final ContainerGenerator containerGenerator = new ContainerGenerator();

  private static Long userId;
  private static Long userReaderId;
  private static ResponseEntity<String> resultString;
  private static ResponseEntity<Void> resultVoid;
  private static final RoleContext roleContext = new RoleContext();
  private static final AuthorContext authorContext = new AuthorContext();
  private static final PublisherContext publisherContext = new PublisherContext();
  private static final TitleContext titleContext = new TitleContext();

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
    var mapRole = UtilTests.listToMap(new JSONArray(result.getBody()), NAME);

    var roles = UtilTests.createRefs(mapRole, listRole);

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
          () -> bookApi.performGet(ROLE_URL + "/" + roleContext.getRoleId()));
      case "Author" -> assertThrows(HttpClientErrorException.class,
          () -> bookApi.performGet(AUTHOR_URL + "/" + authorContext.getAuthorId()));
      case "Publisher" -> assertThrows(HttpClientErrorException.class,
          () -> bookApi.performGet(PUBLISHER_URL + "/" + publisherContext.getPublisherId()));
      case "Title" -> assertThrows(HttpClientErrorException.class,
          () -> bookApi.performGet(TITLE_URL + "/" + titleContext.getTitleId()));
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
      case "Role" -> bookApi.performGet(ROLE_URL + "/" + roleContext.getRoleId());
      case "Author" -> bookApi.performGet(AUTHOR_URL + "/" + authorContext.getAuthorId());
      case "Publisher" ->
          bookApi.performGet(PUBLISHER_URL + "/" + publisherContext.getPublisherId());
      case "Title" -> bookApi.performGet(TITLE_URL + "/" + titleContext.getTitleId());
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
      case "Role" -> bookApi.performDelete(ROLE_URL + "/" + roleContext.getRoleId());
      case "Author" -> bookApi.performDelete(AUTHOR_URL + "/" + authorContext.getAuthorId());
      case "Publisher" ->
          bookApi.performDelete(PUBLISHER_URL + "/" + publisherContext.getPublisherId());
      case "Title" -> bookApi.performDelete(TITLE_URL + "/" + titleContext.getTitleId());
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
        checkRole(roleFromGet, roleContext.getRole());
        roleContext.setRole(roleFromGet);
        break;
      case "Author":
        var authorFromGet = new JSONObject(resultString.getBody());
        checkAuthor(authorFromGet, authorContext.getAuthor());
        authorContext.setAuthor(authorFromGet);
        break;
      case "Publisher":
        var publisherFromGet = new JSONObject(resultString.getBody());
        checkPublisher(publisherFromGet, publisherContext.getPublisher());
        publisherContext.setPublisher(publisherFromGet);
        break;
      case "Title":
        var titleFromGet = new JSONObject(resultString.getBody());
        checkTitle(titleFromGet, titleContext.getTitle());
        titleContext.setTitle(titleFromGet);
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
          () -> bookApi.performPost(AUTHOR_URL, UtilTests.createAuthor().toString()));
      case "Publisher" -> assertThrows(HttpClientErrorException.class,
          () -> bookApi.performPost(PUBLISHER_URL, UtilTests.createPublisher().toString()));
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
      case "Role" -> roleContext.setRoleId(createRole());
      case "Author" -> authorContext.setAuthorId(createTheAuthor());
      case "Publisher" -> publisherContext.setPublisherId(createThePublisher());
      case "Title" -> titleContext.setTitleId(createTheTitle());
      default -> throw new IllegalArgumentException("Unexpected value: " + entity);
    }
  }

  private Long createThePublisher() throws JSONException {
    publisherContext.setPublisher(UtilTests.createPublisher());
    return bookApi.createAndReturnId(PUBLISHER_URL, publisherContext.getPublisher().toString());
  }

  private Long createTheAuthor() throws JSONException {
    authorContext.setAuthor(UtilTests.createAuthor());
    return bookApi.createAndReturnId(AUTHOR_URL, authorContext.getAuthor().toString());
  }

  private Long createTheTitle() throws JSONException {
    var title = UtilTests.createTitle();
    title.put("publisher", UtilTests.createRef(publisherContext.getPublisherId()));
    title.put("authors", UtilTests.createRefs(List.of(authorContext.getAuthorId())));
    titleContext.setTitle(title);
    return bookApi.createAndReturnId(TITLE_URL, title.toString());
  }

  private Long createRole() throws JSONException {
    var mapPermission = UtilTests.listToMap(new JSONArray(resultString.getBody()), NAME);
    var permissions = UtilTests
        .createRefs(mapPermission, List.of("OP_ROLE_READ", "OP_USER_READ", "OP_BOOK_READ"));
    var role = new JSONObject();
    role.put("permissions", permissions);
    role.put(NAME, "newRole");
    role.put(DESCRIPTION, "New Role");
    roleContext.setRole(role);

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
        roleContext.getRole().put(NAME, "updateRole");
        roleContext.getRole().put(DESCRIPTION, "Update Role");
        resultVoid = bookApi.performPut(
            ROLE_URL + "/" + roleContext.getRoleId(), roleContext.getRole().toString());
        break;
      case "Author":
        authorContext.getAuthor().put(FIRST_NAME, "Updatename");
        authorContext.getAuthor().put(LAST_NAME, "Updatelast");
        resultVoid = bookApi.performPut(
            AUTHOR_URL + "/" + authorContext.getAuthorId(), authorContext.getAuthor().toString());
        break;
      case "Publisher":
        publisherContext.getPublisher().put(PUBLISHER_NAME, "Update Publisher Name");
        resultVoid = bookApi.performPut(
            PUBLISHER_URL + "/" + publisherContext.getPublisherId(),
            publisherContext.getPublisher().toString());
        break;
      case "Title":
        titleContext.getTitle().put(NAME, "Title Name Update");
        titleContext.getTitle().put(EDITION_NUMBER, 20L);
        titleContext.getTitle().put(LANGUAGE, "ITALIAN");
        titleContext.getTitle().put(COPYRIGHT, 2001);
        titleContext.getTitle().put(IMAGE_FILE, "image file update");
        titleContext.getTitle().put(PRICE, 60);
        resultVoid =
            bookApi.performPut(
                TITLE_URL + "/" + titleContext.getTitleId(), titleContext.getTitle().toString());
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
