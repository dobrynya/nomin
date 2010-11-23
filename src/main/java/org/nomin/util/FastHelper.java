package org.nomin.util;

import net.sf.cglib.reflect.FastClass;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Contains CGLIB helper operations.
 * @author Dmitry Dobrynin
 *         Date: 23.11.2010 time: 10:52:02
 */
public class FastHelper {
    private static Map<Class, FastClass> cache = new ConcurrentHashMap<Class, FastClass>();

    public static FastClass getOrCreateFastClass(Class<?> clazz) {
        FastClass fast = cache.get(clazz);
        if (fast == null) cache.put(clazz, fast = FastClass.create(clazz));
        return fast;
    }
}
