package org.nomin.mappings

import org.nomin.Mapping
import org.nomin.entity.Employee
import org.nomin.entity.Person

/**
 * Just a mapping.
 * @auther Dmitry Dobrynin
 * Date: 18.07.2010 time: 13:06:56
 */

class Invalid3 extends Mapping {
  protected void build() {
    mappingFor a: Person, b: Employee
    a.children = b.details
  }
}