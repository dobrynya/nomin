package org.nomin.mappings

import org.nomin.Mapping
import org.nomin.entity.Employee
import org.nomin.entity.Person

/**
 * Just a mapping.
 * @author Dmitry Dobrynin
 * Created 21.05.2010 13:55:51
 */
class AutoPerson2Employee extends Mapping {
  protected void build() {
    mappingFor a: Person, b: Employee
    automap()
  }
}
