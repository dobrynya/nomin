package org.nomin.entity;

/**
 * Represents a business entity.
 * @author Dmitry Dobrynin
 *         Created: 15.05.2010 23:07:27
 */
public class OrderItem {
    private String description;
    private Integer amount;
    private Integer price;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }
}