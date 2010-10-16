package org.nomin.functional

import org.nomin.core.Nomin
import org.nomin.entity.Child
import org.nomin.entity.Gender
import org.nomin.entity.Person
import org.nomin.mappings.CleaningPerson2Person

/**
 * Tests cloning with cleaning particular properties.
 * @author Dmitry Dobrynin
 * Created 28.05.2010 13:07:58
 */
class CleaningMappingTest {
  Nomin nomin = new Nomin(CleaningPerson2Person)

  @org.junit.Test
  void testCleaning() {
    Person p = nomin.map(new Person(name: "Name", lastName: "Last", gender: Gender.MALE, birthDate: new Date(),
            children: [new Child(name: "Child")]), Person)
    assert p.name == "Name" && p.lastName == "Last" && !p.gender && !p.birthDate && !p.children
  }
}
