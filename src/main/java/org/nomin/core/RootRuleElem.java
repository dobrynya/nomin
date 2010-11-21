package org.nomin.core;

import org.nomin.NominMapper;
import org.nomin.util.TypeInfo;
import static java.text.MessageFormat.format;

/**
 * Provides access to a root instance.
 * @author Dmitry Dobrynin
 *         Created: 27.04.2010 23:20:50
 */
public class RootRuleElem extends RuleElem {
    private NominMapper mapper;
    private MappingCase mappingCase;

    public RootRuleElem(TypeInfo typeInfo, NominMapper mapper, MappingCase mappingCase) {
        super(typeInfo);
        this.mapper = mapper;
        this.mappingCase = mappingCase;
    }

    public Object get(Object instance) throws Exception { return instance; }

    public Object set(Object instance, Object value) throws Exception {
        return  mapper.map(value, instance == null ? typeInfo.type : instance, mappingCase.get());
    }

    public String toString() { return format("{0}", typeInfo); }
}
