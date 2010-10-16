package org.nomin.functional

import org.nomin.NominMapper
import org.nomin.core.Nomin
import org.nomin.mappings.Child2Kid
import org.nomin.mappings.Person2Details
import org.nomin.mappings.Person2EmployeeAndDetails
import org.nomin.entity.*

/**
 * Tests mapping root instances: b.details = a and a = b.details.
 * @author Dmitry Dobrynin
 * Created: 02.05.2010 15:38:23
 */
class MappingRootsTest {
  NominMapper mapper = new Nomin()
  Person person = new Person(birthDate: new Date(), gender: Gender.MALE, children: [new Child(name: "Andrew")])
  Employee employee = new Employee(details: new Details(birth: new Date(), sex: true, kids: [new Kid(kidName: "Ariel")]))

  @org.junit.Before
  void before() {
    mapper.parse(Person2EmployeeAndDetails, Person2Details, Child2Kid)
  }

  @org.junit.Test
  void testDirectRootMapping() {
    Employee e = mapper.map(person, Employee)
    assert e && e.details
    assert e.details.birth == person.birthDate
    assert e.details.sex == (person.gender == Gender.MALE)
    assert e.details.kids && e.details.kids.size() == 1
    Kid kid = e.details.kids.iterator().next()
    assert kid.kidName == "Andrew"
  }

  @org.junit.Test
  void testReverseRootMapping() {
    Person p = mapper.map(employee, Person)
    assert p && p.birthDate == employee.details.birth
    assert p.gender == (employee.details.sex ? Gender.MALE : Gender.FEMALE)
    assert p.children && p.children.size() == 1 && p.children[0] instanceof Child && p.children[0].name == "Ariel"
  }
}
