package org.nomin.integration

import org.nomin.core.Nomin
import org.junit.Test
import org.nomin.mappings.AutoPerson2Employee
import org.nomin.entity.Person
import org.nomin.entity.Employee

/**
 * Tests automapping facility.
 * @author Dmitry Dobrynin
 * Created 21.05.2010 13:57:03
 */
class AutomappingTest {
  Nomin nomin = new Nomin(AutoPerson2Employee)

  @Test
  void testAutomap() {
    Person p = new Person(name: "Automapped Name", lastName: "Last name")
    Employee e = nomin.map(p, Employee)
    assert e && e.name == "Automapped Name" && !e.last
  }

  @Test
  void testNominAutomappingFacility() {
    nomin.enableAutomapping();
    Person p = new Person(name: "Automapped Name", lastName: "Last name")
    Employee e = nomin.map(p, Employee)
    assert e && e.name == "Automapped Name" && !e.last
  }
}
