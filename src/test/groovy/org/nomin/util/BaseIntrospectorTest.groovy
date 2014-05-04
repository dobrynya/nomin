package org.nomin.util

import org.junit.Test
import java.lang.reflect.Method

/**
 * Tests BaseIntrospector.
 * @author Dmitry Dobrynin
 * Date: 23.11.2010 time: 9:20:28
 */
class BaseIntrospectorTest {
  BaseIntrospector bi = new BaseIntrospector() {
    def Set<String> properties(Class<?> targetClass) { return null; }
    def InstanceCreator instanceCreator() { return null; }
    protected MethodInvocation createInvocation(Method method, Object... args) { return null; }
    protected PropertyAccessor createAccessor(String name, Class<?> targetClass) {return null; }
  }

  @Test
  void testCanApply() {
    assert bi.canApply(null, String)
    assert bi.canApply(String, String)
    assert bi.canApply(Integer.TYPE, Integer.TYPE)
    assert bi.canApply(Integer, Number)
    assert !bi.canApply(Integer, String)
  }

  String methodToFind(String s, int i, String s2, long l) {
    "${s} ${i} ${s2} ${l}"
  }

  @Test
  void testFindApplicable() {
    def method = bi.findApplicableMethod("methodToFind", getClass(), "String", 1, null, 1L)
    assert method && method.invoke(this, "String", 1, null, 1L) == "String 1 null 1"
  }

  @Test
  void testFindAccessorMethods() {
    def (getter, setter, ti) = bi.findAccessorMethods(["getA"], ["setA"], this.class)
    assert getter && setter && ti.type == String && getter.invoke(this) == "a"
    (getter, setter, ti) = bi.findAccessorMethods(["getB"], ["setB"], this.class)
    assert getter && !setter && ti.type == String && getter.invoke(this) == "b"
    (getter, setter, ti) = bi.findAccessorMethods(["getC"], ["setC"], this.class)
    assert !getter && setter && ti.type == String
    setter.invoke(this, "c")
    assert c == "c"
  }

  String a = "a"
  private String b = "b"
  private String c

  String getB() { b }
  void setC(String newC) { c = newC }
}
