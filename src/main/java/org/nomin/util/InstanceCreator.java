package org.nomin.util;

/**
 * Provides the opration to create an instance of the requested type.
 * @author Dmitry Dobrynin
 *         Date: 21.11.2010 Time: 14:23:54
 */
public interface InstanceCreator {
    <T> T create(Class<T> clazz) throws Exception;
}
