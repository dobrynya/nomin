package org.nomin.entity;

/**
 * Represents a business entity.
 * @author Dmitry Dobrynin
 *         Created: 15.05.2010 23:06:41
 */
public class Order {

    private OrderItem[] items;

    public OrderItem[] getItems() {
        return items;
    }

    public void setItems(OrderItem[] items) {
        this.items = items;
    }
}
