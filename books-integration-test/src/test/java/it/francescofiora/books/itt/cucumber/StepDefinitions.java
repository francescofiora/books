package it.francescofiora.books.itt.cucumber;

import static org.junit.jupiter.api.Assertions.assertThrows;

import io.cucumber.java.AfterAll;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import it.francescofiora.books.itt.component.AuthorComponent;
import it.francescofiora.books.itt.component.BookApiService;
import it.francescofiora.books.itt.component.PublisherComponent;
import it.francescofiora.books.itt.component.RoleComponent;
import it.francescofiora.books.itt.component.TitleComponent;
import it.francescofiora.books.itt.component.UserComponent;
import it.francescofiora.books.itt.container.HttpClient;
import it.francescofiora.books.itt.container.StartStopContainers;
import it.francescofiora.books.itt.context.AuthorContext;
import it.francescofiora.books.itt.context.PublisherContext;
import it.francescofiora.books.itt.context.RoleContext;
import it.francescofiora.books.itt.context.TitleContext;
import it.francescofiora.books.itt.util.ContainerGenerator;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.springframework.web.client.HttpClientErrorException;
import org.testcontainers.containers.output.Slf4jLogConsumer;

/**
 * Cucumber Step Definitions.
 */
@Slf4j
public class StepDefinitions {

  private static final String DATASOURCE_URL = "jdbc:mysql://book-mysql:3306/books";
  private static final String ROLE_ADMIN = "roleAdmin";
  private static final String USER_ADMIN = "userAdmin";
  private static final String USER_TEST = "username";
  private static final String USER_READER_TEST = "userRead";
  private static final String PASSWORD_TEST = "mypassword";

  public static final String PASSWORD = "password";

  private static UserComponent userComponent;
  private static RoleComponent roleComponent;
  private static PublisherComponent publisherComponent;
  private static AuthorComponent authorComponent;
  private static TitleComponent titleComponent;
  private static final StartStopContainers containers = new StartStopContainers();

  /**
   * Start all containers.
   */
  @BeforeAll
  public static void init() {
    var containerGenerator = new ContainerGenerator();
    var mySql = containerGenerator.createMySqlContainer();
    containers.add(mySql);

    // @formatter:off
    var bookApi = containerGenerator
        .createSpringAplicationContainer("francescofiora-book")
        .withEnv("SPRING_PROFILES_ACTIVE", "Logging")
        .withEnv("DATASOURCE_URL", DATASOURCE_URL)
        .withEnv("DATASOURCE_ADMIN_USERNAME", ContainerGenerator.MYSQL_USER_ADMIN)
        .withEnv("DATASOURCE_ADMIN_PASSWORD", ContainerGenerator.MYSQL_PASSWORD_ADMIN)
        .withLogConsumer(new Slf4jLogConsumer(log))
        .withExposedPorts(8081);
    // @formatter:on
    containers.add(bookApi);
    var httpClient = new HttpClient(bookApi.getHost(), bookApi.getFirstMappedPort());
    var bookApiService = new BookApiService(httpClient);
    userComponent = new UserComponent(bookApiService);
    roleComponent = new RoleComponent(bookApiService, new RoleContext());
    var publisherContext = new PublisherContext();
    publisherComponent = new PublisherComponent(bookApiService, publisherContext);
    var authorContext = new AuthorContext();
    authorComponent = new AuthorComponent(bookApiService, authorContext);
    titleComponent =
        new TitleComponent(bookApiService, authorContext, publisherContext, new TitleContext());

    userComponent.setUser(USER_ADMIN, PASSWORD);

    userComponent.createUser(USER_TEST, PASSWORD_TEST, List.of("ROLE_BOOK_ADMIN"));
    userComponent.createUser(USER_READER_TEST, PASSWORD_TEST, List.of("ROLE_BOOK_READ"));
  }

  /**
   * Given the Admin User.
   */
  @Given("the admin user")
  public void givenTheAdminUser() {
    userComponent.setUser(ROLE_ADMIN, PASSWORD);
  }

