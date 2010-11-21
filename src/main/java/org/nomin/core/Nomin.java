package org.nomin.core;

import org.nomin.Mapping;
import org.nomin.NominMapper;
import org.nomin.util.*;
import org.slf4j.*;
import java.util.*;

import static java.text.MessageFormat.format;

/**
 * NominMapper implementation.
 * Automapping facility is disabled by default.
 * @author Dmitry Dobrynin
 *         Created 28.04.2010 17:10:24
 */
public class Nomin implements NominMapper {
    public static final String NOMIN_VERSION = "1.1.0";
    static final Logger logger = LoggerFactory.getLogger(Nomin.class);

    List<ParsedMapping> mappings = new ArrayList<ParsedMapping>();
    ContextManager contextManager = new ContextManager();
    ScriptLoader scriptLoader = new ScriptLoader();
    Map<Key, List<MappingWithDirection>> cachedApplicable = new HashMap<Key, List<MappingWithDirection>>();
    boolean automappingEnabled = false;
    protected InstanceCreator instanceCreator = new ReflectionInstanceCreator();

    public Nomin() {}

    public Nomin(Map<String, Object> context) { contextManager.setSharedContext(context); }

    public Nomin(Class<? extends Mapping>... mappingClasses) { parse(mappingClasses); }

    public Nomin(Map<String, Object> context, Class<? extends Mapping>... mappingClasses) {
        this(context);
        parse(mappingClasses);
    }

    public Nomin(Map<String, Object> context, Mapping... mappings) {
        this(context);
        for (Mapping mapping : mappings) parse(mapping);
    }

    public Nomin(Map<String, Object> context, String... mappingScripts) {
        this(context);
        parse(mappingScripts);
    }

    public Nomin(String... mappingScripts) { parse(mappingScripts); }

    public boolean isAutomappingEnabled() { return automappingEnabled; }

    public NominMapper enableAutomapping() {
        this.automappingEnabled = true;
        logger.debug("Nomin's automapping facility is enabled");
        return this;
    }

    public NominMapper disableAutomapping() {
        this.automappingEnabled = false;
        logger.debug("Nomin's automapping facility is disabled");
        return this;
    }

    public NominMapper setContext(Map<String, Object> context) {
        contextManager.setSharedContext(context);
        return this;
    }

    public NominMapper context(Map<String, Object> context) {
        contextManager.setSharedContext(context);
        return this;
    }

    public NominMapper instanceCreator(InstanceCreator instanceCreator) {
        this.instanceCreator = instanceCreator;
        return this;
    }

    public List<ParsedMapping> getMappings() { return Collections.unmodifiableList(mappings); }

    public NominMapper parse(String... mappingScripts) {
        for (String mappingScript : mappingScripts) parse(scriptLoader.load(mappingScript));
        return this;
    }

    public NominMapper parse(Class<? extends Mapping>... mappingClasses) {
        for (Class<? extends Mapping> mc : mappingClasses) parse(new ReflectionInstanceCreator().create(mc));
        return this;
    }

    public synchronized NominMapper parse(Mapping mapping) {
        cachedApplicable.clear(); // the cache is cleared before parsing a mapping
        mapping.setMapper(this);
        addOrReplace(mapping.parse());
        return this;
    }

    public <T> T map(Object source, Class<T> targetClass) {
        return map(source, targetClass, (Object) null);
    }

    public <T> T map(Object source, Class<T> targetClass, Object mappingCase) {
        return source != null && targetClass != null ? map(source, instanceCreator.create(targetClass), mappingCase) : null;
    }

    public <T> T map(Object source, Class<T> targetClass, Map<String, Object> context) {
        contextManager.pushSharedContext(context);
        T target = map(source, targetClass);
        contextManager.popSharedContext();
        return target;
    }

    public <T> T map(Object source, Class<T> targetClass, Object mappingCase, Map<String, Object> context) {
        contextManager.pushSharedContext(context);
        T target = map(source, targetClass, mappingCase);
        contextManager.popSharedContext();
        return target;
    }

    public <T> T map(Object source, T target) {
        return map(source, target, (Object) null);
    }

    public <T> T map(Object source, T target, Object mappingCase) {
        if (source != null && target != null) map(source, target, target.getClass(), mappingCase);
        return target;
    }

    public <T> T map(Object source, T target, Map<String, Object> context) {
        contextManager.pushSharedContext(context);
        map(source, target, (Object) null);
        contextManager.popSharedContext();
        return target;
    }

    public <T> T map(Object source, T target, Object mappingCase, Map<String, Object> context) {
        contextManager.pushSharedContext(context);
        map(source, target, mappingCase);
        contextManager.popSharedContext();
        return target;
    }

    protected void map(Object source, Object target, Class<?> targetClass, Object mappingCase) {
        for (MappingWithDirection mwd : findCachedApplicable(source.getClass(), targetClass, mappingCase)) {
            mwd.mapping.map(source, target, mwd.direction);
        }
    }

    protected List<MappingWithDirection> findCachedApplicable(Class<?> source, Class<?> target, Object mappingCase) {
        List<MappingWithDirection> result = cachedApplicable.get(new Key(source, target, mappingCase));
        if (result == null) {
            cachedApplicable.put(new Key(source, target, mappingCase), result = findApplicable(source, target, mappingCase));
        }
        return result;
    }

    protected List<MappingWithDirection> findApplicable(Class<?> source, Class<?> target, Object mappingCase) {
        ArrayList<MappingWithDirection> result = new ArrayList<MappingWithDirection>(mappings.size());
        for (ParsedMapping pm : mappings) {
            if ((pm.mappingCase == null && mappingCase == null) ^
                            (pm.mappingCase != null && pm.mappingCase.equals(mappingCase))) {
                if (pm.sideA.isAssignableFrom(source) && pm.sideB.isAssignableFrom(target)) result.add(new MappingWithDirection(pm, true));
                else if (pm.sideB.isAssignableFrom(source) && pm.sideA.isAssignableFrom(target)) result.add(new MappingWithDirection(pm, false));
            }
        }
        if (!result.isEmpty()) {
            Collections.sort(result, new MappingComparator(target));
        } else if (automappingEnabled) {
            logger.warn("Mappings between {} and {} were not found. A mapping will be created using automapping facility",
                    source.getName(), target.getName());
            Mapping automapping = new Mapping(source, target, this);
            automapping.automap();
            ParsedMapping pm = automapping.parse();
            mappings.add(pm);
            result.add(new MappingWithDirection(pm, true));
        } else
            logger.warn("Nomin could not find applicable mappings between {} and {}!", source.getName(), target.getName());
        return result;
    }

    protected void addOrReplace(ParsedMapping parsedMapping) {
        Key key = new Key(parsedMapping);
        for (int i = 0; i < mappings.size(); i++) {
            if (key.equals(new Key(mappings.get(i)))) {
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
        ParsedMapping mapping;
        boolean direction;

        MappingWithDirection(ParsedMapping mapping, boolean direction) {
            this.mapping = mapping; this.direction = direction;
        }
    }

    static class Key {
        Class<?> source, target;
        Object mappingCase;
        boolean includeInverse = false;

        Key(Class<?> source, Class<?> target, Object mappingCase) {
            this.source = source; this.target = target; this.mappingCase = mappingCase;
        }

        Key(ParsedMapping parsedMapping) {
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

    static {
        logger.info("Nomin Mapping Engine version {}", NOMIN_VERSION);
    }
}
