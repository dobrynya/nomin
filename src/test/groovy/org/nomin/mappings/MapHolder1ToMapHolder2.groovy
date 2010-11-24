package org.nomin.mappings

import org.nomin.Mapping
import org.nomin.entity.*

/**
 * Just a mapping.
 * @author Dmitry Dobrynin
 * Date: 08.11.2010 Time: 17:59:57
 */
class MapHolder1ToMapHolder2 extends Mapping {
  protected void build() {
    mappingFor a: MapHolder1, b: MapHolder2
    a.strings = b.integers
    a.persons = b.employees
    a.objects = b.objects
    hint a: Map[{ it.matches("\\d+") ? Integer : String }, { DetailedPerson }],
            b: Map[{ it.matches("\\d+") ? Integer : String }, { LinearManager }]
    a.persons2 = b.employees
    convert to_a: { e -> [e.key.toString(), e.value.toString()] },
            to_b: { e -> [Integer.valueOf(e.key), new Employee(name: e.value)] }
  }
}
