package org.nomin.mappings

import org.nomin.Mapping
import org.nomin.entity.Person

/**
 * Just a mapping.
 * @author Dmitry Dobrynin
 * Created 28.05.2010 12:42:55
 */
class CleaningPerson2Person extends Mapping {
  protected void build() {
    mappingFor a: Person, b: Person
    automap()
    mapNulls = true
    b.birthDate = null
    b.children = null
    b.gender = null
  }
}
