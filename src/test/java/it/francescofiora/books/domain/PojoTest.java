package it.francescofiora.books.domain;

import com.openpojo.reflection.PojoClass;
import com.openpojo.reflection.impl.PojoClassFactory;
import com.openpojo.validation.Validator;
import com.openpojo.validation.ValidatorBuilder;
import com.openpojo.validation.affirm.Affirm;
import com.openpojo.validation.rule.impl.GetterMustExistRule;
import com.openpojo.validation.rule.impl.SetterMustExistRule;
import com.openpojo.validation.test.impl.GetterTester;
import com.openpojo.validation.test.impl.SetterTester;
import it.francescofiora.books.util.FilterPackageInfo;
import it.francescofiora.books.util.PojoEqualsTester;
import java.util.List;
import org.junit.jupiter.api.Test;

class PojoTest {
  // Configured for expectation, so we know when a class gets added or removed.
  private static final int EXPECTED_CLASS_COUNT = 3;

  // The package to test
  private static final String POJO_PACKAGE = "it.francescofiora.books.domain";

  @Test
  void ensureExpectedPojoCount() {
    List<PojoClass> pojoClasses =
        PojoClassFactory.getPojoClasses(POJO_PACKAGE, new FilterPackageInfo());
    Affirm.affirmEquals("Classes added / removed?", EXPECTED_CLASS_COUNT, pojoClasses.size());
  }

  @Test
  void testPojoStructureAndBehavior() {
    // @formatter:off
    Validator validator = ValidatorBuilder.create()
        .with(new GetterMustExistRule())
        .with(new SetterMustExistRule())
        .with(new SetterTester())
        .with(new GetterTester())
        .with(new PojoEqualsTester()).build();
    // @formatter:on

    validator.validate(POJO_PACKAGE, new FilterPackageInfo());
  }
}
