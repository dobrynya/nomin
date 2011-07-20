package org.nomin

import org.nomin.core.*
import org.nomin.util.*
import org.slf4j.*
import java.text.SimpleDateFormat
import static org.nomin.util.TypeInfoFactory.typeInfo;

/**
 * Stores and parses mapping entries defined in method build body. It also provides a number of operations to control
 * parsing and mapping details.
 * @author Dmitry Dobrynin
 * Created 06.04.2010 16:53:38
 */
class Mapping {
  protected static final Logger logger = LoggerFactory.getLogger(Mapping)

  static final String DEFAULT = "default";
  static final Introspector jb = new ReflectionIntrospector(JbNamingPolicy.jbNamingPolicy)
  static final Introspector exploding = new ExplodingIntrospector()
  static final Introspector fast = new FastIntrospector()

  Map<String, Class> side = [:]
  Object mappingCase
  String mappingName = this.class.name
  Boolean mapNulls = false
  NominMapper mapper

  protected Introspector introspector = jb
  private List<MappingEntry> entries = []
  private Map<String, Closure<Object>> hooks = [ throwableHandler: {} ]
  private List<Class<? extends Throwable>> throwables

  Mapping() {}

  Mapping(Class<?> a, Class<?> b, Nomin mapper) {
    mappingFor a: a, b: b
    this.mapper = mapper;
  }

  ParsedMapping parse() {
    build()
    new ParsedMapping(mappingName, side.a, side.b, mappingCase, entries*.parse(), hooks, mapNulls, throwables,
            introspector.instanceCreator(), mapper)
  }

  /** Defines mapping information: classes of the sides and mapping case */
  void mappingFor(Map mappingInfo) {
    if (mappingInfo.a) side.a = mappingInfo.a
    if (mappingInfo.b) side.b = mappingInfo.b
    if (mappingInfo.case) mappingCase = mappingInfo.case
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
    if (List.isInstance(parameters.throwables)) this.throwables = parameters.throwables
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
      LoggerFactory.getLogger(ParsedMapping).warn("${mapping.mappingName}: ${Closure.isInstance(failed) ? "Hook" : failed} failed, but the failure is ignored!", throwable)
    }
  }

  /** Defines conversions between sides. */
  void convert(Map<String, Closure> conversions, Closure block = null) {
    if (!entries) entry()
    entries.last().conversion to_a: mapper.contextManager.makeContextAware(conversions.to_a),
            to_b: mapper.contextManager.makeContextAware(conversions.to_b)
  }

  /**
   * When applying a hint to collections there is the ability to specify the type of collection elements.
   * For instance, hint a: List[Person].
   */
  void hint(hints) {
    if (!entries) entry()
    entries.last().hint a: processHints(hints.a), b: processHints(hints.b)
  }

  /** Defines a mapping case for the last mapping rule. */
  void mappingCase(mappingCase) {
    if (!entries) entry()
    if (Closure.isInstance(mappingCase)) mapper.contextManager.makeContextAware(mappingCase)
    entries.last().mappingCase new MappingCase(mappingCase)
  }

  /**
   * Applies a conversion between string and date values to the last mappung rule.
   * As a formatter SimpleDateFormat is used.
   * @param pattern specifies SimpleDateFormat's pattern
   */
  void dateFormat(String pattern) {
    if (!entries) entry()
    SimpleDateFormat sdf = new SimpleDateFormat(pattern)
    def conversion = new String2DateConversion(sdf)
    convert to_a: conversion, to_b: conversion
  }

  /** Applies simple conversions to the last mapping rule. */
  void simple(List... pairs) {
    def (direct, reverse) = [new HashMap(), new HashMap()]
    for (List p in pairs) { direct[p[0]] = p[1]; reverse[p[1]] = p[0] }
    convert to_a: new SimpleConversionClosure(reverse), to_b: new SimpleConversionClosure(direct)
  }

  /** Uses specified class introspector. */
  void introspector(Introspector introspector) {
    if (introspector) this.introspector = introspector
    else throw new NominException("${mappingName}: introspector should not be null!")
  }

  /** Creates mapping entries with properties of the same names. */
  Mapping automap() {
    checkSides()
    if (logger.isDebugEnabled()) logger.debug "${mappingName}: Building a mapping between ${side.a.name} and ${side.b.name} automatically"
    Set<String> b = introspector.properties(side.b)
    for (String property : introspector.properties(side.a)) if (b.contains(property)) this.a."${property}" = this.b."${property}"
    this
  }

  private def processHints(hints) {
    if (hints) {
      hints = List.isInstance(hints) ? hints : [hints]
      hints = hints.collect {
        TypeInfo typeInfo = TypeInfo.isInstance(it) ? it : it != DEFAULT ? typeInfo(it) : null
        if (typeInfo?.isDynamic()) mapper.contextManager.makeContextAware(typeInfo.dynamicType)
        typeInfo
      }
    }
    hints
  }

  /** Returns current mapping entry or newly created entry if the previous entry is full */
  private def entry() {
    if (!entries || entries.last().completed()) entries << new MappingEntry(mapping: this)
    entries.last()
  }

  private def checkSides() {
    if (!side.a || !side.b) throw new NominException("${mappingName}: Mapping sides should be defined before defining mapping rules!")
  }

  /**
   * Creates a root path element.
   * @param side specifies an entry side as map, a or b
   */
  def propertyMissing(String name) {
    checkSides()
    if (["a", "b"].contains(name))
      entry().pathElem(new RootPathElem(rootPathElementSide: name, rootPathElementClass: side[name], pathElementMappingEntry: entry()))
    else throw new NominException("${mappingName}: Property '${name}' isn't defined!")
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