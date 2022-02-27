package it.francescofiora.books.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

import com.openpojo.reflection.PojoClass;
import com.openpojo.validation.rule.Rule;
import it.francescofiora.books.domain.DomainIdentifier;

/**
 * Pojo Equals Tester Rule.
 */
public class PojoEqualsTester implements Rule {

  @Override
  public void evaluate(PojoClass pojoClass) {
    if (pojoClass.isConcrete()) {
      try {
        equalsVerifier(pojoClass.getClazz());
        if (pojoClass.extendz(DomainIdentifier.class)) {
          domainIdentifierVerifier(pojoClass.getClazz());
        }
      } catch (Exception e) {
        fail(e.getMessage());
      }
    }
  }

  private <T> void equalsVerifier(Class<T> clazz) throws Exception {
    var domainObj1 = (Object) clazz.getConstructor().newInstance();
    var domainObj2 = (Object) clazz.getConstructor().newInstance();
    Object domainObj3 = null;

    // Test equals
    assertThat(domainObj1).isNotEqualTo(domainObj2);
    assertThat(domainObj1).isEqualTo(domainObj1);
    assertThat(domainObj1).isNotEqualTo(domainObj3);
    assertThat(domainObj1).isNotEqualTo(new Object());

    // Test toString
    assertThat(domainObj1.toString()).isNotNull();

    // Test hashCode
    assertThat(domainObj1).hasSameHashCodeAs(domainObj2.hashCode());
  }

  private <T> void domainIdentifierVerifier(Class<T> clazz) throws Exception {
    var domainObj1 = TestUtils.createNewDomain(clazz, 1L);
    Object domainObj2 = null;

    assertThat(domainObj1).isNotEqualTo(domainObj2);
    assertThat(domainObj1).isNotEqualTo(new Object());

    var domainObj3 = TestUtils.createNewDomain(clazz, null);
    assertThat(domainObj1).isNotEqualTo(domainObj3);
    TestUtils.checkNotEqualHashAndToString(domainObj3, domainObj1);

    domainObj3 = TestUtils.createNewDomain(clazz, 2L);
    TestUtils.checkNotEqualHashAndToString(domainObj1, domainObj3);

    domainObj3 = TestUtils.createNewDomain(clazz, 1L);
    TestUtils.checkEqualHashAndToString(domainObj1, domainObj3);
  }
}
