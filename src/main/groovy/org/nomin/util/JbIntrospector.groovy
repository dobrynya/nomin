package org.nomin.util

import java.util.regex.*
import org.nomin.core.NominException

/**
 * Introspects a JavaBeans class. No matter whether specified class is a concrete class or interface.
 * JbIntrospector is restricted to work only with public methods.
 * @author Dmitry Dobrynin
 * Created 14.04.2010 17:11:13
 */
class JbIntrospector extends BaseIntrospector {
  static Pattern PROPERTY_ACCESSOR = ~/((get)|(is)|(set))(\p{Upper}.*)+/

  protected String prefixed(name, prefix) { "${prefix}${name.charAt(0).toUpperCase()}${name.substring(1)}" }

  PropertyAccessor property(String name, Class<?> targetClass) {
    def (getter, setter, typeInfo) = findAccessorMethods([prefixed(name, "get"), prefixed(name, "is")], [prefixed(name, "set")], targetClass)
    if (getter || setter) new JbPropertyAccessor(name, getter, setter, typeInfo)
  }

  public Set<String> properties(Class<?> targetClass) {
    Set<String> properties = new HashSet()
    targetClass.methods.each {
      Matcher matcher = it.name =~ PROPERTY_ACCESSOR
      if (it.declaringClass != Object && matcher.matches()) {
        String pn = matcher[0][5]
        properties.add(new String("${Character.toLowerCase(pn.charAt(0))}${pn.substring(1)}"))
      }
    }
    properties
  }
}
