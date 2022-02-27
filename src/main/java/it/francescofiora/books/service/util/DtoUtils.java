package it.francescofiora.books.service.util;

import it.francescofiora.books.service.dto.DtoIdentifier;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Dto Utils.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DtoUtils {

  /**
   * Return true if obj1 and obj2 are same reference or they have same id.
   *
   * @param obj1 DtoIdentifier
   * @param obj2 Object
   * @return true if obj1 and obj2 are same reference or they have same id
   */
  public static boolean equals(DtoIdentifier obj1, Object obj2) {
    if (obj1 == obj2) {
      return true;
    }
    if (obj1 == null || obj2 == null || obj1.getClass() != obj2.getClass()) {
      return false;
    }
    return Objects.equals(obj1.getId(), ((DtoIdentifier) obj2).getId());
  }
}
