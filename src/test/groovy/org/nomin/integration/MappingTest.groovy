package org.nomin.integration

import java.text.SimpleDateFormat
import org.nomin.NominMapper
import org.nomin.core.Nomin
import org.nomin.mappings.Child2Kid
import org.nomin.mappings.DetailedPerson2LinearManager
import org.nomin.mappings.Person2Employee
import org.nomin.entity.*

/**
 * Tests mapping a DetailedPerson instance to a LinearManager instance and vice versa.
 * @author Dmitry Dobrynin
 * Created: 02.05.2010 16:25:37
 */
class MappingTest {
  NominMapper mapper = new Nomin(Person2Employee, DetailedPerson2LinearManager, Child2Kid)

  DetailedPerson person = new DetailedPerson(name: "James", lastName: "Bond", birthDate: new Date(), gender: Gender.MALE,
          children: [new Child(name: "Andrew")], description: "National Security",
          educationName: "Private", educationDescription: "Private")

  LinearManager employee = new LinearManager(name: "Feoctist", last: "Grecian", characteristics: "Ancient", details: new Details(birth: new Date(),
          sex: true, kids: [new Kid(kidName: "Ariel")], educations: [new Education(name: "High", description: "5 years ago")]))

  @org.junit.Test
  void testDirectMapping() {
    LinearManager m = mapper.map(person, LinearManager)
    assert m && m.name == person.name && m.last == person.lastName && m.details && m.details.birth == person.birthDate
    assert m.characteristics == "National Security"
    assert m.details.sex && m.details.kids.size() == 1
    Kid kid = m.details.kids.iterator().next()
    assert kid.kidName == "Andrew"
    assert m.details.educations && m.details.educations.size() == 1 && m.details.educations[0].name == "Private" && m.details.educations[0].description == "Private"
  }

  @org.junit.Test
  void testReverseMapping() {
    DetailedPerson dp = mapper.map(employee, DetailedPerson)
    assert dp && dp.name == employee.name && dp.lastName == employee.last
    assert dp.birthDate == employee.details.birth && dp.description == "Ancient"
    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy")
    assert sdf.format(employee.details.birth) == dp.strDate
    assert dp.children && dp.children.size() == 1 && dp.children[0] instanceof Child && dp.children[0].name == "Ariel"
    assert dp.educationName == "High" && dp.educationDescription == "5 years ago"
  }
}
