package it.francescofiora.books.itt.util;

import it.francescofiora.books.itt.container.SpringAplicationContainer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.Network;

/**
 * Container Generator.
 */
@RequiredArgsConstructor
public class ContainerGenerator {

  private Network network = Network.newNetwork();

  public static final String MYSQL_USER_ADMIN = "japp";
  public static final String MYSQL_PASSWORD_ADMIN = "secret";

  public static final String BOOK_MYSQL = "book-mysql";
  public static final String BOOK_API = "book-api";

  @Getter
  private String tmpDir = "";

  /**
   * Create MySql Container.
   *
   * @return MySQLContainer
   */
  public MySQLContainer<?> createMySqlContainer() {
    // @formatter:off
    var mysql = new MySQLContainer<>("mysql:8.0.27")
        .withNetwork(network)
        .withNetworkAliases(BOOK_MYSQL)
        .withUsername(MYSQL_USER_ADMIN).withPassword(MYSQL_PASSWORD_ADMIN)
        .withDatabaseName("books");
    // @formatter:on

    return mysql;
  }

  public SpringAplicationContainer createSpringAplicationContainer(String dockerImageName) {
    return new SpringAplicationContainer(dockerImageName).withNetwork(network);
  }
}
