package org.nomin.integration

import org.nomin.core.Nomin
import org.nomin.mappings.Zoo2AnimalContainer
import org.nomin.entity.*

/**
 * Tests dynamic hints.
 * @author Dmitry Dobrynin
 * Created: 13.05.2010 0:42:07
 */
class DynamicHintsTest {
  def mapper = new Nomin()

  @org.junit.Before
  void before() { mapper.parse Zoo2AnimalContainer }

  @org.junit.Test
  void test() {
    def c = new AnimalContainer(
            wildAnimals: [new WildAnimal(kind: "Camel"), new WildAnimal(kind: "Crocodile"), new WildAnimal(kind: "Rhino")])
    Zoo z = mapper.map(c, Zoo)
    assert z && z.favorite instanceof Camel && z.animals && z.animals.size() == 3 &&
            z.animals[0] instanceof Camel && z.animals[1] instanceof Crocodile && z.animals[2] instanceof Rhino &&
            z.animalArray?.length == 3 && z.animalArray[0] instanceof Camel && z.animalArray[1] instanceof Crocodile &&
            z.animalArray[2] instanceof Rhino &&
            z.theWorst instanceof Crocodile && z.theWorst.name == "The worst"
  }
}
