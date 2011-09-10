package org.nomin.core;

import org.nomin.Mapping;
import org.nomin.NominMapper;
import org.nomin.context.*;
import org.nomin.util.Introspector;
import org.slf4j.*;
import java.util.*;
import static java.text.MessageFormat.format;

/**
 * NominMapper implementation.
 * Automapping facility is enabled by default.
 * @author Dmitry Dobrynin
 *         Created 28.04.2010 17:10:24
 */
@SuppressWarnings({"unchecked"})
public class Nomin implements NominMapper {
    public static final String NOMIN_VERSION = "1.1.1";
    static final Logger logger = LoggerFactory.getLogger(Nomin.class);

    protected ScriptLoader scriptLoader = new ScriptLoader();
    protected ContextManager contextManager = new ContextManager();
    protected List<ParsedMapping> mappings = new ArrayList<ParsedMapping>();
    protected Map<MappingKey, List<MappingWithDirection>> cachedApplicable = new HashMap<MappingKey, List<MappingWithDirection>>();
    protected boolean automappingEnabled = true;
    protected boolean cacheEnabled = true;
    protected Introspector defaultIntrospector = Mapping.jb;

    public Nomin() {}

    public Nomin(Map<String, Object> context) { contextManager.setSharedContext(new MapContext(context)); }

    public Nomin(Class<? extends Mapping>... mappingClasses) { parse(mappingClasses); }

    /**
     * Constructs a Nomin instance.
     * @param context specifies the context to use
     * @param mappingClasses specifies mapping classes to parse
     * @deprecated use {@link #context(org.nomin.context.Context)} and {@link #parse(Class[])} to configure Nomin
     */
    @Deprecated
    public Nomin(Map<String, Object> context, Class<? extends Mapping>... mappingClasses) {
        this(context);
        parse(mappingClasses);
    }

    @Deprecated
    public Nomin(Map<String, Object> context, Mapping... mappings) {
        this(context);
        for (Mapping mapping : mappings) parse(mapping);
    }

    @Deprecated
    public Nomin(Map<String, Object> context, String... mappingScripts) {
        this(context);
        parse(mappingScripts);
    }

    public Nomin(String... mappingScripts) { parse(mappingScripts); }

    public boolean isAutomappingEnabled() { return automappingEnabled; }

    public Nomin enableAutomapping() {
        this.automappingEnabled = true;
        logger.debug("Nomin's automapping facility is enabled");
        return this;
    }

    public Nomin disableAutomapping() {
        this.automappingEnabled = false;
        logger.debug("Nomin's automapping facility is disabled");
        return this;
    }

    public NominMapper enableCache() {
        cacheEnabled = true;
        return this;
    }

    public NominMapper disableCache() {
        cacheEnabled = false;
        return this;
    }

    @Deprecated
    public NominMapper setContext(Map<String, Object> context) { return context(new MapContext(context)); }

    public Nomin context(Context context) {
        contextManager.setSharedContext(context);
        return this;
    }

    public NominMapper defaultIntrospector(Introspector introspector) {
        defaultIntrospector = introspector;
        return this;
    }

    public Introspector defaultIntrospector() { return defaultIntrospector; }

    public List<ParsedMapping> getMappings() { return Collections.unmodifiableList(mappings); }

    public Nomin parse(String... mappingScripts) {
        for (String mappingScript : mappingScripts) parse(scriptLoader.load(mappingScript));
        return this;
    }

    /**
     * Parses the specified mappings. Nomin uses {@link org.nomin.util.ReflectionIntrospector} to instantiate a mapping
     * instance. Any way it's possible to parse already instantiated mapping instance calling {@link #parse(org.nomin.Mapping)}.
     * @param mappingClasses specifies mapping classes
     * @return this
     */
    public Nomin parse(Class<? extends Mapping>... mappingClasses) {
        for (Class<? extends Mapping> mc : mappingClasses) {
            Mapping mapping;
            try { mapping = Mapping.jb.instanceCreator().create(mc); }
            catch (Exception e) { throw new NominException(format("Could not instantiate a mapping {0}!", mc), e); }
            parse(mapping);
        }
        return this;
    }

    public synchronized Nomin parse(Mapping mapping) {
        cachedApplicable.clear(); // the cache is cleared before parsing a mapping
        mapping.setMapper(this);
        mapping.introspector(defaultIntrospector);
        addOrReplace(mapping.parse());
        return this;
    }

    public <T> T map(Object source, Class<T> targetClass) { return map(source, targetClass, (Object) null); }

    public <T> T map(Object source, Class<T> targetClass, Object mappingCase) {
        return source != null && targetClass != null ? (T) map(source, null, targetClass, mappingCase) : null;
    }

    @Deprecated
    public <T> T map(Object source, Class<T> targetClass, Map<String, Object> context) {
        return map(source, targetClass, new MapContext(context));
    }

    public <T> T map(Object source, Class<T> targetClass, Context context) {
        contextManager.replaceShared(context);
        T target = map(source, targetClass);
        contextManager.restoreShared();
        return target;
    }

