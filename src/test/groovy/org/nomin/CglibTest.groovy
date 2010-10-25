package org.nomin

import net.sf.cglib.reflect.FastClass
import net.sf.cglib.reflect.FastMethod
import org.nomin.entity.Employee
import org.nomin.entity.Person
import org.junit.Test

/**
 * @author Dmitry Dobrynin
 * Date: 21.10.2010 Time: 21:54:16
 */
class CglibTest {

  @Test
  void test() {
    FastClass fc = FastClass.&create(Person);
    FastMethod fm = fc.&getMethod("setName", [String] as Class[])

    cal("Creating using NEW keyword", 1000) {
      def p = new Person()
      p.&setName("New name")
      assert p.&getName() == "New name"
    }

    def params = ["New name"] as Object[]
    cal("Creating using FastClass", 1000) {
      def p = (Person) fc.newInstance()
      fm.&invoke(p, params)
      assert p.&getName() == "New name"
    }

    cal ("Creating FastClass for Employee", 50) {
      FastClass.&create(Employee)
    }
  }

  def cal(String text, times, Closure c) {
    def total = System.currentTimeMillis()
    def l = []
    for (i in 1..times) {
      def t = System.currentTimeMillis();
      c()
      l << System.currentTimeMillis() - t
    }
    total = System.currentTimeMillis() - total
    println("Executing \"${text}\" took, ms: ${l[0..49]}\nTotal: ${total}")
  }
}
