package org.nomin.mappings

import org.nomin.Mapping

import org.nomin.entity.Kid
import org.nomin.entity.Child

/**
 * Just a mapping.
 * @author Dmitry Dobrynin
 * Created 16.04.2010 18:58:51
 */
class Child2Kid extends Mapping {
  protected void build() {
    mappingFor a: Child, b: Kid
//    introspector asm
    a.name = b.kidName
  }
}
