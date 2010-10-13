package org.nomin.core;

import groovy.lang.Closure;

/**
 * Contains a mapping case. If mappingCase is a closure it will be called.
 * @author Dmitry Dobrynin
 *         Created: 19.05.2010 22:18:16
 */
public class MappingCase {
    private Object mappingCase;

    public MappingCase(Object mappingCase) { this.mappingCase = mappingCase; }

    public Object get() { return Closure.class.isInstance(mappingCase) ? ((Closure) mappingCase).call() : mappingCase; }
}
