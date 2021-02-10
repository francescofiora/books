package it.francescofiora.books.domain;

import it.francescofiora.books.domain.enumeration.Language;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Title Entity.
 */
@Getter
@Setter
@Entity
@Table(name = "title")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Title implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "title")
  private String title;

  @Column(name = "edition_number")
  private Long editionNumber;

  @Enumerated(EnumType.STRING)
  @Column(name = "language")
  private Language language;

  @Column(name = "copyright")
  private Integer copyright;

  @Column(name = "image_file")
  private String imageFile;

  @Column(name = "price")
  private Long price;

  @OneToOne
  @JoinColumn(unique = true)
  private Publisher publisher;

  @ManyToMany
  @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
  @JoinTable(
      name = "title_author",
      joinColumns = @JoinColumn(name = "title_id", referencedColumnName = "id"),
      inverseJoinColumns = @JoinColumn(name = "author_id", referencedColumnName = "id"))
  private Set<Author> authors = new HashSet<>();

  public Title title(String title) {
    this.title = title;
    return this;
  }

  public Title editionNumber(Long editionNumber) {
    this.editionNumber = editionNumber;
    return this;
  }

  public Title language(Language language) {
    this.language = language;
    return this;
  }

  public Title copyright(Integer copyright) {
    this.copyright = copyright;
    return this;
  }

  public Title imageFile(String imageFile) {
    this.imageFile = imageFile;
    return this;
  }

  public Title price(Long price) {
    this.price = price;
    return this;
  }

  public Title publisher(Publisher publisher) {
    this.publisher = publisher;
    return this;
  }

  public Title authors(Set<Author> authors) {
    this.authors = authors;
    return this;
  }

  /**
   * add a new Author.
   * @param author Author
   * @return Author
   */
  public Title addAuthor(Author author) {
    this.authors.add(author);
    author.getTitles().add(this);
    return this;
  }

  /**
   * remove an Author.
   * @param author Author
   * @return Author
   */
  public Title removeAuthor(Author author) {
    this.authors.remove(author);
    author.getTitles().remove(this);
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Title)) {
      return false;
    }
    return id != null && id.equals(((Title) o).id);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    return result;
  }

  @Override
  public String toString() {
    return "Title{" + "id=" + getId() + ", title='" + getTitle() + "'" + ", editionNumber="
        + getEditionNumber() + ", language='" + getLanguage() + "'" + ", copyright="
        + getCopyright() + ", imageFile='" + getImageFile() + "'" + ", price=" + getPrice() + "}";
  }
}
