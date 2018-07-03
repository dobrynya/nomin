package org.nomin.util

/**
 * Introspects types.
 * @author Dmitry Dobrynin
 * Created 14.04.2010 17:03:34
 */
interface Introspector {

  /**
   * Creates a method invocation which is able to invoke specified method of specified class.
   * @param name method name
   * @param targetClass a target class
   * @param args method invocation arguments
   * @return a method invocation instance containing a method and agruments
   */
  MethodInvocation invocation(String name, Class<?> targetClass, Object... args);

  /**
   * Creates a property propertyAccessor providing access to a property of a target class.
   * @param name
   * @param targetClass
   * @return
   */
  PropertyAccessor property(String name, Class<?> targetClass);

  /**
   * Introspects a class.
   * @param targetClass
   * @return a set of property names
   */
  Set<String> properties(Class<?> targetClass);


  /**
   * Supplies the corresponding instance creator.
   * @return the instance creator
   */
  InstanceCreator instanceCreator();
}