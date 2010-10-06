package org.nomin.core;

import static java.text.MessageFormat.format;
import static java.util.Arrays.asList;
import org.nomin.core.preprocessing.Preprocessing;
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
    final Preprocessing direct, reverse;
    final boolean directAllowed, reverseAllowed;

    public MappingRule(RuleElem a, RuleElem b, Preprocessing direct, boolean directAllowed, Preprocessing reverse, boolean reverseAllowed) {
        this.a = a; this.b = b;
        this.direct = direct; this.directAllowed = directAllowed;
        this.reverse = reverse; this.reverseAllowed = reverseAllowed;
    }

    void map(Object source, Object target, RuleElem srcElem, RuleElem targetElem, Preprocessing preprocessing) {
        Object result = srcElem.get(source);
        if (logger.isTraceEnabled()) logger.trace("{} = {}", srcElem.path(), result);
        if (result != null || mapping.mapNulls) targetElem.set(target, result, preprocessing);
    }

    void mapBtoA(Object b, Object a) {
        if (reverseAllowed) map(b, a, this.b, this.a, reverse);
    }

    void mapAtoB(Object a, Object b) {
        if (directAllowed) map(a, b, this.a, this.b, direct);
    }

    public String toString() {
        return format(asList(ValueRuleElem.class, ClosureRuleElem.class).contains(a.getClass()) ?
                        "{1} = {0}" : "{0} = {1}", a.path(), b.path()); 
    }
}
