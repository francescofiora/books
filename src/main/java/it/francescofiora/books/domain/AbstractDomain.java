package it.francescofiora.books.domain;

import java.util.Objects;

/**
 * Abstract Domain Identifier impl.
 */
public abstract class AbstractDomain implements DomainIdentifier {

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (getId() == null || obj == null || getClass() != obj.getClass()) {
      return false;
    }
    return Objects.equals(getId(), ((DomainIdentifier) obj).getId());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getId());
  }
  
}
