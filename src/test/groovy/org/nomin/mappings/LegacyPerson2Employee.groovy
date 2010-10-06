package org.nomin.mappings

import org.nomin.Mapping

import org.nomin.core.Array
import org.nomin.entity.LegacyDetails
import org.nomin.entity.Kid
import org.nomin.entity.LegacyPerson
import org.nomin.entity.Employee

/**
 * Mapping between classes.
 * @author Dmitry Dobrynin
 * Created 15.04.2010 12:42:39
 */
class LegacyPerson2Employee extends Mapping {
  protected void build() {
    mappingFor a: LegacyPerson, b: Employee
    a.name = b.name
    hint a: String
    a.lastName = b.last
    hint a: String
    a.details.birthday = b.details.birth
    hint a: LegacyDetails
    a.details.sex = b.details.sex
    hint a: LegacyDetails
    a.details.children = b.details.kids
    hint a: [LegacyDetails, Array[Kid]]
  }
}
