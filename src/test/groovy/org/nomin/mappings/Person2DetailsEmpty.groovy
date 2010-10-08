package org.nomin.mappings

import org.nomin.Mapping
import org.nomin.entity.Details
import org.nomin.entity.Person

/**
 * Just a mapping.
 * @author Dmitry Dobrynin
 * Created: 19.05.2010 23:10:08
 */
class Person2DetailsEmpty extends Mapping {
  protected void build() {
    mappingFor a: Person, b: Details, case: "empty"
    b.birth = new Date(0)
    b.sex = false
  }
}
