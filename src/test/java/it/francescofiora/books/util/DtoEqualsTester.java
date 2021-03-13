package it.francescofiora.books.util;

import static org.assertj.core.api.Assertions.assertThat;
import com.openpojo.reflection.PojoClass;
import com.openpojo.validation.rule.Rule;
import it.francescofiora.books.service.dto.DtoIdentifier;
import org.junit.jupiter.api.Assertions;

public class DtoEqualsTester implements Rule {

  @Override
  public void evaluate(PojoClass pojoClass) {
    if (pojoClass.isConcrete()) {
      try {
        equalsVerifier(pojoClass.getClazz());
        if (pojoClass.extendz(DtoIdentifier.class)) {
          dtoIdentifierVerifier(pojoClass.getClazz());
        }
      } catch (Exception e) {
        Assertions.fail(e.getMessage());
      }
    }
  }

  public <T> void equalsVerifier(Class<T> clazz) throws Exception {
    T dtoObj1 = clazz.getConstructor().newInstance();
    T dtoObj2 = clazz.getConstructor().newInstance();

    // Test equals
    assertThat(dtoObj1.equals(dtoObj2)).isTrue();
    assertThat(dtoObj1.equals(dtoObj1)).isTrue();
    assertThat(dtoObj1.equals(null)).isFalse();
    assertThat(dtoObj1.equals(new Object())).isFalse();

    // Test toString
    assertThat(dtoObj1.toString()).isNotNull();

    // Test hashCode
    assertThat(dtoObj1.hashCode()).isEqualTo(dtoObj2.hashCode());
  }

  public <T> void dtoIdentifierVerifier(Class<T> clazz) throws Exception {
    DtoIdentifier domainObj1 = (DtoIdentifier) clazz.getConstructor().newInstance();
    domainObj1.setId(1L);
    assertThat(domainObj1.equals(null)).isFalse();
    assertThat(domainObj1.equals(new Object())).isFalse();

    DtoIdentifier domainObj2 = (DtoIdentifier) clazz.getConstructor().newInstance();
    assertThat(domainObj1.equals(domainObj2)).isFalse();

    domainObj2.setId(2L);
    assertThat(domainObj1.equals(domainObj2)).isFalse();

    domainObj2.setId(domainObj1.getId());
    assertThat(domainObj1.equals(domainObj2)).isTrue();

    domainObj1.setId(null);
    assertThat(domainObj1.equals(domainObj2)).isFalse();
  }
}
