package org.nomin.core;

import groovy.lang.Closure;

/**
 * Contains a mapping case as dynamic value.
 * @author Dmitry Dobrynin
 *         Created: 19.05.2010 22:14:32
 */
public class DynamicMappingCase implements MappingCase {
    private Closure mappingCase;

    public DynamicMappingCase(Closure mappingCase) { this.mappingCase  = mappingCase; }

    public Object get() { return mappingCase.call(); }
}
