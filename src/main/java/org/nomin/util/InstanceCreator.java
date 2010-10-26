package org.nomin.util;

import sun.reflect.ReflectionFactory;

import java.lang.reflect.Constructor;

/**
 * Creates an instance of the specified class by using just Object's default constructor.
 * @author Dmitry Dobrynin
 *         Date: 26.10.2010 Time: 23:09:16
 */
public class InstanceCreator {
    public static <T> T create(Class<T> clazz) throws Exception { return create(clazz, Object.class); }

    public static <T> T create(Class<T> clazz, Class<? super T> parent) throws Exception {
        ReflectionFactory rf = ReflectionFactory.getReflectionFactory();
        Constructor objDef = parent.getDeclaredConstructor();
        Constructor intConstr = rf.newConstructorForSerialization(clazz, objDef);
        return clazz.cast(intConstr.newInstance());
    }
}
