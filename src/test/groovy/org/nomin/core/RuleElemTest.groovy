package org.nomin.core

import org.nomin.entity.*

/**
 * Tests all the rule elements.
 * @author Dmitry Dobrynin
 * Date: 22.10.2010 Time: 9:10:24
 */
class RuleElemTest implements MappingConsts {

  @org.junit.Test
  void testDeepProperty() {
    PropRuleElem birth = new PropRuleElem(jb.property("birth", Details))
    PropRuleElem details = new PropRuleElem(jb.property("details", Employee))
    details.next = birth

    assert !details.get(null) && !birth.get(null)
    def now = new Date()
    def result = details.set(null, now)
    assert Employee.isInstance(result)
    Employee e = result
    assert e.details && e.details.birth == now
  }

  @org.junit.Test
  void testDeepCollectionProperty() {
    CollectionRuleElem kids = new CollectionRuleElem(jb.property("kids", Details))
    PropRuleElem details = new PropRuleElem(jb.property("details", Employee))
    details.next = kids

    assert !details.get(null) && !kids.get(null)
    def result = details.set(null, [new Kid("John"), new Kid("Mary"), new Kid("Andrew")])
    assert Employee.isInstance(result)
    Employee e = result
    assert e.details && e.details.kids && e.details.kids.size() == 3 &&
            e.details.kids.findAll { ["John", "Mary", "Andrew"].contains(it.kidName) }.size() == 3
  }

  @org.junit.Test
  void testArrayProperty() {
    CollectionRuleElem items = new CollectionRuleElem(jb.property("items", Order))

    assert !items.get(null)
    def result = items.set(null, [new OrderItem(description: "d1"), new OrderItem(description: "d2")])
    assert Order.isInstance(result)
    Order o = result
    assert o.items && o.items.length == 2 && o.items[0].description == "d1" && o.items[1].description == "d2"
  }

  @org.junit.Test
  void testMapProperty() {
    CollectionRuleElem options = new CollectionRuleElem(jb.property("options", Person))

    assert !options.get(null)
    def result = options.set(null, [a: "Option A", b: "Option B"])
    assert Person.isInstance(result)
    Person p = result
    assert p.options?.size() == 2 && p.options["a"] == "Option A" && p.options["b"] == "Option B"
  }

  @org.junit.Test
  void testSeqRuleElemOnArray() {
    CollectionRuleElem items = new CollectionRuleElem(jb.property("items", Order))
    SeqRuleElem seq = new SeqRuleElem(0, items.containerHelper)
    items.next = seq

    assert !items.get(null) && !seq.get(null)
    def result = items.set(null, new OrderItem(description: "orderItem"))
    assert Order.isInstance(result)
    Order o = result
    assert o.items?.length == 1 && o.items[0].description == "orderItem"
  }
}
