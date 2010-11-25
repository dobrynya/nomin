package org.nomin.core

import org.nomin.Mapping

/**
 * Performs loading Groovy scripts containing mappings.
 * @author Dmitry Dobrynin
 * Created 09.04.2010 14:32:59
 */
class ScriptLoader {
  protected GroovyShell shell = new GroovyShell()

  /** Loads specified mapping script. */
  Mapping load(String mappingScript) {
    def stream = this.class.classLoader.getResourceAsStream(mappingScript)
    if (stream) load(shell.parse(stream), mappingScript)
    else throw new NominException("Specified script file ${mappingScript} isn't found!")
  }

  /** Processes loaded Groovy script to integrate it with a mapping instance. */
  Mapping load(Script script, String scriptName) {
    Mapping mapping = new Mapping()
    mapping.mappingName = scriptName
    script.metaClass.methodMissing = mapping.&invokeMethod
    script.binding = new Binding() {
      def Object getVariable(String name) { return mapping.getProperty(name) }

      def void setVariable(String name, Object value) { mapping.setProperty(name, value) }
    }
    mapping.metaClass.build = script.&run
    mapping
  }
}
