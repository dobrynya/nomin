package org.nomin.core

import org.nomin.util.TypeInfo
import static java.text.MessageFormat.format

/**
 * Contains the index in a sequence or the key in case of Map.
 * @author Dmitry Dobrynin
 * Created 09.04.2010 10:21:50
 */
class SeqPathElem extends PathElem {
  Object seqPathElementIndex

  RuleElem createMappingRuleElement(TypeInfo typeInfo, RuleElem prev) {
    if (typeInfo.container) {
      if (!typeInfo.map && !Integer.isInstance(seqPathElementIndex))
        throw new NominException(format("{0}: Mapping rule {1} is invalid because the index of {2} should be an integer value!",
                pathElementMappingEntry.mapping.mappingName, pathElementMappingEntry, prev))
      new SeqRuleElem(seqPathElementIndex, prev.containerHelper)
    } else throw new NominException(format("{0}: Mapping rule {1} is invalid because property {2} isn''t indexable!",
            pathElementMappingEntry.mapping.mappingName, pathElementMappingEntry, prev));
  }

  String toString() { nextPathElement ? "[${seqPathElementIndex}]${nextPathElement}" : "[${seqPathElementIndex}]" }
}
