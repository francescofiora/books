package it.francescofiora.books.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.openpojo.reflection.impl.PojoClassFactory;
import com.openpojo.validation.ValidatorBuilder;
import com.openpojo.validation.rule.impl.GetterMustExistRule;
import com.openpojo.validation.rule.impl.SetterMustExistRule;
import com.openpojo.validation.test.impl.GetterTester;
import com.openpojo.validation.test.impl.SetterTester;
import it.francescofiora.books.util.DtoEqualsTester;
import it.francescofiora.books.util.FilterPackageInfo;
import org.junit.jupiter.api.Test;

class DtoTest {
  // Configured for expectation, so we know when a class gets added or removed.
  private static final int EXPECTED_CLASS_COUNT = 9;

  // The package to test
  private static final String DTO_PACKAGE = DtoTest.class.getPackage().getName();

  @Test
  void ensureExpectedCount() {
    var classes = PojoClassFactory.getPojoClasses(DTO_PACKAGE, new FilterPackageInfo());
    assertThat(classes.size()).isEqualTo(EXPECTED_CLASS_COUNT);
  }

  @Test
  void testStructureAndBehavior() {
    // @formatter:off
    var validator = ValidatorBuilder.create()
        .with(new GetterMustExistRule())
        .with(new SetterMustExistRule())
        .with(new SetterTester())
        .with(new GetterTester())
        .with(new DtoEqualsTester()).build();
    // @formatter:on

    validator.validate(DTO_PACKAGE, new FilterPackageInfo());
  }
}
