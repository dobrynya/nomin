package org.nomin.core

import org.nomin.Mapping
import org.slf4j.*

/**
 * Performs loading Groovy scripts containing mappings.
 * @author Dmitry Dobrynin
 * Created 09.04.2010 14:32:59
 */
class ScriptLoader {
  final static Logger logger = LoggerFactory.getLogger(ScriptLoader)
  protected GroovyShell shell = new GroovyShell()

  /** Loads specified mapping script. */
  Mapping loadFile(String mappingScript) {
      def file = new File(mappingScript)
      if (file.exists()) {
          logger.debug("Loading file ${file.absolutePath}")
          load(shell.parse(file), mappingScript)
      }
      else throw new NominException("Specified script file ${file.absolutePath} isn't found!")
  }

  /** Loads specified mapping script. */
  Mapping load(String mappingScript) {
    def stream = this.class.classLoader.getResourceAsStream(mappingScript)
    if (stream) {
        logger.debug("Loading resource ${mappingScript}")
        load(shell.parse(stream), mappingScript)
    }
    else throw new NominException("Specified script file ${mappingScript} isn't found!")
  }

  /** Processes loaded Groovy script to integrate it with a mapping instance. */
  Mapping load(Script script, String scriptName) {
    Mapping mapping = new ScriptMapping(script)
    mapping.mappingName = scriptName
    script.metaClass.methodMissing = { name, args -> mapping.invokeMethod(name, args) }
    script.binding = new Binding() {
      Object getVariable(String name) { mapping.getProperty(name) }
      void setVariable(String name, Object value) { mapping.setProperty(name, value) }
    }

    mapping
  }
}
