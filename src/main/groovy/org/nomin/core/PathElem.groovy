package org.nomin.core

/**
 * Provides infrastructure to build mapping rules.
 * @author Dmitry Dobrynin
 * Created 07.04.2010 11:30:37
 */
@SuppressWarnings("GroovyFallthrough")
abstract class PathElem {
  PathElem nextPathElem
  MappingEntry mappingEntry

  private def processOppositeSide(value) {
    switch (value) {
      case PathElem: break;
      case Closure: mappingEntry.mapping.mapper.contextManager.makeContextAware value
      default: mappingEntry.pathElem new ExprPathElem(exprPathElementExpr: value, mappingEntry: mappingEntry)
    }
  }

  def getProperties() { propertyMissing("properties") }

  def propertyMissing(String name) {
    def escapedMethod = ["hashCode", "toString", "getClass"].find { name.startsWith(it) && name.endsWith("()") }
    escapedMethod ? methodMissing(escapedMethod, new Object[0]) :
      (nextPathElem = new PropPathElem(propPathElementPropertyName: name, mappingEntry: mappingEntry))
  }

  def propertyMissing(name, value) {
    propertyMissing(name)
    processOppositeSide(value)
  }

  /** Overrides the predefined method to let getting values from Map to a user */
  Object getAt(String index) {
    methodMissing("getAt", [index])
  }

  /** Overrides the predefined method to let putting values to Map to a user */
  void putAt(String index, value) {
    methodMissing("putAt", [index, value])
  }

  def methodMissing(String name, args) {
    switch (name) {
      case "putAt": processOppositeSide(args[1])
      case "getAt": nextPathElem = new SeqPathElem(seqPathElementIndex: args[0], mappingEntry: mappingEntry); break
      default: nextPathElem = new MethodPathElem(methodPathElementMethodName: name, methodPathElementInvocationParameters: args, mappingEntry: mappingEntry)
    }
  }
}
