package it.francescofiora.books.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Publisher Entity.
 */
@Getter
@Setter
@Entity
@ToString
@Table(name = "publisher")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Publisher implements DomainIdentifier, Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "publisher_name")
  private String publisherName;

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
