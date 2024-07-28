package it.francescofiora.books.itt.util;

import lombok.RequiredArgsConstructor;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.Network;

/**
 * Container Generator.
 */
@RequiredArgsConstructor
public class ContainerGenerator {

  private final Network network = Network.newNetwork();

  public static final String MYSQL_USER_ADMIN = "japp";
  public static final String MYSQL_PASSWORD_ADMIN = "secret";

  public static final String BOOK_MYSQL = "book-mysql";

  /**
   * Create MySql Container.
   *
   * @return MySQLContainer
   */
  public MySQLContainer<?> createMySqlContainer() {
    // @formatter:off
    return new MySQLContainer<>("mysql:8.4.1")
        .withNetwork(network)
        .withNetworkAliases(BOOK_MYSQL)
        .withUsername(MYSQL_USER_ADMIN).withPassword(MYSQL_PASSWORD_ADMIN)
        .withDatabaseName("books");
    // @formatter:on
  }

  public GenericContainer<?> createSpringAplicationContainer(String dockerImageName) {
    return new GenericContainer<>(dockerImageName).withNetwork(network);
  }
}
