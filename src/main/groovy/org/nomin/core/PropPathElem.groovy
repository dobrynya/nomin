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

  RuleElem createMappingRuleElement(TypeInfo ownerTypeInfo, TypeInfo hint, RuleElem prev) {
    PropertyAccessor property = pathElementMappingEntry.mapping.introspector.property(propPathElementPropertyName, ownerTypeInfo.type)
    if (!property)
      throw new NominException(format("{0}: Mapping rule {1} is invalid because of missing property {2}.{3}!",
              pathElementMappingEntry.mapping.mappingName, pathElementMappingEntry, ownerTypeInfo.type.simpleName, propPathElementPropertyName))
    def merged = property.typeInfo.merge(hint)
    merged.container ?
      new CollectionRuleElem(property, merged, pathElementMappingEntry.mapping.introspector.instanceCreator()) :
      new PropRuleElem(property, merged, pathElementMappingEntry.mapping.introspector.instanceCreator())
  }

  String toString() { nextPathElement ?
    ".${propPathElementPropertyName}${nextPathElement}" : ".${propPathElementPropertyName}"
  }
}
