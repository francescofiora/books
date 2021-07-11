package it.francescofiora.books.util;

import com.openpojo.reflection.PojoClass;
import com.openpojo.reflection.PojoClassFilter;
import com.openpojo.reflection.java.Java;

/**
 * Filter Package Info for Pojo/Dto test.
 */
public class FilterPackageInfo implements PojoClassFilter {

  @Override
  public boolean include(final PojoClass pojoClass) {
    return !pojoClass.getName().endsWith(Java.PACKAGE_DELIMITER + Java.PACKAGE_INFO)
        && !pojoClass.getName().endsWith("Test") && pojoClass.isConcrete();
  }

  @Override
  public boolean equals(Object o) {
    return this == o || !(o == null || getClass() != o.getClass());
  }

  @Override
  public int hashCode() {
    return this.getClass().hashCode();
  }
}
