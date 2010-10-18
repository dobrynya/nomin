package org.nomin.core

import org.apache.commons.beanutils.ConvertUtils
import org.nomin.Mapping
import org.nomin.util.*
import org.nomin.core.preprocessing.*
import static java.text.MessageFormat.*
import static org.nomin.util.TypeInfoFactory.*

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
  private MappingCase mappingCase = defaultMappingCase

  /** Checks whether this mapping entry is full. */
  boolean completed() { sides.size() == 2 }

  /** Defines left or right conversion.  */
  void conversion(conversion) {
    sides.each {
      if (it.sideA) it.conversion = conversion.to_b
      if (it.sideB) it.conversion = conversion.to_a
    }
  }

  /** Defines hints for the sides.  */
  void hint(Map<String, List<TypeInfo>> hints) {
    hints.each { k, v -> sides.each { if (it.isSide(k)) it.hints = v }}
  }

  /**
   * TODO: Change comment! Change caller!
   * Defines left or right path element. If a path element is specified with 'empty' key it will be stored in firstly
   * available side.
   */
  def pathElem(pathElem) {
    if (completed()) throw new NominException("Mapping Entry ${this} is full!")
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
      it.lastRuleElem = findLast(it.firstRuleElem)
    }
    validate sides[0].lastRuleElem, sides[1].lastRuleElem
    new MappingRule(sides[0].firstRuleElem, sides[1].firstRuleElem,
            analyzeElem(sides[0].lastRuleElem, sides[1].lastRuleElem, sides[0].conversion), allowed(sides[1].lastRuleElem),
            analyzeElem(sides[1].lastRuleElem, sides[0].lastRuleElem, sides[1].conversion), allowed(sides[0].lastRuleElem))
  }

  protected void validate(lastA, lastB) {
    if (RootRuleElem.isInstance(lastA) && RootRuleElem.isInstance(lastB))
      throw new NominException(format("{0}: Recursive mapping rule a = b causes infinite loop!", mapping.mappingName))
    if ((lastA.typeInfo.container ^ lastB.typeInfo.container) && PropRuleElem.isInstance(lastA) && PropRuleElem.isInstance(lastB))
      throw new NominException(format("{0}: Mapping rule {1} is invalid because there is a collection/array on the first side and a single value on another!", mapping.mappingName, this))
  }

  /** Finds the last element in the chain. */
  protected RuleElem findLast(RuleElem elem) { elem.next ? findLast(elem.next) : elem }

  protected Preprocessing analyzeElem(RuleElem source, RuleElem target, Closure conversion) {
    if (conversion) return new ConversionPreprocessing(conversion)
    if (target.typeInfo.dynamic || source.typeInfo.undefined)
      return new DynamicPreprocessing(target.typeInfo, mapping.mapper, mappingCase)
    if (RootRuleElem.isInstance(target)) return new AppliedMapperPreprocessing(mapping.mapper, mappingCase)
    def (sourceType, targetType) = [source.typeInfo.determineType(), target.typeInfo.determineType()]
    if (targetType.isAssignableFrom(sourceType)) return null
    if (ConvertUtils.lookup(sourceType, targetType)) return new ConvertUtilsPreprocessing(target.typeInfo.determineType())
    new MapperPreprocessing(target.typeInfo.determineType(), mapping.mapper, mappingCase)
  }

  protected boolean allowed(RuleElem elem) {
    [PropRuleElem, CollectionRuleElem, ArrayRuleElem, ArraySeqRuleElem, SeqRuleElem,
            MapRuleElem, MapAccessRuleElem, RootRuleElem].contains(elem.class)
  }

  protected RuleElem buildRuleElem(TypeInfo ti, Iterator<TypeInfo> hints, PathElem elem, RuleElem prev = null) {
    RuleElem current = processElem(elem, ti, prev, hints)
    if (prev) prev.next = current
    if (elem.nextPathElem) buildRuleElem(current.typeInfo, hints, elem.nextPathElem, current)
    current
  }

  protected RootRuleElem processElem(RootPathElem root, TypeInfo typeInfo, RuleElem prev, Iterator<TypeInfo> hints) {
    new RootRuleElem(typeInfo)
  }

  protected RuleElem processElem(PropPathElem elem, TypeInfo ti, RuleElem prev, Iterator<TypeInfo> hints) {
    PropertyAccessor property = introspector.property(elem.propPathElementPropertyName, ti.type)
    if (!property)
      throw new NominException(format("{0}: Mapping rule {1} is invalid because of missing property {2}.{3}!",
              mapping.mappingName, this, ti.type.simpleName, elem.propPathElementPropertyName))

    if (property.typeInfo.collection)
      applyHint(new CollectionRuleElem(property.typeInfo, property), hints)
    else if (property.typeInfo.array)
      applyHint(new ArrayRuleElem(property.typeInfo, property), hints)
    else if (property.typeInfo.map)
      applyHint(new MapRuleElem(property.typeInfo, property), hints)
    else
      applyHint(new PropRuleElem(property.typeInfo, property), hints)
  }

  protected RuleElem processElem(SeqPathElem elem, TypeInfo ti, RuleElem prev, Iterator<TypeInfo> hints) {
    if (ti.container) {
      RuleElem res
      if (prev instanceof ArrayRuleElem) {
        if (elem.seqPathElementIndex instanceof Integer)
          res = new ArraySeqRuleElem(ti.getParameter(0), (Integer) elem.seqPathElementIndex, prev)
        else throw new NominException(format("{0}: Mapping rule {1} is invalid because the index of {2} should be an integer value!",
            mapping.mappingName, this, prev))
      } else {
        if (ti.map) res = new MapAccessRuleElem(ti.getParameter(1), elem.seqPathElementIndex)
        else {
          if (elem.seqPathElementIndex instanceof Integer)
            res = new SeqRuleElem(ti.getParameter(0), elem.seqPathElementIndex)
          else
            throw new NominException(format("{0}: Mapping rule {1} is invalid because the index of {2} should be an integer value!",
                    mapping.mappingName, this, prev))
        }
      }
      applyHint(res, hints)
    } else throw new NominException(format("{0}: Mapping rule {1} is invalid because property {2} isn''t indexable!",
            mapping.mappingName, this, prev));
  }

  protected RuleElem processElem(MethodPathElem elem, TypeInfo ti, RuleElem prev, Iterator<TypeInfo> hints) {
    MethodInvocation invocation = introspector.invocation(elem.methodPathElementMethodName, ti.type, * elem.methodPathElementInvocationParameters)
    applyHint(new MethodRuleElem(invocation.typeInfo, invocation), hints)
  }

  protected RuleElem processElem(ExprPathElem elem, TypeInfo ti, RuleElem prev, Iterator<TypeInfo> hints) {
    applyHint(Closure.isInstance(elem.exprPathElementExpr) ? new ClosureRuleElem(elem.exprPathElementExpr) : new ValueRuleElem(elem.exprPathElementExpr), hints)
  }

  protected RuleElem applyHint(RuleElem elem, Iterator<TypeInfo> hints) {
    if (hints?.hasNext()) {
      def nextHint = hints.next()
      if (nextHint) elem.typeInfo.merge nextHint
    }
    elem
  }

  String toString() { format("{0} = {1}", sides[0].pathElem, sides[1].pathElem) }
}
