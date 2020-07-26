package it.francescofiora.books.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Publisher Entity.
 */
@Entity
@Table(name = "publisher")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Publisher implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "publisher_name")
  private String publisherName;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getPublisherName() {
    return publisherName;
  }

  public Publisher publisherName(String publisherName) {
    this.publisherName = publisherName;
    return this;
  }

  public void setPublisherName(String publisherName) {
    this.publisherName = publisherName;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Publisher)) {
      return false;
    }
    return id != null && id.equals(((Publisher) o).id);
  }

  @Override
  public int hashCode() {
    return 31;
  }

  @Override
  public String toString() {
    return "Publisher{" + "id=" + getId() + ", publisherName='" + getPublisherName() + "'" + "}";
  }
}
