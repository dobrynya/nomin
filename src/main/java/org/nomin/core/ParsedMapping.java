package org.nomin.core;

import groovy.lang.Closure;
import static java.text.MessageFormat.format;
import org.nomin.context.MapContext;
import org.nomin.util.InstanceCreator;
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
    final Class<?> sideA, sideB;
    final boolean mapNulls;
    final Object mappingCase;
    final MappingRule[] rules; // using an array is necessary for optimizing performance
    final Map<String, Closure> hooks;
    private List<Class<? extends Throwable>> throwables;
    final InstanceCreator instanceCreator;
    final Nomin mapper;
    protected static ThreadLocal<WeakHashMap<Object, Map<Key, Object>>> cache =
            new ThreadLocal<WeakHashMap<Object, Map<Key, Object>>>() {
                protected WeakHashMap<Object, Map<Key, Object>> initialValue() {
                    return new WeakHashMap<Object, Map<Key, Object>>();
                }
            };

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
                    localContext.put("throwable", throwable);
                    callHook("throwableHandler");
                    return;
                }
        }
        throw throwable instanceof NominException && !((NominException) throwable).shouldWrap ?
                (NominException) throwable : new NominException(format(message, params), throwable);
    }

    protected void callHook(String hookName) {
        Closure hook = hooks.get(hookName);
        if (hook != null) hook.call();
    }

    public Object map(Object source, Object target, Class<?> targetClass, boolean direction) {
        if (logger.isTraceEnabled())
            logger.trace(format("{0}: {1} is being mapped to {2}",
                    mappingCase == null ? mappingName : mappingName + ":" + mappingCase,
                    source.getClass().getName(), targetClass.getName()));

        if (target == null)
            try { target = instanceCreator.create(targetClass); }
            catch (Throwable th) { throw new NominException(true, format("Could not instantiate {0}!", targetClass), th); }


        // TODO: Fix the bug!
        // In case f hierarchical mapping it maps only a base class. Upper level mappings are not being applied!
        if (mapper.cacheEnabled) {
            Map<Key, Object> keys = cache.get().get(source);
            if (keys == null) cache.get().put(source, keys = new HashMap<Key, Object>(1));
            Key key = new Key(source.getClass(), targetClass, mappingCase);
            Object cached = keys.get(key);
            if (cached == null) keys.put(key, target);
            else return cached;
        }

        Map<String, Object> lc = direction ? lc(source, target, direction) : lc(target, source, direction);
        mapper.contextManager.pushLocal(new MapContext(lc));

        try { callHook("before"); }
        catch (Throwable throwable) { handle(throwable, lc, hooks.get("before"), "{0}: Hook ''before'' has failed!", mappingName); }

        for (MappingRule rule : rules)
            try { if (direction) rule.mapAtoB(source, target); else rule.mapBtoA(source, target); }
            catch (Throwable throwable) { handle(throwable, lc, rule, "{0}: Mapping rule {1} has failed!", mappingName, rule); }

        try { callHook("after"); }
        catch (Throwable throwable) { handle(throwable, lc, hooks.get("after"), "{0}: Hook ''after'' has failed!", mappingName); }

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
