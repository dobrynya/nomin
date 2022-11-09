package org.nomin.core;

import org.junit.Test;
import org.nomin.entity.OrderItem;

import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Tests for NominCache
 *
 * @author Rato
 *         Created 08.11.2022 07:39:51
 */
public class NominCacheTest {

    @Test
    public void cacheWithMultipleNomins() {
        Nomin nominOne = new Nomin("mappings/map2orderOne.groovy");
        Nomin nominTwo = new Nomin("mappings/map2orderTwo.groovy");

        Map<String, Object> source = Map.of("description", "Order Item");

        OrderItem item1 = nominOne.map(source, OrderItem.class);
        OrderItem item2 = nominTwo.map(source, OrderItem.class);

        assertEquals("Order Item", item1.getDescription());
        assertEquals(Integer.valueOf(1), item1.getAmount());
        assertEquals("Order Item", item2.getDescription());
        assertEquals(Integer.valueOf(2), item2.getAmount());
    }
}
