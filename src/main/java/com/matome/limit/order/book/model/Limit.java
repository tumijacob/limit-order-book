package com.matome.limit.order.book.model;

public final class Limit {
    private int price;
    private long quantity;
    private int orders;

    private Limit leftSibling;
    private Limit rightSibling;

    private long lastAdded;

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

    public int getOrders() {
        return orders;
    }

    public void setOrders(int orders) {
        this.orders = orders;
    }

    public Limit getLeftSibling() {
        return leftSibling;
    }

    public void setLeftSibling(Limit leftSibling) {
        this.leftSibling = leftSibling;
    }

    public Limit getRightSibling() {
        return rightSibling;
    }

    public void setRightSibling(Limit rightSibling) {
        this.rightSibling = rightSibling;
    }

    public long getLastAdded() {
        return lastAdded;
    }

    public void setLastAdded(long lastAdded) {
        this.lastAdded = lastAdded;
    }
}