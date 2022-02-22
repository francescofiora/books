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
import lombok.ToString;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

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
  @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
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
