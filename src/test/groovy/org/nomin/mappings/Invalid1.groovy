package org.nomin.mappings

import org.nomin.Mapping
import org.nomin.entity.*

/**
 * Just a mapping.
 * @auther Dmitry Dobrynin
 * Date: 18.07.2010 time: 13:06:56
 */
class Invalid1 extends Mapping {
  protected void build() {
    mappingFor a: Person, b: Employee
    a.missingProperty = b.missingProperty
  }
}
