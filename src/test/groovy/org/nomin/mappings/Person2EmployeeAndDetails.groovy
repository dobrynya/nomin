package org.nomin.mappings

import org.nomin.Mapping

import org.nomin.entity.Employee
import org.nomin.entity.Person

/**
 * Just a mapping.
 * @author Dmitry Dobrynin
 * Created: 02.05.2010 15:29:51
 */
class Person2EmployeeAndDetails extends Mapping {
  protected void build() {
    mappingFor a: Person, b: Employee
    a.name = b.name
    a.lastName = b.last
    a = b.details
  }
}