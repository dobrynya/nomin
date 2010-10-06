package org.nomin.entity;

import java.util.List;

/**
 * Documentation.
 *
 * @author Dmitry Dobrynin
 *         Created: 15.05.2010 23:10:06
 */
public class CompletedOrder {
    private List<CompletedItem> items;

    public List<CompletedItem> getItems() {
        return items;
    }

    public void setItems(List<CompletedItem> items) {
        this.items = items;
    }
}
