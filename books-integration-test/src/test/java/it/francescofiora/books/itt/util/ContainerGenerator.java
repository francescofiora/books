package it.francescofiora.books.itt.util;

import it.francescofiora.books.itt.container.SpringAplicationContainer;
import it.francescofiora.books.itt.ssl.CertificateGenerator;
import java.io.File;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.Network;

/**
 * Container Generator.
 */
@RequiredArgsConstructor
public class ContainerGenerator {

  private Network network = Network.newNetwork();
  private CertificateGenerator generator;
  private final boolean useSsl;

  public static final String ETC_EXTRA = File.separator + "etcextra";

  public static final String MYSQL_USER_ADMIN = "japp";
  public static final String MYSQL_PASSWORD_ADMIN = "secret";

  public static final String BOOK_MYSQL = "book-mysql";
  public static final String BOOK_API = "book-api";

  @Getter
  private String tmpDir = "";

  /**
   * Use Ssl.
   */
  public void useSsl() {
    if (!useSsl) {
      throw new RuntimeException("useSsl is false");
    }

    tmpDir = UtilResource.getResourceFile("ssl");
    generator = new CertificateGenerator(tmpDir, "ca.francescofiora.it");
    generator.clean();
    generator.generateRoot();

    generator.generateSignedCertificate(BOOK_MYSQL, "01", false);
    generator.generateSignedCertificate(BOOK_API, "02", true);
  }

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
    if (useSsl) {
      mysql.withUrlParam("useSSL", "true");
      mysql.addFileSystemBind(UtilResource.getResourceFile("mysqld.cnf"),
          "/etc/mysql/conf.d/mysqld.cnf", BindMode.READ_ONLY);
      mysql.addFileSystemBind(tmpDir, "/etc/certs", BindMode.READ_ONLY);
    }

    return mysql;
  }

  public SpringAplicationContainer createSpringAplicationContainer(String dockerImageName) {
    return new SpringAplicationContainer(dockerImageName).withNetwork(network);
  }

  public void endUseSsl() {
    generator.clean();
  }

}
