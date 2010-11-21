package org.nomin.util;

import org.nomin.core.NominException;
import static java.text.MessageFormat.format;

/**
 * Instantiates an instance using reflection.
 * @author Dmitry Dobrynin
 *         Date: 21.11.2010 Time: 14:57:28
 */
public class ReflectionInstanceCreator implements InstanceCreator {
    public <T> T create(Class<T> clazz) {
        try { return clazz.newInstance(); }
        catch (Exception e) { throw new NominException(format("Could not create an instance of {0}!", clazz), e); }
    }
}
