package org.nomin.core

import org.nomin.Mapping

/**
 * Contains a script instance for building mapping rules. This is necessary because Groovy 1.8 doesn't support overriding
 * protected methods through a meta class.
 * @author Dmitry Dobrynin
 * Date: 20.07.11 Time: 3:04
 */
class ScriptMapping extends Mapping {
    Script script

    ScriptMapping(Script script) { this.script = script }

    protected void build() { script.run() }
}
