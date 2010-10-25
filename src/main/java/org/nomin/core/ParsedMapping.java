package org.nomin.core;

import groovy.lang.Closure;
import static java.text.MessageFormat.format;
import org.slf4j.*;
import java.util.*;

/**
 * Contains rule elements and provides mapping facility. Instead of doing mapping by itself a parsed mapping delegates
 * the work to its mapping rules.
 * @author Dmitry Dobrynin
 *         Created 28.04.2010 12:57:58
 */
public class ParsedMapping {
    private static final Logger logger = LoggerFactory.getLogger(ParsedMapping.class);

    final String mappingName;
    final Class<?> sideA, sideB; // TODO: Extract to the separate structure
    final Object mappingCase;
    final MappingRule[] rules; // using an array is necessary for optimizing performance
    final Map<String, Closure> hooks;
    private List<Class<? extends Throwable>> throwables;
    final boolean mapNulls; // TODO: Extract to the separate structure
    final Nomin mapper;

    public ParsedMapping(String mappingName, Class<?> sideA, Class<?> sideB, Object mappingCase, List<MappingRule> rules,
                         Map<String, Closure> hooks, boolean mapNulls, List<Class<? extends Throwable>> throwables, Nomin mapper) {
        this.mappingName = mappingName;
        this.sideB = sideB;
        this.sideA = sideA;
        this.mappingCase = mappingCase;
        this.rules = rules.toArray(new MappingRule[rules.size()]);
        this.hooks = hooks;
        this.throwables = throwables;
        this.mapNulls = mapNulls;
        this.mapper = mapper;
        for (MappingRule rule : this.rules) rule.mapping = this;
    }

    Map<String, Object> lc(Object a, Object b, Boolean direction) {
        Map<String, Object> lc = new HashMap<String, Object>(3);
        lc.put("a", a);
        lc.put("b", b);
        lc.put("direction", direction);
        return lc;
    }

    protected void handle(Throwable throwable, Map<String, Object> localContext, Object failedObject, String message, Object... params) {
        if (throwables != null) {
            for (Class<? extends Throwable> thc : throwables)
                if (thc.isInstance(throwable)) {
                    localContext.put("mapping", this);
                    localContext.put("failed", failedObject);
                    localContext.put("throwable", throwable);
                    callHook("throwableHandler");
                    return;
                }
        }
        throw throwable instanceof NominException ? (NominException) throwable : new NominException(format(message, params), throwable);
    }

    protected void callHook(String hookName) {
        Closure hook = hooks.get(hookName);
        if (hook != null) hook.call();
    }

    public Object map(Object source, Object target, boolean direction) {
        if (logger.isTraceEnabled())
            logger.trace(format("{0}{1}: {2} is being mapped to {3}", mappingName, mappingCase != null ? ":" + mappingCase : "",
                    direction ? sideA.getName() : sideB.getName(), direction ? sideB.getName() : sideA.getName()));

        Map<String, Object> lc = direction ? lc(source, target, direction) : lc(target, source, direction);
        mapper.contextManager.pushLocalContext(lc);

        try { callHook("before"); }
        catch (Throwable throwable) { handle(throwable, lc, hooks.get("before"), "{0}: Hook ''before'' has failed!", mappingName); }

        for (MappingRule rule : rules) {
            try {
                Object newTarget = direction ? rule.mapAtoB(source, target) : rule.mapBtoA(source, target);
                if (newTarget != target) {
                    target = newTarget;
                    lc.put(direction ? "b" : "a", target);
                }
            } catch (Throwable throwable) {
                handle(throwable, lc, rule, "{0}: Mapping rule {1} has failed!", mappingName, rule);
            }
        }

        try { callHook("after"); }
        catch (Throwable throwable) { handle(throwable, lc, hooks.get("after"), "{0}: Hook ''after'' has failed!", mappingName); }

        mapper.contextManager.popLocalContext();
        return target;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("Mapping a: ").append(sideA.getSimpleName()).append(" b: ").append(sideB.getSimpleName());
        if (mappingCase != null) sb.append(" case: ").append(mappingCase);
        sb.append(" {\n");
        for (MappingRule rule : rules) sb.append("  ").append(rule).append("\n");
        return sb.append('}').toString();
    }
}
