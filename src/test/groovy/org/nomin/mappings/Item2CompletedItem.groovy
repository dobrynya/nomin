package org.nomin.mappings

import org.nomin.Mapping
import org.nomin.entity.CompletedItem
import org.nomin.entity.OrderItem

/**
 * Documentation.
 * @author Dmitry Dobrynin
 * Created: 16.05.2010 0:24:56
 */
class Item2CompletedItem extends Mapping {
  protected void build() {
    mappingFor a: OrderItem, b: CompletedItem
    a.description = b.description
    a.price = b.price
    a.amount = b.amount
    b.total = { a.amount * a.price }
  }
}
