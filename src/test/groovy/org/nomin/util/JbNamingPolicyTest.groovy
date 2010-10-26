package org.nomin.util

import static org.nomin.util.JbNamingPolicy.*
/**
 * Tests JavaBeans naming policy.
 * @author Dmitry Dobrynin
 * Date: 26.10.2010 Time: 9:34:05
 */
class JbNamingPolicyTest {
  @org.junit.Test
  void test() {
    assert ["getProperty", "isProperty"] == jbNamingPolicy.getters("property")
    assert ["setProperty"] == jbNamingPolicy.setters("property")
    assert ["property", "property", "property"] == 
            ["getProperty", "isProperty", "setProperty"].collect { jbNamingPolicy.propertyAccessor(it) }
  }
}
