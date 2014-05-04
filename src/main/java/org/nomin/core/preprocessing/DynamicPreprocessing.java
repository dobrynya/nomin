package org.nomin.core.preprocessing;

import org.nomin.NominMapper;
import org.nomin.core.*;
import org.nomin.util.*;

/**
 * Chooses and applies appropriate preprocessing depending on a value.
 * @author Dmitry Dobrynin
 *         Created: 12.05.2010 23:40:05
 */
public class DynamicPreprocessing extends Preprocessing {
    private TypeInfo typeInfo;
    private NominMapper mapper;
    private MappingCase mappingCase;

    public DynamicPreprocessing(TypeInfo typeInfo, NominMapper mapper, MappingCase mappingCase) {
        this.typeInfo = typeInfo;
        this.mapper = mapper;
        this.mappingCase = mappingCase;
    }

    public Object preprocess(Object source) {
        Class<?> targetClass = typeInfo.determineTypeDynamically(source);
        if (source == null || targetClass.isInstance(source)) return source;
        ScalarConverter<Object, Object> converter = Converters.findConverter(source.getClass(), targetClass);
        return converter != null ? converter.convert(source) : mapper.map(source, targetClass, mappingCase.get());
    }
}
