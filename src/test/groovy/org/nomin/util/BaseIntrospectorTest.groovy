package org.nomin.util

/**
 * Tests BaseIntrospector.
 * @author Dmitry Dobrynin
 * Date: 23.11.2010 time: 9:20:28
 */
class BaseIntrospectorTest {
  BaseIntrospector bi = new BaseIntrospector()

  @org.junit.Test
  void testCanApply() {
    assert bi.canApply(null, String)
    assert bi.canApply(String, String)
    assert bi.canApply(Class.getPrimitiveClass("int"), Class.getPrimitiveClass("int"))
    assert bi.canApply(Integer, Number)
    assert !bi.canApply(Integer, String)
  }

  String methodToFind(String s, int i, String s2, long l) {
    "${s} ${i} ${s2} ${l}"
  }

  @org.junit.Test
  void testFindApplicable() {
    def method = bi.findApplicableMethod("methodToFind", getClass(), "String", 1, null, 1L)
    assert method && method.invoke(this, "String", 1, null, 1L) == "String 1 null 1"
  }

  @org.junit.Test
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
