package org.nomin.functional

import org.nomin.Mapping
import org.nomin.entity.Person
import org.nomin.core.Nomin

/**
 * Tests mapping an interface rather a class.
 * @author Dmitry Dobrynin
 * Date: 30.11.2010 time: 9:42:53
 */
class MappingInterfaceTest {
  def nomin = new Nomin(MappingInterface).disableAutomapping()

  @org.junit.Test
  void test() {
    Person p = nomin.map(new BusinessImpl(name: "businessImpl"), Person)
    assert p?.name == "businessImpl"
    BusinessInterface bi = nomin.map(new Person(name: "personName"), new BusinessImpl())
    assert bi?.name == "personName"
  }
}

interface BusinessInterface {
  String getName();
  void setName(String name);
}

class BusinessImpl implements BusinessInterface {
  String name;
}

class MappingInterface extends Mapping {
  protected void build() {
    mappingFor a: BusinessInterface, b: Person
    a.name = b.name
  }
}