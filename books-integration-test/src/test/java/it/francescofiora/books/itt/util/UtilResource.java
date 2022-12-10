package it.francescofiora.books.itt.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Assertions;

/**
 * Util Resource.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UtilResource {

  /**
   * Get a path of a resource by file name.
   *
   * @param resourceName file name
   * @return path of the file name
   */
  public static String getResourceFile(final String resourceName) {
    final var classLoader = UtilResource.class.getClassLoader();
    var url = classLoader.getResource(resourceName);
    return url.getFile();
  }

  /**
   * Load file.
   *
   * @param resourceName file name
   * @return the content of the file
   */
  public static String loadFile(final String resourceName) {
    var fileName = Path.of(getResourceFile(resourceName));
    try {
      return Files.readString(fileName);
    } catch (IOException e) {
      e.printStackTrace();
      Assertions.fail();
      return "";
    }
  }
}
