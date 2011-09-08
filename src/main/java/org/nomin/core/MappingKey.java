package org.nomin.core;

/**
 * Contains characteristics of a mapping.
 * @author Dmitry Dobrynin
 *         Date: 28.07.11 time: 5:18
 */
@SuppressWarnings({"EqualsWhichDoesntCheckParameterClass"})
public class MappingKey {
    final Class<?> source, target;
    final Object mappingCase;
    final boolean includeInverse;

    public MappingKey(Class<?> source, Class<?> target, Object mappingCase) {
        this.source = source; this.target = target; this.mappingCase = mappingCase; includeInverse = false;
    }

    public MappingKey(ParsedMapping pm) {
        this.source = pm.sideA; this.target = pm.sideB; this.mappingCase = pm.mappingCase; includeInverse = true;
    }

    public boolean equals(Object obj) { // There cannot be the same key
        MappingKey k = (MappingKey) obj;
        return ((source == k.source && target == k.target) ^ (includeInverse && source == k.target && target == k.source)) &&
                ((mappingCase == null && k.mappingCase == null) ^ (mappingCase != null && mappingCase.equals(k.mappingCase)));
    }

    public int hashCode() {
        return source.hashCode() * 13 + 31 * target.hashCode() + (mappingCase != null ? 71 * mappingCase.hashCode() : 0);
    }
}
