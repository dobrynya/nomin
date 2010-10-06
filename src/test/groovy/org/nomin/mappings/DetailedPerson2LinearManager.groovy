package org.nomin.mappings

import org.nomin.Mapping
import org.nomin.entity.DetailedPerson
import org.nomin.entity.LinearManager

/**
 * Just a mapping.
 * @author Dmitry Dobrynin
 * Created 14.04.2010 16:34:33
 */
class DetailedPerson2LinearManager extends Mapping {
  protected void build() {
    mappingFor a: DetailedPerson, b: LinearManager
//    introspector asm
    a.description = b.characteristics
    a.educationName = b.details.educations[0].name
    a.educationDescription = b.details.educations[0].description
  }
}
