package org.nomin.mappings

import org.nomin.Mapping
import org.nomin.entity.Details
import org.nomin.entity.Gender
import org.nomin.entity.Person

/**
 * Just a mapping.
 * @author Dmitry Dobrynin
 * Created 19.04.2010 11:08:46
 */
class Person2Details extends Mapping {
  protected void build() {
    mappingFor a: Person, b: Details
    a.birthDate = b.birth
    a.children = b.kids
    a.strDate = dateFormat("dd-MM-yyyy", b.birth)
    a.gender = b.sex
    simple([Gender.MALE, true], [Gender.FEMALE, false])
  }
}
