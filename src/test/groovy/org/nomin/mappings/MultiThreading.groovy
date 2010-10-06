package org.nomin.mappings

import org.nomin.Mapping
import org.nomin.entity.*

/**
 * Just a mapping.
 * @author Dmitry Dobrynin
 * Date: 30.08.2010 time: 18:26:13
 */
class MultiThreading extends Mapping {
  protected void build() {
    mappingFor a: Person, b: Employee
    b.name = { key }
  }
}
