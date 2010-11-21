package org.nomin.util;

import org.nomin.core.NominException;
import sun.reflect.ReflectionFactory;
import java.lang.reflect.Constructor;
import static java.text.MessageFormat.format;

/**
 * Creates an instance of the specified class by using just Object's default constructor.
 * @author Dmitry Dobrynin
 *         Date: 26.10.2010 Time: 23:09:16
 */
public class SunInstanceCreator implements InstanceCreator {
    public <T> T create(Class<T> clazz) {
        try {
            ReflectionFactory rf = ReflectionFactory.getReflectionFactory();
            Constructor constructor = rf.newConstructorForSerialization(clazz, Object.class.getDeclaredConstructor());
            return clazz.cast(constructor.newInstance());
        } catch (Exception e) {
            throw new NominException(format("Could not create an instance of {0}!", clazz), e);
        }
    }
}
