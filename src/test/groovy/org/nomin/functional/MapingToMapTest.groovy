package org.nomin.functional

import java.util.HashMap
import org.nomin.core.Nomin
import org.nomin.mappings.*
import org.nomin.entity.*

/**
 * Tests mapping to a map.
 */
class MapingToMapTest {
  def nomin = new Nomin(Animal2Map)

  def animal = new Animal(name: '42')

  @org.junit.Test
  void testAnimal2Map() {
    Map map = nomin.map(animal, HashMap)
    assert map?.size() == 1 && map['name'] == 42
  }

}
