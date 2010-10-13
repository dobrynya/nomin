package org.nomin.core.preprocessing;

import org.nomin.NominMapper;
import org.nomin.core.MappingCase;

/**
 * Applies mappings to already created instance.
 * @author Dmitry Dobrynin
 *         Created: 02.05.2010 14:46:28
 */
public class AppliedMapperPreprocessing implements Preprocessing {
    private NominMapper mapper;
    private MappingCase mappingCase;

    public AppliedMapperPreprocessing(NominMapper mapper, MappingCase mappingCase) {
        this.mapper = mapper;
        this.mappingCase = mappingCase;
    }

    public Object preprocess(Object source, Object target) {
        return mapper.map(source, target, mappingCase.get());
    }
}
