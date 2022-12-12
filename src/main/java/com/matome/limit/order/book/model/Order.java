package com.matome.limit.order.book.model;

import com.matome.limit.order.book.enums.Side;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

@Entity
public class Order implements Serializable {
    private static final long serialVersionUID = -5208838874932414502L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private BigDecimal price;
    private int quantity;

    @Enumerated(EnumType.STRING)
    private Side side;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDate timestamp;

    private int tradeCount;

    public Order() {

    }

    public Order(Long id, BigDecimal price, int quantity, Side side, LocalDate timestamp) {
        this.id = id;
        this.price = price;
        this.quantity = quantity;
        this.side = side;
        this.timestamp = timestamp;
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Side getSide() {
        return side;
    }

    public void setSide(Side side) {
        this.side = side;
    }

    public LocalDate getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDate timestamp) {
        this.timestamp = timestamp;
    }

    public int getTradeCount() {
        return tradeCount;
    }

    public void setTradeCount(int tradeCount) {
        this.tradeCount = tradeCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Order)) return false;
        Order order = (Order) o;
        return getQuantity() == order.getQuantity() && getTradeCount() == order.getTradeCount() && Objects.equals(getId(), order.getId()) && Objects.equals(getPrice(), order.getPrice()) && getSide() == order.getSide() && Objects.equals(getTimestamp(), order.getTimestamp());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getPrice(), getQuantity(), getSide(), getTimestamp(), getTradeCount());
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", price=" + price +
                ", quantity=" + quantity +
                ", side=" + side +
                ", timestamp=" + timestamp +
                ", tradeCount=" + tradeCount +
                '}';
    }
}
