package org.nomin.integration

import org.nomin.core.Nomin
import org.nomin.mappings.ObjectAndGroovyMethods
import org.junit.Test
import org.nomin.entity.*

/**
 * Tests a mapping.
 * @author Dmitry Dobrynin
 * Date: 09.10.2010 Time: 11:54:54
 */
class ObjectAndGroovyMethodsTest {
  Nomin nomin = new Nomin(ObjectAndGroovyMethods)

  @Test
  void test() {
    Employee e = new Employee(properties: "Employee's properties")
    Person p = nomin.map(e, Person)
    assert p.strDate == "Employee's properties"
    assert p.name == String.valueOf(e.hashCode())
    assert p.lastName == e.toString()
    assert p.options["oppositeClass"] == Employee.class.toString()

    p.strDate = "Person's strDate"
    e = nomin.map(p, Employee)
    assert e.properties == "Person's strDate"
  }
}