    @Deprecated
    public <T> T map(Object source, Class<T> targetClass, Object mappingCase, Map<String, Object> context) {
        return map(source, targetClass, mappingCase, new MapContext(context));
    }

    public <T> T map(Object source, Class<T> targetClass, Object mappingCase, Context context) {
        contextManager.replaceShared(context);
        T target = map(source, targetClass, mappingCase);
        contextManager.restoreShared();
        return target;
    }

    public <T> T map(Object source, T target) {
        return map(source, target, (Object) null);
    }

    public <T> T map(Object source, T target, Object mappingCase) {
        if (source != null && target != null) map(source, target, target.getClass(), mappingCase);
        return target;
    }

    @Deprecated
    public <T> T map(Object source, T target, Map<String, Object> context) {
        return map(source, target, new MapContext(context));
    }

    public <T> T map(Object source, T target, Context context) {
        contextManager.replaceShared(context);
        map(source, target, (Object) null);
        contextManager.restoreShared();
        return target;
    }

    @Deprecated
    public <T> T map(Object source, T target, Object mappingCase, Map<String, Object> context) {
        return map(source, target, mappingCase, new MapContext(context));
    }

    public <T> T map(Object source, T target, Object mappingCase, Context context) {
        contextManager.replaceShared(context);
        map(source, target, mappingCase);
        contextManager.restoreShared();
        return target;
    }

    protected Object map(Object source, Object target, Class<?> targetClass, Object mappingCase) {
        boolean first = true;
        for (MappingWithDirection mwd : findCachedApplicable(new MappingKey(source.getClass(), targetClass, mappingCase))) {
            target = mwd.mapping.map(source, target, targetClass, mwd.direction, first);
            if (first) first = false;
        }
        return target;
    }

    protected List<MappingWithDirection> findCachedApplicable(MappingKey key) {
        List<MappingWithDirection> result = cachedApplicable.get(key);
        if (result == null)
            cachedApplicable.put(key, result = findApplicable(key));
        return result;
    }

    protected List<MappingWithDirection> findApplicable(MappingKey key) {
        ArrayList<MappingWithDirection> result = new ArrayList<MappingWithDirection>();
        for (ParsedMapping pm : mappings) {
            if ((pm.mappingCase == null && key.mappingCase == null) ^ (pm.mappingCase != null && pm.mappingCase.equals(key.mappingCase))) {
                if (pm.sideA.isAssignableFrom(key.source) && pm.sideB.isAssignableFrom(key.target)) result.add(new MappingWithDirection(pm, true));
                else if (pm.sideB.isAssignableFrom(key.source) && pm.sideA.isAssignableFrom(key.target)) result.add(new MappingWithDirection(pm, false));
            }
        }
        if (!result.isEmpty()) {
            Collections.sort(result, new MappingComparator(key.target));
        } else if (automappingEnabled) {
            logger.info("Could not find applicable mappings between {} and {}. A mapping will be created using automapping facility",
                    key.source.getName(), key.target.getName());
            ParsedMapping pm = new Mapping(key.source, key.target, this).automap().parse();
            logger.debug("Automatically created {}", pm);
            mappings.add(pm);
            result.add(new MappingWithDirection(pm, true));
        } else
            logger.warn("Could not find applicable mappings between {} and {}!", key.source.getName(), key.target.getName());
        return result;
    }

    protected void addOrReplace(ParsedMapping parsedMapping) {
        MappingKey key = new MappingKey(parsedMapping);
        for (int i = 0; i < mappings.size(); i++) {
            if (key.equals(new MappingKey(mappings.get(i)))) {
                logger.warn("{}\nis replaced with\n{}", mappings.get(i), parsedMapping);
                mappings.set(i, parsedMapping); return;
            }
        }
        logger.debug("Parsed {}", parsedMapping);
        mappings.add(parsedMapping);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("Nomin Mapper\nParsed mappings:\n");
        for (ParsedMapping pm : mappings) sb.append(pm).append("\n");
        return sb.toString();
    }

    static class MappingComparator implements Comparator<MappingWithDirection> {
        private Class<?> target;

        public MappingComparator(Class<?> target) { this.target = target; }

        public int compare(MappingWithDirection o1, MappingWithDirection o2) {
            Class<?> class1 = o1.mapping.sideA.isAssignableFrom(target) ? o1.mapping.sideA : o1.mapping.sideB;
            Class<?> class2 = o2.mapping.sideA.isAssignableFrom(target) ? o2.mapping.sideA : o2.mapping.sideB;
            return class1 == class2 ? 0 : class1.isAssignableFrom(class2) ? -1 : 1;
        }
    }

    static class MappingWithDirection {
        final ParsedMapping mapping;
        final boolean direction;

        MappingWithDirection(ParsedMapping mapping, boolean direction) {
            this.mapping = mapping; this.direction = direction;
        }
    }

    static { logger.info("Nomin Mapping Engine version {}", NOMIN_VERSION); }
}
