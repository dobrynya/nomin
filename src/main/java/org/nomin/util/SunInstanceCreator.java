package org.nomin.util;

import java.lang.reflect.Constructor;
import sun.reflect.ReflectionFactory;

/**
 * Creates an instance of the specified class by using just Object's default constructor.
 * @author Dmitry Dobrynin
 *         Date: 26.10.2010 Time: 23:09:16
 */
public class SunInstanceCreator implements InstanceCreator {
    public <T> T create(Class<T> clazz) throws Exception {
        ReflectionFactory rf = ReflectionFactory.getReflectionFactory();
        Constructor constructor = rf.newConstructorForSerialization(clazz, Object.class.getDeclaredConstructor());
        return clazz.cast(constructor.newInstance());
    }
}
