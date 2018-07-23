package org.nomin

import org.nomin.core.*
import org.nomin.util.*
import org.slf4j.*
import java.text.SimpleDateFormat
import static org.nomin.util.TypeInfoFactory.typeInfo;

/**
 * Stores and parses mapping entries defined inside of the method build. It also provides a number of operations to control
 * parsing and mapping details.
 * @author Dmitry Dobrynin
 * Created 06.04.2010 16:53:38
 */
class Mapping {
  protected static final Logger logger = LoggerFactory.getLogger(Mapping)

  static final String DEFAULT = "default";
  public static final Introspector jb = new ReflectionIntrospector(JbNamingPolicy.jbNamingPolicy)
  public static final Introspector exploding = new ExplodingIntrospector()
  public static final Introspector fast = new FastIntrospector()

  Map<String, Class> side = [:]
  Object mappingCase
  String mappingName = this.class.name
  Boolean mapNulls = false
  NominMapper mapper

  protected Introspector introspector
  private List<MappingEntry> entries = []
  private Map<String, Closure> hooks = [ throwableHandler: {} ]
  private List<Class<? extends Throwable>> throwables

  Mapping() {}

  Mapping(Class<?> a, Class<?> b, Nomin mapper) {
    mappingFor a: a, b: b
    this.mapper = mapper;
    this.introspector = mapper.defaultIntrospector();
  }

  ParsedMapping parse() {
    build()
    new ParsedMapping(mappingName, side.a, side.b, mappingCase, entries*.parse(), hooks, mapNulls, throwables,
            introspector.instanceCreator(), mapper)
  }

  /**
   * Defines mapping information: classes of the sides and mapping case. This method should be called firstly.
   * <p><code> mappingFor a: org.nomin.entity.Person, b: org.nomin.entity.Employee</code></p>or
   * <p><code> mappingFor a: org.nomin.entity.Person, b: org.nomin.entity.Employee, case: "specificCase"</code></p>
   * @param mappingInfo specifies parameters of the mapping
   */
  void mappingFor(Map mappingInfo) {
    if (mappingInfo.a) side.a = mappingInfo.a
    if (mappingInfo.b) side.b = mappingInfo.b
    if (mappingInfo.case) mappingCase = mappingInfo.case
  }

  /**
   * Hangs <b>before</b> mapping hook. Closure <b>before</b> has access to <b>a</b>, <b>b</b> and <b>direction</b> local variables.
   * @param before specifies a closure to be called before mapping will be running
   */
  void before(Closure before) { beforeAtoB before; beforeBtoA before }

  /**
   * Hangs <b>before a to b</b> hook.
   * @param beforeAtoB specifies a closure to be called before mapping <b>a</b> to <b>b</b>
   */
  void beforeAtoB(Closure beforeAtoB) { hook beforeAtoB: beforeAtoB }

  /**
   * Hangs <b>before b to a</b> hook.
   * @param beforeBtoA specifies a closure to be called before mapping <b>b</b> to <b>a</b>
   */
  void beforeBtoA(Closure beforeBtoA) { hook beforeBtoA: beforeBtoA }

  /**
   * Hangs <b>after</b> mapping hook. Closure <b>after</b> has access <b>a</b>, <b>b</b> and <b>direction</b> local variables.
   * @param before specifies a closure to be called after mapping is done
   */
  void after(Closure after) { afterAtoB after; afterBtoA after }

  /**
   * Hangs <b>after a to b</b> hook.
   * @param beforeBtoA specifies a closure to be called after mapping <b>a</b> to <b>b</b>
   */
  void afterAtoB(Closure afterAtoB) { hook afterAtoB: afterAtoB }

  /**
   * Hangs <b>after b to a</b> hook.
   * @param beforeBtoA specifies a closure to be called after mapping <b>b</b> to <b>a</b>
   */
  void afterBtoA(Closure afterBtoA) { hook afterBtoA: afterBtoA }

  /**
   * Hangs 'throwable handler' hook. As the only parameter the method takes a map containing a list of 'throwables' and
   * a 'handler' closure. The handler can access <b>mapping</b>, <b>failed</b>, <b>message</b> and <b>throwable</b> objects.
   * F.e. <p><code>handle throwables: Exception, handler: { ... }</code><p> or in case of a set of throwables
   * <p><code>handle throwables: [RuntimeException, IllegalArgumentException],  handler: { ... }</code></p>
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
      LoggerFactory.getLogger(ParsedMapping).warn("${message} Ignored!", throwable)
    }
  }

  /**
   * Defines conversions between sides.
   * <p><code>
   * a.sex = b.gender<br>
   * convert to_a: { gender -> gender == Gender.MALE }, to_b: { sex -> sex ? Gender.MALE : Gender.FEMALE }
   * </code></p>
   */
  void convert(Map<String, Closure> conversions) {
    if (!entries) entry()
    entries.last().conversion to_a: mapper.contextManager.makeContextAware(conversions.to_a),
            to_b: mapper.contextManager.makeContextAware(conversions.to_b)
  }

