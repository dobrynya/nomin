package org.nomin.util

import org.nomin.entity.Person
import org.nomin.core.MappingConsts

/**
 * Tests ExplodingIntrospector.
 * @author Dmitry Dobrynin
 * Created 21.05.2010 13:15:35
 */
class ExplodingIntrospectorTest implements MappingConsts {
  @org.junit.Test
  void testProperties() {
    assert ["name", "lastName", "birthDate", "gender", "children", "strDate", "options"].containsAll(exploding.properties(Person))
  }
}
