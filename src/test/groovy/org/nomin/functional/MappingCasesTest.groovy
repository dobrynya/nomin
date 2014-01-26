package org.nomin.functional

import org.nomin.context.MapContext
import org.nomin.core.Nomin
import org.nomin.entity.*
import org.nomin.mappings.*
import org.nomin.NominMapper

/**
 * Tests mapping with using mapping cases identifiers.
 * @author Dmitry Dobrynin
 * Created 18.05.2010 19:13:27
 */
class MappingCasesTest {
  String mappingCase = null;
  NominMapper mapper = new Nomin(new MapContext(decision: { mappingCase }),
          Person2Employee, Person2EmployeeInverse, Person2Details, Person2DetailsEmpty).disableCache()

  @org.junit.Test
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
