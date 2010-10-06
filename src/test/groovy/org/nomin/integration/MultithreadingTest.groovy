package org.nomin.integration

import org.nomin.mappings.*
import org.nomin.core.*
import org.nomin.entity.*
import org.junit.Test

/**
 * Tests Nomin in a multithreading environment.
 * @author Dmitry Dobrynin
 * Date: 30.08.2010 time: 18:18:50
 */
class MultithreadingTest {
  Nomin nomin = new Nomin([key: "value"], MultiThreading)

  Employee e

  @Test
  void test() {
    def t = new Thread({ e = nomin.map(new Person(), Employee) } as Runnable)
    t.start()
    t.join()
    assert e?.name == "value"
  }
}