  /**
   * Specifies the types of the sides to be used instead of discovered by the introspector. This is useful when we have
   * more specific types. For instance, if we have a.objects of type Object but we would like to interpret this as
   * a list of Person.
   * <p><code>a.objects = b.list<br>hint a: List[Person]</code></p>
   * @param hints specifies hints
   */
  void hint(hints) {
    if (!entries) entry()
    entries.last().hint a: processHints(hints.a), b: processHints(hints.b)
  }

  /**
   * Defines a mapping case for the last mapping rule as follows
   * <p><code>a.details = b.object<br>mappingCase "specificCase"</code></p>
   * @param mappingCase specifies the mapping case to use
   */
  void mappingCase(mappingCase) {
    if (!entries) entry()
    if (Closure.isInstance(mappingCase)) mapper.contextManager.makeContextAware(mappingCase)
    entries.last().mappingCase new MappingCase(mappingCase)
  }

  /**
   * Applies a conversion between string and date values to the last mapping rule as follows:
   * <p><code>
   * a.date = b.dateAsString<br>
   * dateFormat "dd-MM-yyyy"
   * </code></p>
   * As a formatter SimpleDateFormat is used.
   * @param pattern specifies SimpleDateFormat's pattern
   */
  void dateFormat(String pattern) {
    if (!entries) entry()
    entries.last().dateFormat new SimpleDateFormat(pattern)
  }

  /**
   * Defines a simple conversion as follows:
   * <p><code>
   * a.sex = b.gender<br>
   * simple([true, Gender.MALE], [false, Gender.FEMALE])
   * </code></p>
   * @param pairs specifies corresponding values from both sides
   */
  void simple(List... pairs) { simple(pairs.collect {[a: it[0], b: it[1]]}.toArray(new Map[pairs.size()])) }

  /**
   * Defines a simple conversion as follows:
   * <p><code>
   * a.sex = b.gender<br>
   * simple([a: true, b: Gender.MALE], [a: false, b: Gender.FEMALE])
   * </code></p>
   * @param pairs specifies corresponding values from both sides
   */
  void simple(Map<String, Object>... pairs) {
    def (direct, reverse) = [new HashMap(), new HashMap()]
    for (Map<String, Object> p in pairs) { direct[p.a] = p.b; reverse[p.b] = p.a }
    convert to_a: new SimpleConversionClosure(reverse), to_b: new SimpleConversionClosure(direct)
  }

  /**
   * Uses specified class introspector: <p><code>introspector exploding // or fast</code></p>
   * @param introspector specifies an introspector to be used for discovering classes.
   *
   */
  void introspector(Introspector introspector) {
    if (introspector) this.introspector = introspector
    else throw new NominException("${mappingName}: introspector should not be null!")
  }

  /**
   * Creates mapping entries with properties of the same names. This is useful in case of classes with the same structure,
   * for example DTO or something like that.
   */
  Mapping automap() {
    checkSides()
    if (logger.isDebugEnabled()) logger.debug "${mappingName}: Building a mapping between ${side.a.name} and ${side.b.name} automatically"
    Set<String> b = introspector.properties(side.b)
    for (String property : introspector.properties(side.a)) if (b.contains(property)) {
        logger.debug("a.{} = b.{}", property, property)
        this.a."${property}" = this.b."${property}"
    }
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

  /** Returns current mapping entry or newly created entry if the previous entry is full. */
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

  void propertyMissing(String name, value) {
    propertyMissing(name)
  }

  /**
   * Contains mapping rules. This method should be overridden to provide mapping details to Nomin as follows
   * <pre><code>
   * class MyMapping extends Mapping {
   *     void build() {
   *         mappingFor a: Class1, b: Class2
   *         a.property1 = b.property2
   *         ...
   *     }
   * }
   * </code></pre>
   */
  protected void build() {}

  String toString() {
    def sb = new StringBuilder("Mapping { a: ${sideA?.name} b: ${sideB?.name} case: ${mappingCase ?: "default"}\n")
    entries.each { sb.append("  ").append(it).append("\n") }
    sb.append("}").toString()
  }
}