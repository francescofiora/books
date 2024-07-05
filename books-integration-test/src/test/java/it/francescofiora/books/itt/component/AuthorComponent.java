package it.francescofiora.books.itt.component;

import static org.assertj.core.api.Assertions.assertThat;

import it.francescofiora.books.itt.context.AuthorContext;
import it.francescofiora.books.itt.util.JsonException;
import it.francescofiora.books.itt.util.UtilTests;
import lombok.RequiredArgsConstructor;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;

/**
 * Author Component.
 */
@RequiredArgsConstructor
public class AuthorComponent {

  private final BookApiService bookApiService;
  private final AuthorContext authorContext;

  /**
   * Find Authors.
   */
  public void findAuthors() {
    var resultAuthors = bookApiService.findAuthors();
    assertThat(resultAuthors.getStatusCode()).isEqualTo(HttpStatus.OK);
    authorContext.setResultAuthors(resultAuthors);
  }

  /**
   * Create Author.
   */
  public void createAuthor() {
    var author = UtilTests.createAuthor();
    authorContext.setNewAuthor(author);
    var authorId = bookApiService.createAuthor(author.toString());
    authorContext.setAuthorId(authorId);
  }

  /**
   * Update Author.
   */
  public void updateAuthor() {
    UtilTests.updateAuthor(authorContext.getNewAuthor());
    var resultVoid = bookApiService.updateAuthor(
        authorContext.getAuthorId(), authorContext.getNewAuthor().toString());
    assertThat(resultVoid.getStatusCode()).isEqualTo(HttpStatus.OK);
  }

  /**
   * Get Author By Id.
   */
  public void getAuthorById() {
    var resultAuthor = bookApiService.getAuthorById(authorContext.getAuthorId());
    assertThat(resultAuthor.getStatusCode()).isEqualTo(HttpStatus.OK);
    try {
      authorContext.setAuthor(new JSONObject(resultAuthor.getBody()));
    } catch (JSONException e) {
      throw new JsonException(e.getMessage());
    }
  }

  public void deleteAuthorById() {
    var result = bookApiService.deleteAuthorById(authorContext.getAuthorId());
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
  }

  /**
   * Check Author.
   */
  public void checkAuthor() {
    var author = authorContext.getAuthor();
    var newAuthor = authorContext.getNewAuthor();
    try {
      assertThat(author.getString(UtilTests.FIRST_NAME))
          .isEqualTo(newAuthor.getString(UtilTests.FIRST_NAME));
      assertThat(author.getString(UtilTests.LAST_NAME))
          .isEqualTo(newAuthor.getString(UtilTests.LAST_NAME));
    } catch (JSONException e) {
      throw new RuntimeException(e);
    }
    authorContext.setNewAuthor(author);
  }
}
