package org.nomin.functional

import org.nomin.core.Nomin
import org.nomin.core.NominException
import org.nomin.mappings.Invalid1
import org.nomin.mappings.Invalid2
import org.nomin.mappings.Invalid3
import org.nomin.mappings.Invalid4

/**
 * Tests invalid mappings.
 * @auther Dmitry Dobrynin
 * Date: 18.07.2010 time: 13:09:25
 */
class InvalidMappingsTest {
  Nomin nomin = new Nomin()

  @org.junit.Test
  void testInvalid1() {
    try {
      nomin.parse Invalid1
      assert false
    } catch (NominException e) {
      assert e.message == "org.nomin.mappings.Invalid1: Mapping rule a.missingProperty = b.missingProperty is invalid because of missing property Person.missingProperty!"
    }
  }

  @org.junit.Test
  void testInvalid2() {
    try {
      nomin.parse Invalid2
      assert false
    } catch (NominException e) {
      assert e.message == "org.nomin.mappings.Invalid2: Mapping rule a.name[0] = b.name is invalid because property name:String isn't indexable!"
    }
  }

  /*@org.junit.Test
  void testInvalid3() {
    try {
      nomin.parse Invalid3
      assert false
    } catch (NominException e) {
      assert e.message == "org.nomin.mappings.Invalid3: Mapping rule a.children = b.details is invalid because there is a collection/array on the first side and a single value on another!"
    }
  }*/

  @org.junit.Test
  void testInvalid4() {
    try {
      nomin.parse Invalid4
      assert false
    } catch (NominException e) {
      assert e.message == "org.nomin.mappings.Invalid4: Recursive mapping rule a = b causes infinite loop!"
    }
  }
}
