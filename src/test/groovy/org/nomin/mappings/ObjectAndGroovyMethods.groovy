package org.nomin.mappings

import org.nomin.Mapping
import org.nomin.entity.*

/**
 * A mapping showing mapping rules using methods of Object and special Groovy methods.
 * @author Dmitry Dobrynin
 * Date: 09.10.2010 Time: 11:48:59
 */
class ObjectAndGroovyMethods extends Mapping {
  protected void build() {
    mappingFor a: Person, b: Employee
    a.name = b."hashCode()"
    a.lastName = b."toString()"
    a.options["oppositeClass"] = b."getClass()"."toString()"
    a.strDate = b.properties
  }
}
