package it.francescofiora.books.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Author Entity.
 */
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

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getFirstName() {
    return firstName;
  }

  public Author firstName(String firstName) {
    this.firstName = firstName;
    return this;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public Author lastName(String lastName) {
    this.lastName = lastName;
    return this;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public Set<Title> getTitles() {
    return titles;
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

  public void setTitles(Set<Title> titles) {
    this.titles = titles;
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
    return 31;
  }

  @Override
  public String toString() {
    return "Author{" + "id=" + getId() + ", firstName='" + getFirstName() + "'" + ", lastName='"
        + getLastName() + "'" + "}";
  }
}
