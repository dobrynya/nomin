package org.nomin.mappings

import org.nomin.Mapping
import org.nomin.entity.CompletedOrder
import org.nomin.entity.Order

/**
 * Just a mapping.
 * @author Dmitry Dobrynin
 * Created: 16.05.2010 12:03:57
 */
class Order2CompletedOrderIndexed extends Mapping {
  protected void build() {
    mappingFor a: Order, b: CompletedOrder
    a.items[0] = b.items[0]
    a.items[1] = b.items[1]
    a.items[2].description = b.items[2].description
  }
}
