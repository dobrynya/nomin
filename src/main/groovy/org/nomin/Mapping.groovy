package org.nomin

import static java.text.MessageFormat.*
import org.nomin.core.*
import org.nomin.util.*
import org.slf4j.*
import java.text.SimpleDateFormat

/**
 * Stores and parses mapping entries defined in method build body. It also provides a number of operations to control
 * parsing and mapping details.
 * @author Dmitry Dobrynin
 * Created 06.04.2010 16:53:38
 */
class Mapping implements MappingConsts {
  protected static final Logger logger = LoggerFactory.getLogger(Mapping)

  Class sideA
  Class sideB
  Object mappingCase
  String mappingName = this.class.name
  Boolean mapNulls = false
  Nomin mapper

  private Introspector introspector = jb
  private List<MappingEntry> entries = []
  private MappingEntry last = new MappingEntry(mapping: this, introspector: introspector)
  private Map<String, Closure> hooks = [ throwableHandler: {} ]
  private List<Class<? extends Throwable>> throwables;

  def Mapping() {}

  def Mapping(Class<?> sideA, Class<?> sideB, Nomin mapper) {
    this.sideA = sideA; this.sideB = sideB; this.mapper = mapper;
  }

  ParsedMapping parse() {
    build()
    completed()
    new ParsedMapping(mappingName, sideA, sideB, mappingCase, entries*.parse(), hooks, mapNulls, throwables, mapper)
  }

  /** Defines mapping information: classes of the sides and mapping case */
  void mappingFor(Map mappingInfo, Closure block = null) {
    if (mappingInfo.a) sideA = mappingInfo.a
    if (mappingInfo.b) sideB = mappingInfo.b
    if (mappingInfo.case) mappingCase = mappingInfo.case
    closure(block)
  }

  /** Hangs 'after mapping' hook. Closure 'after' has a, b and direction local variables */
  void after(Closure after) { hook after: after }

  /** Hangs 'before mapping' hook. Closure 'before' has a, b and direction local variables */
  void before(Closure before) { hook before: before }

  /**
   * Hangs 'throwable handler' hook. As the only parameter the method takes a map containing a list of 'throwables' and
   * a 'handler' closure. 'Handler' closure can access 'mapping' and 'failed' objects.
   * F.e. handle throwables: Exception, handler: { ... } or in case of a set of throwables
   * handle throwables: [RuntimeException, IllegalArgumentException],  handler: { ... }
   */
  void handle(parameters) {
    if (parameters.throwables instanceof List) this.throwables = parameters.throwables
    else this.throwables = [parameters.throwables]
    hook throwableHandler: parameters.handler
  }

  /** Hangs a hook.  */
  void hook(Map<String, Closure> hook) {
    for (Map.Entry<String, Closure> entry : hook.entrySet())
      hooks[entry.key] = mapper.contextManager.makeContextAware(entry.value)
  }

  /**
   * Configures the mapping to swallow specified throwables at mapping time.
   * @param silent specifies whether or not to log all throwables
   */
  void swallow(boolean silent = false, Class<? extends Throwable>... throwables) {
    handle throwables: throwables.collect { it }, handler: silent ? null : {
      LoggerFactory.getLogger(ParsedMapping).warn(format("{0}: {1} failed, but the failure is ignored!",
                mapping.mappingName, Closure.isInstance(failed) ? "Hook" : failed), throwable)
    }
  }

  /** Defines conversions between sides. */
  void convert(Map<String, Closure> conversions, Closure block = null) {
    closure(block)
    last.conversion to_a: mapper.contextManager.makeContextAware(conversions.to_a),
            to_b: mapper.contextManager.makeContextAware(conversions.to_b)
  }

  /**
   * When applying a hint to collections there is the ability to specify the type of collection elements.
   * For instance, hint a: List[Person].
   */
  void hint(hints, Closure block = null) {
    closure(block)
    last.hint a: processHints(hints.a), b: processHints(hints.b)
  }

  /** Defines a mapping case for the last mapping rule. */
  void mappingCase(mappingCase) {
    if (mappingCase instanceof Closure)  {
      mapper.contextManager.makeContextAware(mappingCase)
      last.mappingCase new DynamicMappingCase(mappingCase)
    } else last.mappingCase new StaticMappingCase(mappingCase)
  }

  /**
   * Applies a conversion between string and date values. As a formatter FastDateFormat is used.
   * @param pattern specifies a conversion pattern as supported by SimpleDateFormat
   * @param date specifies the date property
   */
  def dateFormat(String pattern, date) {
    SimpleDateFormat sdf = new SimpleDateFormat(pattern)
    if (last.side.a.pathElem) convert to_a: new String2DateClosure(sdf), to_b: new Date2StringClosure(sdf)
    else convert to_b: new String2DateClosure(sdf), to_a: new Date2StringClosure(sdf)
    date
  }

  /** Applies simple conversions to the last mapping rule. */
  void simple(List... pairs) {
    def (direct, reverse) = [new HashMap(), new HashMap()]
    for (List p in pairs) { direct[p[0]] = p[1]; reverse[p[1]] = p[0] }
    convert to_a: new SimpleConversionClosure(reverse), to_b: new SimpleConversionClosure(direct)
  }

  /** Uses specified class introspector. */
  void introspector(Introspector introspector) {
    this.introspector = introspector
    if (!last.completed()) last.introspector = introspector
  }

  /** Creates mapping entries with properties of the same names. */
  void automap() {
    checkSides()
    logger.debug "{}: Automapping facility is enabled", mappingName
    Set<String> b = introspector.properties(sideB)
    for (String property : introspector.properties(sideA)) if (b.contains(property)) this.a."${property}" = this.b."${property}"
  }

  private def processHints(hints) {
    if (hints) {
      hints = hints instanceof List ? hints : [hints]
      hints = hints.collect {
        TypeInfo typeInfo = TypeInfo.isInstance(it) ? it : it != MappingConsts.DEFAULT ? TypeInfoFactory.typeInfo(it) : null
        if (typeInfo?.isDynamic()) mapper.contextManager.makeContextAware(typeInfo.dynamicType)
        typeInfo
      }
    }
    hints
  }

  /**
   * Checks the last entry for completeness. If so the last entry will be stored (i.e. accepted)
   * and a new one will be created.
   */
  private def completed() {
    if (last.completed()) {
      entries << last
      last = new MappingEntry(mapping: this, introspector: introspector)
    }
  }

  private def checkSides() {
    if (!sideA || !sideB) throw new NominException("${mappingName}: Mapping sides should be defined before defining mapping rules!")
  }

  /** Executes a block of code. */
  private void closure(Closure block) {
    if (block) { block.delegate = this; block() }
  }

  /**
   * Creates a root path element and also updates the last entry.
   * @param side specifies an entry side as map, a or b
   */
  def propertyMissing(String name) {
    checkSides()
    completed()
    switch (name) {
      case "a": return last.pathElem(a: new RootPathElem(mappingEntry: last))
      case "b": return last.pathElem(b: new RootPathElem(mappingEntry: last))
      default: throw new NominException("${mappingName}: Property '${name}' isn't defined!")
    }
  }

  def propertyMissing(String name, value) {
    propertyMissing(name)
  }

  /** Contains mapping rules.  */
  protected void build() {}

  def String toString() {
    def sb = new StringBuilder("Mapping { a: ${sideA?.name} b: ${sideB?.name} case: ${mappingCase ?: "default"}\n")
    entries.each { sb.append("  ").append(it).append("\n") }
    sb.append("}").toString()
  }

  static { ClassImprover }
}