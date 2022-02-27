package it.francescofiora.books.service.dto;

import it.francescofiora.books.service.util.DtoUtils;
import java.util.Objects;

/**
 * Ref Dto.
 */
public abstract class RefDto implements DtoIdentifier {

  @Override
  public int hashCode() {
    return Objects.hashCode(getId());
  }

  @Override
  public boolean equals(Object obj) {
    return DtoUtils.equals(this, obj);
  }
}
