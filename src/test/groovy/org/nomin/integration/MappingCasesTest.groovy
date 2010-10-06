package org.nomin.integration

import org.junit.Test
import org.nomin.core.Nomin
import org.junit.Before
import org.nomin.mappings.Person2EmployeeInverse
import org.nomin.mappings.Person2Employee
import org.nomin.entity.Person
import org.nomin.entity.Employee
import org.nomin.mappings.Person2Details
import org.nomin.mappings.Person2DetailsEmpty
import org.nomin.entity.Gender

/**
 * Tests mapping with using mapping cases identifiers.
 * @author Dmitry Dobrynin
 * Created 18.05.2010 19:13:27
 */
class MappingCasesTest {
  String mappingCase = null;
  def decision = { mappingCase }
  Nomin mapper = new Nomin([decision: decision])

  @Before
  void before() { mapper.parse Person2Employee, Person2EmployeeInverse, Person2Details, Person2DetailsEmpty }

  @Test
  void test() {
    def p = new Person(name: "Name", lastName: "Last name", birthDate: new Date(), gender: Gender.MALE)
    Employee e = mapper.map(p, Employee)
    assert e && e.name == p.name && e.last == p.lastName && e.details.birth == p.birthDate && e.details.sex
    e = mapper.map(p, Employee, "inverse")
    assert e && e.name == p.lastName && e.last == p.name && e.details.birth == p.birthDate && e.details.sex
    mappingCase = "empty"
    e = mapper.map(p, Employee, "inverse")
    assert e && e.name == p.lastName && e.last == p.name && e.details.birth == new Date(0) && !e.details.sex
  }
}
