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

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Title Entity.
 */
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

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public Title title(String title) {
    this.title = title;
    return this;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public Long getEditionNumber() {
    return editionNumber;
  }

  public Title editionNumber(Long editionNumber) {
    this.editionNumber = editionNumber;
    return this;
  }

  public void setEditionNumber(Long editionNumber) {
    this.editionNumber = editionNumber;
  }

  public Language getLanguage() {
    return language;
  }

  public Title language(Language language) {
    this.language = language;
    return this;
  }

  public void setLanguage(Language language) {
    this.language = language;
  }

  public Integer getCopyright() {
    return copyright;
  }

  public Title copyright(Integer copyright) {
    this.copyright = copyright;
    return this;
  }

  public void setCopyright(Integer copyright) {
    this.copyright = copyright;
  }

  public String getImageFile() {
    return imageFile;
  }

  public Title imageFile(String imageFile) {
    this.imageFile = imageFile;
    return this;
  }

  public void setImageFile(String imageFile) {
    this.imageFile = imageFile;
  }

  public Long getPrice() {
    return price;
  }

  public Title price(Long price) {
    this.price = price;
    return this;
  }

  public void setPrice(Long price) {
    this.price = price;
  }

  public Publisher getPublisher() {
    return publisher;
  }

  public Title publisher(Publisher publisher) {
    this.publisher = publisher;
    return this;
  }

  public void setPublisher(Publisher publisher) {
    this.publisher = publisher;
  }

  public Set<Author> getAuthors() {
    return authors;
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

  public void setAuthors(Set<Author> authors) {
    this.authors = authors;
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
