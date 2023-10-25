package it.francescofiora.books.domain;

import it.francescofiora.books.domain.enumeration.Language;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Title Entity.
 */
@Getter
@Setter
@Entity
@ToString
@Table(name = "title")
public class Title extends AbstractDomain implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column
  private String name;

  @Column(name = "edition_number")
  private Long editionNumber;

  @Enumerated(EnumType.STRING)
  @Column
  private Language language;

  @Column
  private Integer copyright;

  @Column(name = "image_file")
  private String imageFile;

  @Column
  private Long price;

  @OneToOne
  @JoinColumn(unique = true)
  private Publisher publisher;

  @ManyToMany
  @JoinTable(name = "title_author",
      joinColumns = @JoinColumn(name = "title_id", referencedColumnName = "id"),
      inverseJoinColumns = @JoinColumn(name = "author_id", referencedColumnName = "id"))
  private Set<Author> authors = new HashSet<>();

  @Override
  public boolean equals(Object obj) {
    return super.equals(obj);
  }

  @Override
  public int hashCode() {
    return super.hashCode();
  }
}
