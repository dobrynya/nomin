package org.nomin.core

import org.codehaus.groovy.control.CompilerConfiguration
import org.nomin.Mapping
import java.io.*
import org.slf4j.*
import java.nio.charset.Charset

/**
 * Performs loading Groovy scripts containing mappings.
 * @author Dmitry Dobrynin
 * Created 09.04.2010 14:32:59
 */
class ScriptLoader {
    private final Logger logger = LoggerFactory.getLogger(ScriptLoader)
    protected GroovyShell shell

    ScriptLoader() {
        CompilerConfiguration configuration = new CompilerConfiguration()
        configuration.setScriptBaseClass(DelegatingScript.name)
        shell = new GroovyShell(configuration)
    }

    Mapping loadWithReader(Reader reader) {
        logger.debug("Loading a script using reader")
        load(shell.parse(reader), "$reader")
    }

    /**
     * Loads specified mapping script.
     * @param mappingScript groovy script
     * @param charset file encoding
     * @return initialized mapping
     */
    Mapping loadFile(File mappingScript, Charset charset) {
        if (mappingScript.exists()) {
            logger.debug("Loading file ${mappingScript.absolutePath}")
            load(shell.parse(new InputStreamReader(new FileInputStream(mappingScript), charset)), mappingScript.name)
        } else
            throw new NominException("Specified script file ${file.absolutePath} does not exist!")
    }

    /**
     * Loads specified mapping script.
     * @param mappingScript a mapping script contained as a resource
     * @return initialized mapping
     */
  Mapping loadResource(String mappingScript, Charset charset) {
    def stream = this.class.classLoader.getResourceAsStream(mappingScript)
    if (stream) {
        logger.debug("Loading resource {}", mappingScript)
        load(shell.parse(new InputStreamReader(stream, charset)), mappingScript)
    } else
        throw new NominException("Specified resource ${mappingScript} isn't found!")
  }

    /**
     * Processes loaded Groovy script to integrate it with a mapping instance.
     * @param script a script to be parsed
     * @param scriptName specifies name of a script
     * @return
     */
    Mapping load(Script script, String scriptName) {
        Mapping mapping = new ScriptMapping(script)
        mapping.mappingName = scriptName
        script.setDelegate(mapping)
        mapping
    }
}
