package org.nomin.functional

import java.util.HashMap
import org.nomin.Mapping
import org.nomin.entity.Animal
import org.nomin.entity.Person
import org.nomin.core.Nomin

/**
 * Tests mapping to a map.
 */
class MapingToMapTest {

  def nomin = new Nomin(Animal2Map)

  def animal = new Animal(name: '42')

  def map = ['name': 42]

  @org.junit.Test
  void testAnimal2Map() {
    Map map = nomin.map(animal, HashMap)
    assert map?.size() == 1 && map['name'] == 42
  }

  @org.junit.Test
  void testMap2Animal() {
    Animal animal = nomin.map(map, Animal)
    assert animal?.name == '42'
  }
}

class Animal2Map extends Mapping {
  protected void build() {
      mappingFor a: java.util.Map, b: Animal
      b.name = a['name']
      hint a: Integer
  }
}
