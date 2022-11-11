package org.nomin.core;

import groovy.lang.Closure;
import org.nomin.context.MapContext;
import org.nomin.util.InstanceCreator;
import org.slf4j.*;
import java.util.*;
import static java.lang.String.format;

/**
 * Contains rule elements and provides mapping facility. Instead of doing mapping by itself a parsed mapping delegates
 * the work to its mapping rules.
 * @author Dmitry Dobrynin
 *         Created 28.04.2010 12:57:58
 */
public class ParsedMapping {
    static final Logger logger = LoggerFactory.getLogger(ParsedMapping.class);

    final String mappingName;
    final Class<?> sideA, sideB;
    final boolean mapNulls;
    final Object mappingCase;
    final MappingRule[] rules; // using an array is necessary for optimizing performance
    final Map<String, Closure> hooks;
    final List<Class<? extends Throwable>> throwables;
    final InstanceCreator instanceCreator;
    final Nomin mapper;

    public ParsedMapping(String mappingName, Class<?> sideA, Class<?> sideB, Object mappingCase, List<MappingRule> rules,
                         Map<String, Closure> hooks, boolean mapNulls, List<Class<? extends Throwable>> throwables,
                         InstanceCreator instanceCreator, Nomin mapper) {
        this.mappingName = mappingName;
        this.sideB = sideB;
        this.sideA = sideA;
        this.mapNulls = mapNulls;
        this.mappingCase = mappingCase;
        this.rules = rules.toArray(new MappingRule[rules.size()]);
        this.hooks = hooks;
        this.throwables = throwables;
        this.instanceCreator = instanceCreator;
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
                    localContext.put("message", message);
                    localContext.put("throwable", throwable);
                    callHook("throwableHandler", localContext);
                    return;
                }
        }
        throw throwable instanceof NominException && !((NominException) throwable).shouldWrap ?
                (NominException) throwable : new NominException(message, throwable);
    }

    protected void callHook(String hookName, Map<String, Object> lc) {
        Closure hook = hooks.get(hookName);
        if (hook != null) try { hook.call(); } catch (Throwable th) {
            handle(th, lc, hook, format("%s: Hook '%s' has failed!", mappingName, hookName));
        }
    }

    public Object map(Object source, Object target, Class<?> targetClass, boolean direction, boolean stopMapping) {
        if (logger.isTraceEnabled()) logger.trace(format("%s: %s is being mapped to %s",
                mappingCase == null ? mappingName : mappingName + ":" + mappingCase,
                source.getClass().getName(), targetClass.getName()));

        if (target == null)
            try { target = instanceCreator.create(targetClass); }
            catch (Throwable th) { throw new NominException(true, format("Could not instantiate %s!", targetClass), th); }

        if (mapper.cacheEnabled) {
            Map<MappingKey, Object> keys = mapper.cache.get().get(source);
            if (keys == null) mapper.cache.get().put(source, keys = new HashMap<MappingKey, Object>(1));
            MappingKey key = new MappingKey(source.getClass(), targetClass, mappingCase);
            Object cached = keys.get(key);
            if (cached == null) keys.put(key, target);
            else if (stopMapping) return cached;
        }

        Map<String, Object> lc = direction ? lc(source, target, direction) : lc(target, source, direction);
        mapper.contextManager.pushLocal(new MapContext(lc));

        callHook(direction ? "beforeAtoB" : "beforeBtoA", lc);

        if (direction) for (MappingRule rule : rules) try {  rule.mapAtoB(source, target); }
        catch (Throwable throwable) { handle(throwable, lc, rule, format("%s: Mapping rule %s has failed!", mappingName, rule)); }
        else for (MappingRule rule : rules) try {  rule.mapBtoA(source, target); }
        catch (Throwable throwable) { handle(throwable, lc, rule, format("%s: Mapping rule %s has failed!", mappingName, rule)); }

        callHook(direction ? "afterAtoB" : "afterBtoA", lc);

        mapper.contextManager.popLocal();
        return target;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("Mapping a: ").append(sideA != null ? sideA.getSimpleName() : "")
                .append(" b: ").append(sideB != null ? sideB.getSimpleName() : "");
        if (mappingCase != null) sb.append(" case: ").append(mappingCase);
        sb.append(" {\n");
        for (MappingRule rule : rules) sb.append("  ").append(rule).append("\n");
        return sb.append('}').toString();
    }
}
