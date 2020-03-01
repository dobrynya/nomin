package org.nomin;

import java.io.*;
import org.nomin.context.Context;
import org.nomin.util.Introspector;
import java.nio.charset.Charset;

/**
 * Provides operations to perform mapping object trees as well as parsing mappings.
 * @author Dmitry Dobrynin
 *         Created 15.04.2010 11:06:56
 */
public interface NominMapper {
    /**
     * Parses Groovy scripts located in the specified directory.
     * @param directory specifies the directory containing mapping scripts
     * @return this
     * @see java.io.File File
     */
    NominMapper parseDirectory(String directory);

    /**
     * Parses Groovy scripts located in the specified directory.
     * @param directory specifies the directory containing mapping scripts
     * @return this
     * @see java.io.File File
     */
    NominMapper parseDirectory(File directory);

    /**
     * Parses Groovy scripts located in the specified directory.
     * @param directory specifies the directory containing mapping scripts
     * @param charset specifies character set to parse mappings
     * @return this
     * @see java.io.File File
     */
    NominMapper parseDirectory(File directory, Charset charset);

    /**
     * Parses specified files containing mappings.
     * @param mappingScripts specifies files using relative or absolute paths
     * @return this
     * @see java.io.File File
     */
    NominMapper parseFiles(String... mappingScripts);

    /**
     * Parses specified files containing mappings.
     * @param mappingScripts specifies files using relative or absolute paths
     * @return this
     * @see java.io.File File
     */
    NominMapper parseFiles(File... mappingScripts);

    /**
     * Parses specified files containing mappings.
     * @param charset specifies file encoding
     * @param mappingScripts specifies files using relative or absolute paths
     * @return this
     * @see java.io.File File
     */
    NominMapper parseFiles(Charset charset, File... mappingScripts);

    /**
     * Parses mapping scripts using supplied readers.
     * @param readers readers supplying mapping scripts
     * @return this
     * @see java.io.Reader
     */
    NominMapper parse(Reader... readers);

    /**
     * Parses specified Groovy scripts containing mappings.
     * Nomin looks for scripts using {@link ClassLoader#getResourceAsStream(String)} call, so scripts should be
     * available in the classpath. In the future this restriction will be eliminated.
     * @param mappingScripts specifies files in the class path
     * @return this
     */
    NominMapper parse(String... mappingScripts);

    /**
     * Parses specified Groovy scripts containing mappings.
     * Nomin looks for scripts using {@link ClassLoader#getResourceAsStream(String)} call, so scripts should be
     * available in the classpath. In the future this restriction will be eliminated.
     * @param charset specifies file encoding
     * @param mappingScripts specifies files in the class path
     * @return this
     */
    NominMapper parse(Charset charset, String... mappingScripts);

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
     * @return mapped instance of a target class or null if appropriate mappings are not found and automapping is disabled
     */
    <T> T map(Object source, Class<T> targetClass);

    /**
     * Maps specified object to specified target class.
     * @param source specifies source object
     * @param targetClass specifies target class to map to
     * @param mappingCase specifies a mapping case
     * @param <T> specifies target class
     * @return mapped instance of a target class or null if appropriate mappings are not found and automapping is disabled
     */
    <T> T map(Object source, Class<T> targetClass, Object mappingCase);

    /**
     * Maps specified object to specified target class.
     * @param source specifies source object
     * @param targetClass specifies target class to map to
     * @param context specifies the context replacing current context
     * @param <T> specifies target class
     * @return mapped instance of a target class
     */
    <T> T map(Object source, Class<T> targetClass, Context context);

    /**
     * Maps specified object to specified target class.
     * @param source specifies source object
     * @param targetClass specifies target class to map to
     * @param mappingCase specifies a mapping case
     * @param context specifies the context replacing current context
     * @param <T> specifies target class
     * @return mapped instance of a target class or null if appropriate mappings are not found and automapping is disabled
     */
    <T> T map(Object source, Class<T> targetClass, Object mappingCase, Context context);

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
     * @param context specifies the context replacing current context
     * @param <T> specifies target class
     * @return target instance
     */
    <T> T map(Object source, T target, Context context);

   /**
     * Maps specified object to specified target object.
     * @param source specifies source object
     * @param target specifies target object to map to
     * @param mappingCase specifies a mapping case
     * @param context specifies context replacing current context
     * @param <T> specifies target class
     * @return target instance
     */
    <T> T map(Object source, T target, Object mappingCase, Context context);

    /**
     * Enables automapping facility. When automapping facility is enabled and the mapper have not found appropriate
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
     * Enables caching mapped instances to improve performance and make possible cycle detection. Caching is enabled by
     * default.
     * @return this
     */
    NominMapper enableCache();

    /**
     * Disables caching mapped instances. Beware of cycles in object graphs, cycles will
     * cause {@link StackOverflowError StackOverflowError}.
     * @return this
     */
    NominMapper disableCache();

    /**
     * Provides Nomin with the context to use.
     * @param context specifies the context to use
     * @return this
     */
    NominMapper context(Context context);

    /**
     * Removes meta-information and context to prepare shutting down.
     */
    void clearContext();

    /**
     * Provides Nomin with the default introspector to use accross all mappings. Nomin uses
     * {@link org.nomin.util.ReflectionIntrospector ReflectionIntrospector} by default.
     * @param introspector specifies the introspector to use
     * @return this
     */
    NominMapper defaultIntrospector(Introspector introspector);
    
    /**
     * Provides Nomin with a special {@link java.lang.ClassLoader} to use.
     * This becomes handy if use Nomin in a non-standalone environment.
     * @param classLoader specifies the class loader to use during parsing mappings
     * @return this
     */
    NominMapper classLoader(ClassLoader classLoader);
}
