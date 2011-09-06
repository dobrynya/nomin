package org.nomin.core;

/**
 * Contains characteristics of a mapping.
* @author Dmitry Dobrynin
*         Date: 28.07.11 time: 5:18
*/
public class Key {
    Class<?> source, target;
    Object mappingCase;
    boolean includeInverse = false;

    public Key(Class<?> source, Class<?> target, Object mappingCase) {
        this.source = source; this.target = target; this.mappingCase = mappingCase;
    }

    public Key(ParsedMapping parsedMapping) {
        this(parsedMapping.sideA, parsedMapping.sideB, parsedMapping.mappingCase);
        includeInverse = true;
    }

    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Key)) return false;
        if (this == obj) return true;
        Key another = (Key) obj;
        return ((source == another.source && target == another.target) ^
                (includeInverse && source == another.target && target == another.source)) &&
                ((mappingCase == null && another.mappingCase == null) ^
                        (mappingCase != null && mappingCase.equals(another.mappingCase)));
    }

    public int hashCode() {
        return source.hashCode() * 13 + 31 * target.hashCode() + (mappingCase != null ? 71 * mappingCase.hashCode() : 0);
    }
}
