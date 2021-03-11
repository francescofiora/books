package it.francescofiora.books.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Author Entity.
 */
@Getter
@Setter
@Entity
@Table(name = "author")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Author implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "first_name")
  private String firstName;

  @Column(name = "last_name")
  private String lastName;

  @ManyToMany(mappedBy = "authors")
  @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
  private Set<Title> titles = new HashSet<>();

  public Author firstName(String firstName) {
    this.firstName = firstName;
    return this;
  }

  public Author lastName(String lastName) {
    this.lastName = lastName;
    return this;
  }

  public Author titles(Set<Title> titles) {
    this.titles = titles;
    return this;
  }

  /**
   * add new Title.
   * @param title Title
   * @return Author
   */
  public Author addTitle(Title title) {
    this.titles.add(title);
    title.getAuthors().add(this);
    return this;
  }

  /**
   * remove a Title.
   * @param title Title
   * @return Author
   */
  public Author removeTitle(Title title) {
    this.titles.remove(title);
    title.getAuthors().remove(this);
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Author)) {
      return false;
    }
    return id != null && id.equals(((Author) o).id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }

  @Override
  public String toString() {
    return "Author{" + "id=" + getId() + ", firstName='" + getFirstName() + "'" + ", lastName='"
        + getLastName() + "'" + "}";
  }
}
