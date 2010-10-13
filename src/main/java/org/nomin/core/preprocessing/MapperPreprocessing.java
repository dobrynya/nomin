package org.nomin.core.preprocessing;

import org.nomin.NominMapper;
import org.nomin.core.MappingCase;

/**
 * Applies the mapper to get needed value.
 * @author Dmitry Dobrynin
 *         Created 28.04.2010 15:44:50
 */
public class MapperPreprocessing implements Preprocessing {
    private NominMapper mapper;
    private Class<?> targetClass;
    private MappingCase mappingCase;

    public MapperPreprocessing(Class<?> targetClass, NominMapper mapper, MappingCase mappingCase) {
        this.mapper = mapper;
        this.targetClass = targetClass;
        this.mappingCase = mappingCase;
    }

    public Object preprocess(Object source, Object target) {
        return mapper.map(source, targetClass, mappingCase.get());
    }
}
