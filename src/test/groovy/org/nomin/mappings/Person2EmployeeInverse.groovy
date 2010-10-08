package org.nomin.mappings

import org.nomin.Mapping
import org.nomin.entity.Employee
import org.nomin.entity.Person

/**
 * Just a mapping.
 * @author Dmitry Dobrynin
 * Created: 19.05.2010 21:44:28
 */
class Person2EmployeeInverse extends Mapping {
  protected void build() {
    mappingFor a: Person, b: Employee, case: "inverse"
    a.name = b.last
    a.lastName = b.name
    a = b.details
    mappingCase { decision() }
  }
}