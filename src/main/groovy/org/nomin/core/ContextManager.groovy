package org.nomin.core

/**
 * Contains and manages a context.
 * @author Dmitry Dobrynin
 * Created: 24.04.2010 16:46:01
 */
class ContextManager {
  Map<String, Object> sharedContext = [:]

  protected ThreadLocal<Map<String, Object>> shared = new ThreadLocal() {
    protected Object initialValue() { return sharedContext; }
  }

  protected ThreadLocal<List<Map<String, Object>>> local = new ThreadLocal() {
    protected Object initialValue() { new ArrayList() }
  }

  // TODO: methods should be package protected to prevent these from calling in a closure!
  void pushSharedContext(Map<String, Object> sharedContext) { shared.set sharedContext }

  void popSharedContext() { shared.set sharedContext }

  void pushLocalContext(Map<String, Object> localContext) { local.get().push(localContext) }

  void popLocalContext() { local.get().pop() }

  Closure makeContextAware(Closure closure) {
    if (closure) { closure.resolveStrategy = Closure.DELEGATE_FIRST; closure.delegate = this }
    closure
  }

  def propertyMissing(String name) {
    def result = local.get().last().get(name)
    if (result == null) {
      result = shared.get().get(name)
    }
    if (result == null) throw new MissingPropertyException("There is no object '${name}' in the context!")
    result
  }

  def propertyMissing(String name, value) {
    throw new GroovyRuntimeException("Context modification isn't allowed!")
  }

  def methodMissing(String name, args) {
    def property = propertyMissing(name)
    if (property instanceof Closure) property(*args)
    else throw new NominException("There is no closure '${name}' in the context!")
  }

  def String toString() { return "ContextManager [local context = ${local.get().last()} shared context = ${shared.get()}]" }
}
