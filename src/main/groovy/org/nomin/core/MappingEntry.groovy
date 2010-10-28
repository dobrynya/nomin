package org.nomin.core

import org.nomin.Mapping
import org.nomin.util.*
import static java.text.MessageFormat.format
import static org.nomin.util.TypeInfoFactory.*
import org.nomin.core.preprocessing.ConversionPreprocessing
import org.nomin.core.preprocessing.DynamicPreprocessing
import org.apache.commons.beanutils.ConvertUtils
import org.nomin.core.preprocessing.ConvertUtilsPreprocessing
import org.nomin.core.preprocessing.MapperPreprocessing
import org.nomin.core.preprocessing.Preprocessing

/**
 * Contains mapping pair from side a and side b.
 * @author Dmitry Dobrynin
 * Created 08.04.2010 18:38:13
 */
@SuppressWarnings(["GroovyVariableNotAssigned", "GroovyAssignabilityCheck"])
class MappingEntry {
  private static final defaultMappingCase = new MappingCase(null)

  Mapping mapping
  List<MappingSide> sides = []
  Introspector introspector
  MappingCase mappingCase = defaultMappingCase

  /** Checks whether this mapping entry is full. */
  boolean completed() { sides.size() == 2 }

  /** Defines left or right conversion.  */
  void conversion(conversion) {
    sides.each {
      if (it.sideA) it.conversion = conversion.to_a
      if (it.sideB) it.conversion = conversion.to_b
    }
  }

  /** Defines hints for the sides.  */
  void hint(Map<String, List<TypeInfo>> hints) {
    hints.each { k, v -> sides.each { if (it.isSide(k)) it.hints = v }}
  }

  /** Adds a path element.  */
  def pathElem(pathElem) {
    sides << new MappingSide(pathElem: pathElem)
    pathElem
  }

  /** Forces the mapper to use a particular mapping case.  */
  void mappingCase(mappingCase) { this.mappingCase = mappingCase }

  MappingRule parse() {
    sides.sort { it.sideA ? -1 : it.sideB ? 1 : 0 }
    sides.each {
      it.firstRuleElem = buildRuleElem(typeInfo(it.sideA || it.sideB ? it.pathElem.rootPathElementClass : Undefined),
              it.hints.iterator(), it.pathElem)
      // Remove the first root rule element to optimize performance
      if (RootRuleElem.isInstance(it.firstRuleElem) && it.firstRuleElem.next) it.firstRuleElem = it.firstRuleElem.next 
      it.lastRuleElem = findLast(it.firstRuleElem)
    }
    validate sides[0].lastRuleElem, sides[1].lastRuleElem
    sides[0].lastRuleElem.initialize(sides[0], sides[1], this)
    sides[1].lastRuleElem.initialize(sides[1], sides[0], this)
    new MappingRule(sides[0].firstRuleElem, sides[1].firstRuleElem, allowed(sides[1].lastRuleElem), allowed(sides[0].lastRuleElem))
  }

  protected void validate(lastA, lastB) {
    if (RootRuleElem.isInstance(lastA) && RootRuleElem.isInstance(lastB))
      throw new NominException(format("{0}: Recursive mapping rule {1} causes infinite loop!", mapping.mappingName, this))
    if ((lastA.typeInfo.container ^ lastB.typeInfo.container) && PropRuleElem.isInstance(lastA) && PropRuleElem.isInstance(lastB))
      throw new NominException(format("{0}: Mapping rule {1} is invalid because there is a collection/array on the first side and a single value on another!", mapping.mappingName, this))
    if (lastA.typeInfo.map ^ lastB.typeInfo.map)
      throw new NominException(format("{0}: Mapping rule {1} is invalid because there is a map on the first side and a non-map on another!", mapping.mappingName, this))
  }

  protected RuleElem findLast(RuleElem elem) { elem.next ? findLast(elem.next) : elem }

  protected boolean allowed(RuleElem elem) {
    [PropRuleElem, CollectionRuleElem, SeqRuleElem, RootRuleElem].contains(elem.class)
  }

  protected RuleElem buildRuleElem(TypeInfo ti, Iterator<TypeInfo> hints, PathElem elem, RuleElem prev = null) {
    RuleElem current = applyHint(elem.createMappingRuleElement(ti, prev), hints)
    if (prev) prev.next = current
    if (elem.nextPathElement) buildRuleElem(current.typeInfo, hints, elem.nextPathElement, current)
    current
  }

  protected RuleElem applyHint(RuleElem elem, Iterator<TypeInfo> hints) {
    if (!RootRuleElem.isInstance(elem) &&  hints?.hasNext()) {
      def nextHint = hints.next()
      if (nextHint) elem.typeInfo.merge nextHint
    }
    elem
  }

  protected Preprocessing createPreprocessing(MappingSide thiz, MappingSide that) {
    if (thiz.getConversion() != null) return new ConversionPreprocessing(thiz.getConversion());
    else if (thiz.lastRuleElem.typeInfo.isDynamic() || that.lastRuleElem.typeInfo.isUndefined())
      return new DynamicPreprocessing(thiz.lastRuleElem.typeInfo, mapping.mapper, mappingCase);
    Class st = thiz.lastRuleElem.typeInfo.determineType(), tt = that.lastRuleElem.typeInfo.determineType();
    if (ConvertUtils.lookup(tt, st) != null) return new ConvertUtilsPreprocessing(st);
    else if (!st.isAssignableFrom(tt)) return new MapperPreprocessing(st, mapping.mapper, mappingCase);
  }

  String toString() { format("{0} = {1}", sides[0].pathElem, sides[1].pathElem) }
}
