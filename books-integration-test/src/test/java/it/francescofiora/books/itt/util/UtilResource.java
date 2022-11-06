package it.francescofiora.books.itt.util;

import java.nio.file.Files;
import java.nio.file.Path;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

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

  public static String fileToString(final String resourceName) throws Exception {
    var fileName = Path.of(getResourceFile(resourceName));
    return Files.readString(fileName);
  }

}
