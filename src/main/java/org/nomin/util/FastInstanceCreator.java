package org.nomin.util;

import java.lang.reflect.InvocationTargetException;
import org.nomin.core.NominException;
import static java.text.MessageFormat.format;

/**
 *
 * @author Dmitry Dobrynin
 *         Date: 23.11.2010 time: 10:57:45
 */
public class FastInstanceCreator implements InstanceCreator {
    public <T> T create(Class<T> clazz) {
        try {
            return clazz.cast(FastHelper.getOrCreateFastClass(clazz).newInstance());
        } catch (InvocationTargetException e) {
            throw new NominException(format("Could not create an instance of {0}!", clazz));
        }
    }
}
