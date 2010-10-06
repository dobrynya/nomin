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

  def propertyMissing(name) { return nextPathElem = new PropPathElem(prop: name, mappingEntry: mappingEntry) }

  private def processOppositeSide(value) {
    switch (value) {
      case PathElem: break;
      case Closure: mappingEntry.mapping.mapper.contextManager.makeContextAware value
      default: mappingEntry.pathElem empty: new ExprPathElem(expr: value, mappingEntry: mappingEntry)
    }
  }

  def propertyMissing(name, value) {
    propertyMissing(name)
    processOppositeSide(value)
  }

  def methodMissing(String name, args) {
    switch (name) {
      case "putAt": processOppositeSide(args[1])
      case "getAt": nextPathElem = new SeqPathElem(index: args[0], mappingEntry: mappingEntry); break
      default: nextPathElem = new MethodPathElem(methodName: name, params: args, mappingEntry: mappingEntry)
    }
  }
}
