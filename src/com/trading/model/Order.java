package com.trading.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Order implements Comparable<Order> {

    public enum OrderType { BUY, SELL }
    public enum Status { PENDING, EXECUTED, CANCELLED }

    private String orderId;
    private String symbol;
    private OrderType type;
    private double price;
    private int quantity;
    private Status status;
    private LocalDateTime timestamp;

    public Order(String symbol, OrderType type, double price, int quantity) {
        this.orderId   = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        this.symbol    = symbol;
        this.type      = type;
        this.price     = price;
        this.quantity  = quantity;
        this.status    = Status.PENDING;
        this.timestamp = LocalDateTime.now();
    }

    @Override
    public int compareTo(Order other) {
        return Double.compare(other.price, this.price);
    }

    public String getOrderId()          { return orderId; }
    public String getSymbol()           { return symbol; }
    public OrderType getType()          { return type; }
    public double getPrice()            { return price; }
    public int getQuantity()            { return quantity; }
    public Status getStatus()           { return status; }
    public LocalDateTime getTimestamp() { return timestamp; }

    public void setStatus(Status status)   { this.status = status; }
    public void setQuantity(int quantity)  { this.quantity = quantity; }

    @Override
    public String toString() {
        return String.format("[%s] %s %s | Qty: %d | Price: $%.2f | Status: %s",
                orderId, type, symbol, quantity, price, status);
    }
}
