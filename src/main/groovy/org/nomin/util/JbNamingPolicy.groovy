package org.nomin.util

import java.util.regex.Pattern
import java.util.regex.Matcher

/**
 * Defines JavaBeans naming policy.
 * @author Dmitry Dobrynin
 * Date: 26.10.2010 Time: 8:38:54
 */
class JbNamingPolicy implements NamingPolicy {
  static final jbNamingPolicy = new JbNamingPolicy()

  static Pattern propertyAccessorPattern = ~/((get)|(is)|(set))(\p{Upper}.*)+/

  List<String> getters(String propertyName) { [prefixed(propertyName, "get"), prefixed(propertyName, "is")] }

  List<String> setters(String propertyName) { [prefixed(propertyName, "set")] }

  String propertyAccessor(String methodName) {
    Matcher matcher = methodName =~ propertyAccessorPattern
    if (matcher.matches()) {
      String capitalizedPropertyName = matcher[0][5]
      "${Character.toLowerCase(capitalizedPropertyName.charAt(0))}${capitalizedPropertyName.substring(1)}"
    }
  }

  protected String prefixed(name, prefix) { "${prefix}${name.charAt(0).toUpperCase()}${name.substring(1)}" }
}
