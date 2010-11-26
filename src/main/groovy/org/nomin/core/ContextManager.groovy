package org.nomin.core

import org.nomin.context.Context

/**
 * Contains and manages a context.
 * @author Dmitry Dobrynin
 * Created: 24.04.2010 16:46:01
 */
class ContextManager {
  Context sharedContext = new EmptyContext()

  protected ThreadLocal<Context> shared = new ThreadLocal() {
    protected Context initialValue() { sharedContext }
  }

  protected ThreadLocal<List<Context>> local = new ThreadLocal() {
    protected List<Context> initialValue() { new ArrayList() }
  }

  protected void replaceShared(Context sharedContext) { shared.set sharedContext }

  protected void restoreShared() { shared.set sharedContext }

  protected void pushLocal(Context localContext) { local.get().push localContext }

  protected void popLocal() { local.get().pop() }

  Closure makeContextAware(Closure closure) {
    if (closure) { closure.resolveStrategy = Closure.DELEGATE_FIRST; closure.delegate = this }
    closure
  }

  def propertyMissing(String name) {
    def result = local.get().last().getResource(name)
    if (result == null) result = shared.get().getResource(name)
    if (result == null) throw new MissingPropertyException("There is no resource/component '${name}' in the context!")
    result
  }

  def propertyMissing(String name, value) {
    throw new NominException("Context modification isn't allowed!")
  }

  def methodMissing(String name, args) {
    def closure = propertyMissing(name)
    if (closure instanceof Closure) closure(*args)
    else throw new NominException("There is no closure '${name}' in the context!")
  }

  def String toString() {
    local.get().isEmpty() ? "ContextManager [ shared context = ${shared.get()} ]" :
      "ContextManager [ local context = ${local.get().last()} shared context = ${shared.get()} ]"
  }

  static class EmptyContext implements Context {
    Object getResource(String resource) { null }
  }
}
