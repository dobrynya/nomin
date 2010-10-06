package org.nomin.integration

import org.nomin.NominMapper
import org.nomin.core.Nomin
import org.junit.*

import org.nomin.mappings.Order2CompletedOrder
import org.nomin.mappings.Item2CompletedItem
import org.nomin.mappings.Order2CompletedOrderIndexed
import org.nomin.entity.CompletedItem
import org.nomin.entity.OrderItem
import org.nomin.entity.CompletedOrder
import org.nomin.entity.Order

/**
 * Tests cases when mapping arrays.
 * @author Dmitry Dobrynin
 * Created: 16.05.2010 0:10:34
 */
class MappingArraysTest {
  NominMapper mapper = new Nomin();

  @Test
  void testDirect() {
    mapper.parse Order2CompletedOrder, Item2CompletedItem
    def order = new Order(items: [new OrderItem(description: "Snowboard", amount: 5, price: 5),
            new OrderItem(description: "Ski", amount: 1, price: 15)])
    CompletedOrder co = mapper.map(order, CompletedOrder)
    assert co && co.items?.size() == 2 && co.items[0] instanceof CompletedItem && co.items[0] instanceof CompletedItem
  }

  @Test
  void testReverse() {
    mapper.parse Order2CompletedOrder, Item2CompletedItem
    def co = new CompletedOrder(items: [new CompletedItem(description: "Snowboard", amount: 5, price: 5, total: 25),
            new CompletedItem(description: "Ski", amount: 1, price: 15, total: 15)])
    Order o = mapper.map(co, Order)
    assert o && o.items?.size() == 2 && o.items[0] instanceof OrderItem && o.items[0] instanceof OrderItem
  }

  @Test
  void testDirectIndexedAccess() {
    mapper.parse Order2CompletedOrderIndexed, Item2CompletedItem
    def order = new Order(items: [new OrderItem(description: "Snowboard", amount: 5, price: 5),
    new OrderItem(description: "Ski", amount: 1, price: 15), new OrderItem(description: "Bike", amount: 1, price: 300)])
    CompletedOrder co = mapper.map(order, CompletedOrder)
    assert co && co.items?.size() == 3 && co.items[0] instanceof CompletedItem && co.items[1] instanceof CompletedItem &&
            co.items[2] instanceof CompletedItem && co.items[2].description == order.items[2].description
  }

  @Test
  void testReverseIndexedAccess() {
    mapper.parse Order2CompletedOrderIndexed, Item2CompletedItem
    def co = new CompletedOrder(items: [new CompletedItem(description: "Snowboard", amount: 5, price: 5),
            new CompletedItem(description: "Ski", amount: 1, price: 15, total: 15),
    new CompletedItem(description: "Bike")])
    Order o = mapper.map(co, Order)
    assert o && o.items?.size() == 3 && o.items[0] instanceof OrderItem && o.items[1] instanceof OrderItem &&
            o.items[2] instanceof OrderItem && o.items[2].description == co.items[2].description
  }
}