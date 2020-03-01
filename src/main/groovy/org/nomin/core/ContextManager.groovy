package org.nomin.core

import org.nomin.context.Context
import java.util.concurrent.ConcurrentHashMap

/**
 * Contains and manages a context.
 * @author Dmitry Dobrynin
 * Created: 24.04.2010 16:46:01
 */
class ContextManager {
  Context sharedContext = new EmptyContext()
  protected ConcurrentHashMap<Thread, Context> globalContext = new ConcurrentHashMap<>()
  protected ConcurrentHashMap<Thread, LinkedList<Context>> localStack = new ConcurrentHashMap<>()

  def clearContexts() {
    globalContext.clear()
    localStack.clear()
  }

  protected void replaceShared(Context sharedContext) {
    globalContext.put(Thread.currentThread(), sharedContext)
  }

  protected void restoreShared() {
    globalContext.put(Thread.currentThread(), sharedContext)
  }

  protected void pushLocal(Context localContext) {
    localStack.compute(Thread.currentThread()) { thread, stack ->
      if (stack == null) stack = new LinkedList<Context>()
      stack.addFirst(localContext)
      stack
    }
  }

  protected void popLocal() {
    localStack.compute(Thread.currentThread()) { thread, stack ->
      stack.removeFirst()
      stack
    }
  }

  Closure makeContextAware(Closure closure) {
    if (closure != null) {
      closure.resolveStrategy = Closure.DELEGATE_FIRST
      closure.delegate = this
    }
    closure
  }

  def propertyMissing(String name) {
    def result = localStack.get(Thread.currentThread()).peekFirst().getResource(name)
    if (result == null) result = globalContext.getOrDefault(Thread.currentThread(), sharedContext).getResource(name)
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
    def global = globalContext.getOrDefault(Thread.currentThread(), sharedContext)
    def local = localStack.get(Thread.currentThread())
    "ContextManager [ local context = ${local.peekFirst()} shared context = ${global} ]"
  }

  static class EmptyContext implements Context {
    Object getResource(String resource) { null }
  }
}
