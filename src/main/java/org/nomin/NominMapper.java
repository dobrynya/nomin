package org.nomin;

import java.util.Map;

/**
 * Provides operations to perform mapping object trees as well as parsing mappings.
 * @author Dmitry Dobrynin
 *         Created 15.04.2010 11:06:56
 */
public interface NominMapper {
    // TODO: Implement a method to load all scripts in specified directory
    // TODO: Implement a method to load scripts from specified files which are not on the classpath

    /**
     * Parses specified Groovy scripts containing mappings.
     * Nomin looks for scripts using {@link ClassLoader#getResourceAsStream(String)} call, so scripts should be
     * available in the classpath. In the future this restriction will be eliminated.
     * @param mappingScripts specifies files in the class path
     * @return this
     */
    NominMapper parse(String... mappingScripts);

    /**
     * Creates and parses mapping instances of specified mapping classes.
     * @param mappingClasses specifies mapping classes
     * @return this
     */
    NominMapper parse(Class<? extends Mapping>... mappingClasses);

    /**
     * Parses specified mapping instance.
     * @param mapping a mapping to parse
     * @return this
     */
    NominMapper parse(Mapping mapping);

    /**
     * Maps specified object to specified target class.
     * @param source specifies source object
     * @param targetClass specifies target class to map to
     * @param <T> specifies target class
     * @return mapped instance of a target class
     */
    <T> T map(Object source, Class<T> targetClass);

    /**
     * Maps specified object to specified target class.
     * @param source specifies source object
     * @param targetClass specifies target class to map to
     * @param mappingCase specifies a mapping case
     * @param <T> specifies target class
     * @return mapped instance of a target class
     */
    <T> T map(Object source, Class<T> targetClass, Object mappingCase);

    /**
     * Maps specified object to specified target class. 
     * @param source specifies source object
     * @param targetClass specifies target class to map to
     * @param context specifies context replacing current context
     * @param <T> specifies target class
     * @return mapped instance of a target class
     */
    <T> T map(Object source, Class<T> targetClass, Map<String, Object> context);

    /**
     * Maps specified object to specified target class.
     * @param source specifies source object
     * @param targetClass specifies target class to map to
     * @param mappingCase specifies a mapping case
     * @param context specifies context replacing current context
     * @param <T> specifies target class
     * @return mapped instance of a target class
     */
    <T> T map(Object source, Class<T> targetClass, Object mappingCase, Map<String, Object> context);

    /**
     * Maps specified object to specified target object.
     * @param source specifies source object
     * @param target specifies target object to map to
     * @param <T> specifies target class
     * @return target instance
     */
    <T> T map(Object source, T target);

    /**
     * Maps specified object to specified target object.
     * @param source specifies source object
     * @param target specifies target object to map to
     * @param mappingCase specifies a mapping case
     * @param <T> specifies target class
     * @return target instance
     */
    <T> T map(Object source, T target, Object mappingCase);

    /**
     * Maps specified object to specified target object.
     * @param source specifies source object
     * @param target specifies target object to map to
     * @param context specifies context replacing current context
     * @param <T> specifies target class
     * @return target instance
     */
    <T> T map(Object source, T target, Map<String, Object> context);

    /**
     * Maps specified object to specified target object.
     * @param source specifies source object
     * @param target specifies target object to map to
     * @param mappingCase specifies a mapping case
     * @param context specifies context replacing current context
     * @param <T> specifies target class
     * @return target instance
     */
    <T> T map(Object source, T target, Object mappingCase, Map<String, Object> context);

    /**
     * Enables automapping facility. When automapping facility was enabled and the mapper didn't find appropriate
     * mappings to apply it creates a mapping automatically to map properties of the same names.
     * @return this
     */
    NominMapper enableAutomapping();

    /**
     * Disables automapping facility.
     * @return this
     */
    NominMapper disableAutomapping();

    /**
     * Provides Nomin with the context to use.
     * @param context the context to use
     * @return this
     */
    NominMapper setContext(Map<String, Object> context);
}
