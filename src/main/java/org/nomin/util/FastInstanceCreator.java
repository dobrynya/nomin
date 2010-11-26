package org.nomin.util;

/**
 * Creates object instances using CGLIB FastClass.
 * @author Dmitry Dobrynin
 *         Date: 23.11.2010 time: 10:57:45
 */
public class FastInstanceCreator implements InstanceCreator {
    public <T> T create(Class<T> clazz) throws Exception {
        return clazz.cast(FastHelper.getOrCreateFastClass(clazz).newInstance());
    }
}