  /**
   * Given the Book Admin User.
   */
  @Given("the book admin user")
  public void givenTheBookAdminUser() {
    userComponent.setUser(USER_TEST, PASSWORD_TEST);
  }

  /**
   * Given the Reader User.
   */
  @Given("the reader user")
  public void givenTheReaderUser() {
    userComponent.setUser(USER_READER_TEST, PASSWORD_TEST);
  }

  /**
   * Perform GET endpoint of entities.
   *
   * @param entities the entities
   */
  @When("^get the (\\w+)$")
  public void getList(String entities) {
    switch (entities) {
      case "Roles" -> roleComponent.findRoles();
      case "Permissions" -> roleComponent.findPermissions();
      case "Authors" -> authorComponent.findAuthors();
      case "Publishers" -> publisherComponent.findPublishers();
      case "Titles" -> titleComponent.findTitles();
      default -> throw new IllegalArgumentException("Unexpected value: " + entities);
    };
  }

  /**
   * Check then list of last research is not empty.
   *
   * @param entities the entities name
   */
  @Then("^the list of (\\w+) should be not empty$")
  public void thenRolePermissionNotEmpty(String entities) {
    switch (entities) {
      case "Permissions" -> roleComponent.checkPermissionList();
      case "Roles" -> roleComponent.checkRoleList();
      default -> throw new IllegalArgumentException("Unexpected value: " + entities);
    }
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
          () -> roleComponent.findPermissions());
      case "Roles" -> assertThrows(HttpClientErrorException.class,
          () -> roleComponent.findRoles());
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
          () -> roleComponent.getRoleById());
      case "Author" -> assertThrows(HttpClientErrorException.class,
          () -> authorComponent.getAuthorById());
      case "Publisher" -> assertThrows(HttpClientErrorException.class,
          () -> publisherComponent.getPublisherById());
      case "Title" -> assertThrows(HttpClientErrorException.class,
          () -> titleComponent.getTitleById());
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
    switch (entity) {
      case "Role" -> roleComponent.getRoleById();
      case "Author" -> authorComponent.getAuthorById();
      case "Publisher" -> publisherComponent.getPublisherById();
      case "Title" -> titleComponent.getTitleById();
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
    switch (entity) {
      case "Role" -> roleComponent.deleteRoleById();
      case "Author" -> authorComponent.deleteAuthorById();
      case "Publisher" -> publisherComponent.deletePublisherById();
      case "Title" -> titleComponent.deleteTitleById();
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
      case "Role" -> roleComponent.checkRole();
      case "Author" -> authorComponent.checkAuthor();
      case "Publisher" -> publisherComponent.checkPublisher();
      case "Title" -> titleComponent.checkTitle();
      default -> throw new IllegalArgumentException("Unexpected value: " + entity);
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
          () -> authorComponent.createAuthor());
      case "Publisher" -> assertThrows(HttpClientErrorException.class,
          () -> publisherComponent.createPublisher());
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
      case "Role" -> roleComponent.createRole();
      case "Author" -> authorComponent.createAuthor();
      case "Publisher" -> publisherComponent.createPublisher();
      case "Title" -> titleComponent.createTitle();
      default -> throw new IllegalArgumentException("Unexpected value: " + entity);
    }
  }

  /**
   * Update the entity.
   *
   * @param entity the entity
   */
  @When("^update the (\\w+)$")
  public void whenUpdate(final String entity) {
    switch (entity) {
      case "Role" -> roleComponent.updateRole();
      case "Author" -> authorComponent.updateAuthor();
      case "Publisher" -> publisherComponent.updatePublisher();
      case "Title" -> titleComponent.updateTitle();
      default -> throw new IllegalArgumentException("Unexpected value: " + entity);
    }
  }

  /**
   * Stop and close all containers.
   */
  @AfterAll
  public static void endAll() {
    userComponent.setUser(USER_ADMIN, PASSWORD);
    userComponent.deleteAllUsers();

    containers.stopAndCloseAll();
  }
}
