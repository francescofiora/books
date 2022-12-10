package it.francescofiora.books.itt.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import it.francescofiora.books.itt.container.SpringAplicationContainer;
import it.francescofiora.books.itt.container.StartStopContainers;
import it.francescofiora.books.itt.ssl.CertificateGenerator;
import it.francescofiora.books.itt.util.ContainerGenerator;
import java.io.File;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.TrustStrategy;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.output.Slf4jLogConsumer;

/**
 * Api Ssl Test.
 */
@Slf4j
class ApiSslTest extends AbstractTestContainer {

  private static final String DATASOURCE_URL = "jdbc:mysql://book-mysql:3306/books?"
      + "verifyServerCertificate=true&useSSL=true&requireSSL=true"
      + "&clientCertificateKeyStoreUrl=file:./config/book-api-keystore.jks"
      + "&clientCertificateKeyStorePassword=mypass"
      + "&trustCertificateKeyStoreUrl=file:./config/truststore.ts"
      + "&trustCertificateKeyStorePassword=mypass";

  private static SpringAplicationContainer bookApi;
  private static StartStopContainers containers = new StartStopContainers();
  private static ContainerGenerator containerGenerator = new ContainerGenerator(true);
  private static Long userId;

  private static RestTemplate getRestTemplateSsl() throws Exception {
    TrustStrategy acceptingTrustStrategy = (x509Certificates, s) -> true;
    var sslContext = SSLContexts.custom().loadTrustMaterial(null, acceptingTrustStrategy).build();
    var csf = new SSLConnectionSocketFactory(sslContext, new NoopHostnameVerifier());
    var restTemplate = new RestTemplate();
    restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(
        HttpClientBuilder.create().setSSLSocketFactory(csf).build()));
    return restTemplate;
  }

  /**
   * Configuration before all tests.
   *
   */
  @BeforeAll
  static void init() throws Exception {
    containerGenerator.useSsl();

    var mySql = containerGenerator.createMySqlContainer();
    containers.add(mySql);

    var tmpDir = containerGenerator.getTmpDir();

    // @formatter:off
    bookApi = containerGenerator
        .createSpringAplicationContainer("francescofiora-book")
        .withEnv("SPRING_PROFILES_ACTIVE", "Logging")
        .withEnv("SSL_ENABLED", "true")
        .withEnv("KEYSTORE_PASSWORD", CertificateGenerator.PASSWORD)
        .withEnv("KEYSTORE_FILE", "/workspace/config/book-api-keystore.jks")
        .withEnv("TRUSTSTORE_PASSWORD", CertificateGenerator.PASSWORD)
        .withEnv("TRUSTSTORE_FILE", "/workspace/config/truststore.ts")
        .withEnv("DATASOURCE_URL", DATASOURCE_URL)
        .withEnv("DATASOURCE_ADMIN_USERNAME", ContainerGenerator.MYSQL_USER_ADMIN)
        .withEnv("DATASOURCE_ADMIN_PASSWORD", ContainerGenerator.MYSQL_PASSWORD_ADMIN)
        .withLogConsumer(new Slf4jLogConsumer(log))
        .withExposedPorts(8081)
        .withRestTemplate(getRestTemplateSsl());
    // @formatter:on
    bookApi.addFileSystemBind(tmpDir + File.separator + "book-api-keystore.jks",
        "/workspace/config/book-api-keystore.jks", BindMode.READ_ONLY);
    bookApi.addFileSystemBind(tmpDir + File.separator + "truststore.ts",
        "/workspace/config/truststore.ts", BindMode.READ_ONLY);
    containers.add(bookApi);

    bookApi.withUsername(USER_ADMIN).withPassword(PASSWORD);

    userId = createUser(USER_TEST, PASSWORD_TEST, List.of("ROLE_BOOK_ADMIN"));
  }

  @Test
  void testHealth() {
    assertTrue(containers.areRunning());
  }

  @Test
  void testPermission() throws JSONException {
    bookApi.withUsername(ROLE_ADMIN).withPassword(PASSWORD);

    var result = bookApi.performGet(PERMISSION_URL);
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    checkRoleAndPermissionList(new JSONArray(result.getBody()));
  }

  @Test
  void testRoleUser() throws JSONException {
    bookApi.withUsername(ROLE_ADMIN).withPassword(PASSWORD);

    var result = bookApi.performGet(ROLE_URL);
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    checkRoleAndPermissionList(new JSONArray(result.getBody()));

    result = bookApi.performGet(PERMISSION_URL);
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    var mapPermission = listToMap(new JSONArray(result.getBody()), NAME);

    var permissions =
        createRefs(mapPermission, List.of("OP_ROLE_READ", "OP_USER_READ", "OP_BOOK_READ"));
    var newRole = new JSONObject();
    newRole.put("permissions", permissions);
    newRole.put(NAME, "newRole");
    newRole.put(DESCRIPTION, "New Role");

    var roleId = bookApi.createAndReturnId(ROLE_URL, newRole.toString());
    result = bookApi.performGet(ROLE_URL + "/" + roleId);
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    var role = new JSONObject(result.getBody());
    checkRole(role, newRole);

    role.put(NAME, "updateRole");
    role.put(DESCRIPTION, "Update Role");
    var voidResult = bookApi.performPut(ROLE_URL + "/" + roleId, role.toString());
    assertThat(voidResult.getStatusCode()).isEqualTo(HttpStatus.OK);
    result = bookApi.performGet(ROLE_URL + "/" + roleId);
    var roleUpdated = new JSONObject(result.getBody());
    checkRole(roleUpdated, role);

    voidResult = bookApi.performDelete(ROLE_URL + "/" + roleId);
    assertThat(voidResult.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

    assertThrows(HttpClientErrorException.class, () -> bookApi.performGet(ROLE_URL + "/" + roleId));
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

  @Test
  void testAuthor() throws JSONException {
    bookApi.withUsername(USER_TEST).withPassword(PASSWORD_TEST);

    var newAuthor = createAuthor();
    var authorId = bookApi.createAndReturnId(AUTHOR_URL, newAuthor.toString());
    var result = bookApi.performGet(AUTHOR_URL + "/" + authorId);
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    var author = new JSONObject(result.getBody());
    checkAuthor(author, newAuthor);

    result = bookApi.performGet(AUTHOR_URL);
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    var authors = new JSONArray(result.getBody());
    checkAuthor(authors.getJSONObject(0), newAuthor);

    author.put(FIRST_NAME, "Updatename");
    author.put(LAST_NAME, "Updatelast");
    var voidResult = bookApi.performPut(AUTHOR_URL + "/" + authorId, author.toString());
    assertThat(voidResult.getStatusCode()).isEqualTo(HttpStatus.OK);
    result = bookApi.performGet(AUTHOR_URL + "/" + authorId);
    var authorUpdated = new JSONObject(result.getBody());
    checkAuthor(authorUpdated, author);

    voidResult = bookApi.performDelete(AUTHOR_URL + "/" + authorId);
    assertThat(voidResult.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

    assertThrows(HttpClientErrorException.class,
        () -> bookApi.performGet(AUTHOR_URL + "/" + authorId));
  }

  @Test
  void testPublisher() throws JSONException {
    bookApi.withUsername(USER_TEST).withPassword(PASSWORD_TEST);

    var newPublisher = createPublisher();
    var publisherId = bookApi.createAndReturnId(PUBLISHER_URL, newPublisher.toString());
    var result = bookApi.performGet(PUBLISHER_URL + "/" + publisherId);
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    var publisher = new JSONObject(result.getBody());
    checkPublisher(publisher, newPublisher);

    result = bookApi.performGet(PUBLISHER_URL);
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    var publishers = new JSONArray(result.getBody());
    checkPublisher(publishers.getJSONObject(0), newPublisher);

    publisher.put(PUBLISHER_NAME, "Update Publisher Name");
    var voidResult = bookApi.performPut(PUBLISHER_URL + "/" + publisherId, publisher.toString());
    assertThat(voidResult.getStatusCode()).isEqualTo(HttpStatus.OK);
    result = bookApi.performGet(PUBLISHER_URL + "/" + publisherId);
    var publisherUpdated = new JSONObject(result.getBody());
    checkPublisher(publisherUpdated, publisher);

    voidResult = bookApi.performDelete(PUBLISHER_URL + "/" + publisherId);
    assertThat(voidResult.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

    assertThrows(HttpClientErrorException.class,
        () -> bookApi.performGet(PUBLISHER_URL + "/" + publisherId));
  }

  @Test
  void testTitle() throws JSONException {
    bookApi.withUsername(USER_TEST).withPassword(PASSWORD_TEST);

    var authorId = bookApi.createAndReturnId(AUTHOR_URL, createAuthor().toString());
    var publisherId = bookApi.createAndReturnId(PUBLISHER_URL, createPublisher().toString());

    var newTitle = createTitle();
    newTitle.put("publisher", createRef(publisherId));
    newTitle.put("authors", createRefs(List.of(authorId)));

    var titleId = bookApi.createAndReturnId(TITLE_URL, newTitle.toString());
    var result = bookApi.performGet(TITLE_URL + "/" + titleId);
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    var title = new JSONObject(result.getBody());
    checkTitle(title, newTitle);

    result = bookApi.performGet(TITLE_URL);
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    var titles = new JSONArray(result.getBody());
    var titleItem = titles.getJSONObject(0);
    checkTitle(titleItem, title);

    title.put(NAME, "Title Name Update");
    title.put(EDITION_NUMBER, 20L);
    title.put(LANGUAGE, "ITALIAN");
    title.put(COPYRIGHT, 2001);
    title.put(IMAGE_FILE, "image file update");
    title.put(PRICE, 60);
    var voidResult = bookApi.performPut(TITLE_URL + "/" + titleId, title.toString());
    assertThat(voidResult.getStatusCode()).isEqualTo(HttpStatus.OK);
    result = bookApi.performGet(TITLE_URL + "/" + titleId);
    var titleUpdated = new JSONObject(result.getBody());
    checkTitle(titleUpdated, title);

    voidResult = bookApi.performDelete(TITLE_URL + "/" + titleId);
    assertThat(voidResult.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

    assertThrows(HttpClientErrorException.class,
        () -> bookApi.performGet(TITLE_URL + "/" + titleId));
  }

  @AfterAll
  static void endAll() {
    bookApi.withUsername(USER_ADMIN).withPassword(PASSWORD);

    var voidResult = bookApi.performDelete("/api/v1/users/" + userId);
    assertThat(voidResult.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

    containers.stopAndCloseAll();
    containerGenerator.endUseSsl();
  }
}
