package org.nomin.functional

import org.nomin.core.Nomin
import org.nomin.entity.Employee
import org.nomin.entity.Person
import org.nomin.mappings.MultiThreading

/**
 * Tests Nomin in a multithreading environment.
 * @author Dmitry Dobrynin
 * Date: 30.08.2010 time: 18:18:50
 */
class MultithreadingTest {
  Nomin nomin = new Nomin([key: "value"], MultiThreading)

  Employee e

  @org.junit.Test
  void test() {
    def t = new Thread({ e = nomin.map(new Person(), Employee) } as Runnable)
    t.start()
    t.join()
    assert e?.name == "value"
  }
}
