package org.nomin.core

import org.nomin.util.TypeInfo
import org.nomin.util.PropertyAccessor
import static java.text.MessageFormat.format

/**
 * Contains name of the requested property.
 * @author Dmitry Dobrynin
 * Created 09.04.2010 10:45:49
 */
class PropPathElem extends PathElem {
  String propPathElementPropertyName

  RuleElem createMappingRuleElement(TypeInfo typeInfo, RuleElem prev) {
    PropertyAccessor property = pathElementMappingEntry.introspector.property(propPathElementPropertyName, typeInfo.type)
    if (!property)
      throw new NominException(format("{0}: Mapping rule {1} is invalid because of missing property {2}.{3}!",
              pathElementMappingEntry.mapping.mappingName, pathElementMappingEntry, typeInfo.type.simpleName, propPathElementPropertyName))
    property.typeInfo.container ? new CollectionRuleElem(property) : new PropRuleElem(property)
  }

  String toString() { nextPathElement ?
    ".${propPathElementPropertyName}${nextPathElement}" : ".${propPathElementPropertyName}"
  }
}
