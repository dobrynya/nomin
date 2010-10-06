package org.nomin.entity;

/**
 * Documentation.
 *
 * @author Dmitry Dobrynin
 *         Created: 16.05.2010 0:19:38
 */
public class CompletedItem {
    private String description;
    private Integer amount;
    private Double price;
    private Double total;

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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }
}
