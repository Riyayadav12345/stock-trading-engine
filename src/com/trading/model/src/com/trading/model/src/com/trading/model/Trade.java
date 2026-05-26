package com.trading.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Trade {

    private String tradeId;
    private String symbol;
    private double executedPrice;
    private int quantity;
    private Order.OrderType type;
    private LocalDateTime executedAt;

    private static int counter = 1;

    public Trade(String symbol, double executedPrice, int quantity, Order.OrderType type) {
        this.tradeId       = "TRD" + String.format("%04d", counter++);
        this.symbol        = symbol;
        this.executedPrice = executedPrice;
        this.quantity      = quantity;
        this.type          = type;
        this.executedAt    = LocalDateTime.now();
    }

    public double getTotalValue()    { return executedPrice * quantity; }
    public String getSymbol()        { return symbol; }
    public double getExecutedPrice() { return executedPrice; }
    public int getQuantity()         { return quantity; }
    public Order.OrderType getType() { return type; }

    @Override
    public String toString() {
        return String.format("[%s] %s %s x%d @ $%.2f = $%.2f | %s",
                tradeId, type, symbol, quantity, executedPrice,
                getTotalValue(),
                executedAt.format(DateTimeFormatter.ofPattern("HH:mm:ss")));
    }
}
