package org.nomin.util;

import java.util.List;

/**
 * Defines a naming policy and is used by introspectors to find a property setter/getter.
 * @author Dmitry Dobrynin
 *         Date: 26.10.2010 Time: 8:32:11
 */
public interface NamingPolicy {
    /**
     * Returns all the possible getter names.
     * @param propertyName specifies a target property
     * @return all the possible getter method names
     */
    List<String> getters(String propertyName);

    /**
     * Returns all the possible setter names.
     * @param propertyName specifies a target property
     * @return all the possible setter method names
     */
    List<String> setters(String propertyName);

    /**
     * Determines whether the specified method name is an propertyAccessor method for a property.
     * @param methodName specifies method name
     * @return the property name or null if the method is not a property accessor
     */
    String propertyAccessor(String methodName);
}
