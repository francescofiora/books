package it.francescofiora.books.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

import com.openpojo.reflection.PojoClass;
import com.openpojo.validation.rule.Rule;
import it.francescofiora.books.service.dto.DtoIdentifier;

/**
 * Dto Equals Tester Rule.
 */
public class DtoEqualsTester implements Rule {

  @Override
  public void evaluate(PojoClass pojoClass) {
    if (pojoClass.isConcrete()) {
      try {
        equalsVerifier(pojoClass.getClazz());
        if (pojoClass.extendz(DtoIdentifier.class)) {
          dtoObjectVerifier(pojoClass.getClazz());
          dtoIdentifierVerifier(pojoClass.getClazz());
        }
      } catch (Exception e) {
        fail(e.getMessage());
      }
    }
  }

  private <T> void equalsVerifier(Class<T> clazz) throws Exception {
    var dtoObj1 = (Object) clazz.getConstructor().newInstance();
    var dtoObj2 = (Object) clazz.getConstructor().newInstance();
    Object dtoObj3 = null;

    // Test equals
    assertThat(dtoObj1).isEqualTo(dtoObj2);
    assertThat(dtoObj1).isEqualTo(dtoObj1);
    assertThat(dtoObj1).isNotEqualTo(dtoObj3);
    assertThat(dtoObj1).isNotEqualTo(new Object());

    // Test toString
    assertThat(dtoObj1.toString()).isNotNull();

    // Test hashCode
    assertThat(dtoObj1).hasSameHashCodeAs(dtoObj2.hashCode());
  }

  private <T> void dtoObjectVerifier(Class<T> clazz) throws Exception {
    Object dtoObj1 = TestUtils.createNewDtoIdentifier(clazz, 1L);
    Object dtoObj2 = null;
    assertThat(dtoObj1).isNotEqualTo(dtoObj2);
    assertThat(dtoObj1).isNotEqualTo(new Object());
  }

  private <T> void dtoIdentifierVerifier(Class<T> clazz) throws Exception {
    var dtoObj1 = TestUtils.createNewDtoIdentifier(clazz, 1L);
    var dtoObj2 = TestUtils.createNewDtoIdentifier(clazz, null);

    assertThat(dtoObj1).isNotEqualTo(dtoObj2);
    TestUtils.checkNotEqualHashAndToString(dtoObj2, dtoObj1);

    dtoObj2 = TestUtils.createNewDtoIdentifier(clazz, 2L);
    TestUtils.checkNotEqualHashAndToString(dtoObj1, dtoObj2);

    dtoObj2 = TestUtils.createNewDtoIdentifier(clazz, 1L);
    TestUtils.checkEqualHashAndToString(dtoObj1, dtoObj2);
  }
}
