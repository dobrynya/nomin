package org.nomin.mappings

import org.nomin.Mapping
import org.nomin.entity.Employee
import org.nomin.entity.Gender
import org.nomin.entity.Person

/**
 * Just a mapping.
 * @author Dmitry Dobrynin
 * Created 14.04.2010 16:34:43
 */
class Person2Employee extends Mapping {
  void build() {
    mappingFor a: Person, b: Employee
//    introspector asm
    a.name = b.name
    a.lastName = b.last
    a.birthDate = b.details.birth
    a.children = b.details.kids
    a.strDate = b.details.birth
    dateFormat "dd-MM-yyyy"

    a.gender = b.details.sex
    simple([Gender.MALE, true], [Gender.FEMALE, false])
  }
}
