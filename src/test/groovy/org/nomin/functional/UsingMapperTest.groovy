package org.nomin.functional

import org.nomin.core.Nomin
import org.nomin.entity.Details
import org.nomin.entity.Employee
import org.nomin.entity.Kid
import org.nomin.entity.Person
import org.nomin.mappings.Child2Kid
import org.nomin.mappings.UsingMapper

/**
 * Tests using mapper in mappings.
 * @author Dmitry Dobrynin
 * Date: 07.10.2010 Time: 21:10:26
 */
class UsingMapperTest {
  Nomin nomin = new Nomin(Child2Kid, UsingMapper)

  @org.junit.Test
  void test() {
    Employee e = new Employee(details: new Details(kids: [new Kid("The First")]))
    Person p = nomin.map(e, Person)
    assert p && p.children && p.children[0]?.name == "The First" && p.children[1]?.name == "The Second"
  }
}
