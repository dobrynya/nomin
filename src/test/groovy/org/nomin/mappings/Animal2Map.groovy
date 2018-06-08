package org.nomin.mappings

import org.nomin.Mapping
import org.nomin.entity.Animal

/**
 * Mapping between an animal and a simple map.
 */
class Animal2Map extends Mapping {
  protected void build() {
    mappingFor a: java.util.Map, b: Animal
    b.name = a['name']
    hint a: Integer
  }
}