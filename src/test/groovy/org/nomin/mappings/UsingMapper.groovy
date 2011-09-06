package org.nomin.mappings

import org.nomin.Mapping
import org.nomin.entity.Child
import org.nomin.entity.Employee
import org.nomin.entity.Kid
import org.nomin.entity.Person

/**
 * Just a mapping.
 * @author Dmitry Dobrynin
 * Date: 07.10.2010 Time: 21:06:05
 */
class UsingMapper extends Mapping {
  protected void build() {
    mappingFor a: Person, b: Employee
    a.children[0] = { mapper.map(b.details.kids.iterator().next(), Child) }
    a.children[1] = mapper.map(new Kid("The Second"), Child)
  }
}
