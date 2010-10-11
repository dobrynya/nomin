package org.nomin.mappings

import org.nomin.Mapping
import org.nomin.entity.Employee
import org.nomin.entity.Person

/**
 * Just a mapping.
 * @author Dmitry Dobrynin
 * Date: 07.10.2010 Time: 22:28:17
 */
class MappingMapValue extends Mapping {
  protected void build() {
    mappingFor a: Person, b: Employee
    b.name = a.options["name"]
    b.last = a.options["lastName"]
    b.details.kids = a.options.values()
  }
}
