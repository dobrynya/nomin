package org.nomin.core;

import static java.text.MessageFormat.format;
import static java.util.Arrays.asList;
import org.slf4j.*;

/**
 * Contains mapping rule elements and delegates mapping to them.
 * @author Dmitry Dobrynin
 *         Created 28.04.2010 12:10:08
 */
public class MappingRule {
    private static final Logger logger = LoggerFactory.getLogger(MappingRule.class);

    ParsedMapping mapping;
    final RuleElem a, b;
    final boolean allowedToB, allowedToA;

    public MappingRule(RuleElem a, RuleElem b, boolean allowedToB, boolean allowedToA) {
        this.a = a; this.b = b;
        this.allowedToB = allowedToB;
        this.allowedToA = allowedToA;
    }

    public Object map(Object source, Object target, RuleElem srcElem, RuleElem targetElem) throws Exception {
        Object result = srcElem.get(source);
        if (logger.isTraceEnabled()) logger.trace("{} = {}", srcElem.path(), result);
        if (result != null || mapping.mapNulls) return targetElem.set(target, result);
        return target;
    }

    public Object mapBtoA(Object b, Object a) throws Exception {
        if (allowedToA) return map(b, a, this.b, this.a); else return a;
    }

    public Object mapAtoB(Object a, Object b) throws Exception {
        if (allowedToB) return map(a, b, this.a, this.b); else return b;
    }

    public String toString() {
        return format(asList(ValueRuleElem.class, ClosureRuleElem.class).contains(a.getClass()) ?
                        "{1} = {0}" : "{0} = {1}", a.path(), b.path()); 
    }
}
