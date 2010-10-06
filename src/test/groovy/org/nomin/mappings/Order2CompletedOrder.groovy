package org.nomin.mappings

import org.nomin.Mapping
import org.nomin.entity.CompletedOrder
import org.nomin.entity.Order

/**
 * Just a mapping.
 * @author Dmitry Dobrynin
 * Created: 15.05.2010 23:11:17
 */
class Order2CompletedOrder extends Mapping {
  protected void build() {
    mappingFor a: Order, b: CompletedOrder
    a.items = b.items
  }
}
