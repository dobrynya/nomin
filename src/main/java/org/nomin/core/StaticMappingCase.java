package org.nomin.core;

/**
 * Contains a mapping case as static value.
 * @author Dmitry Dobrynin
 *         Created: 19.05.2010 22:11:01
 */
public class StaticMappingCase implements MappingCase {
    private Object mappingCase;

    public StaticMappingCase(Object mappingCase) { this.mappingCase = mappingCase; }

    public Object get() { return mappingCase; }
}
