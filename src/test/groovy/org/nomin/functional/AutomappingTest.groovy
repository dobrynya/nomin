package org.nomin.functional

import org.nomin.core.Nomin
import org.nomin.entity.Employee
import org.nomin.entity.Person
import org.nomin.mappings.AutoPerson2Employee

/**
 * Tests automapping facility.
 * @author Dmitry Dobrynin
 * Created 21.05.2010 13:57:03
 */
class AutomappingTest {
  Nomin nomin = new Nomin(AutoPerson2Employee)

  @org.junit.Test
  void testAutomap() {
    Person p = new Person(name: "Automapped Name", lastName: "Last name")
    Employee e = nomin.map(p, Employee)
    assert e && e.name == "Automapped Name" && !e.last
  }

  @org.junit.Test
  void testNominAutomappingFacility() {
    nomin.enableAutomapping();
    Person p = new Person(name: "Automapped Name", lastName: "Last name")
    Employee e = nomin.map(p, Employee)
    assert e && e.name == "Automapped Name" && !e.last
  }
}
