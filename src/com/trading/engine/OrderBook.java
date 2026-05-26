package com.trading.engine;

import com.trading.model.Order;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.List;
import java.util.ArrayList;

public class OrderBook {

    private String symbol;
    private PriorityQueue<Order> buyOrders;
    private PriorityQueue<Order> sellOrders;

    public OrderBook(String symbol) {
        this.symbol = symbol;
        this.buyOrders  = new PriorityQueue<>(Comparator.comparingDouble(Order::getPrice).reversed());
        this.sellOrders = new PriorityQueue<>(Comparator.comparingDouble(Order::getPrice));
    }

    public void addOrder(Order order) {
        if (order.getType() == Order.OrderType.BUY) {
            buyOrders.add(order);
        } else {
            sellOrders.add(order);
        }
    }

    public Order peekBest(Order.OrderType type) {
        return type == Order.OrderType.BUY ? buyOrders.peek() : sellOrders.peek();
    }

    public Order pollBest(Order.OrderType type) {
        return type == Order.OrderType.BUY ? buyOrders.poll() : sellOrders.poll();
    }

    public boolean hasBuyOrders()  { return !buyOrders.isEmpty(); }
    public boolean hasSellOrders() { return !sellOrders.isEmpty(); }
    public String getSymbol()      { return symbol; }

    public void printOrderBook() {
        System.out.println("\n╔══════════════════════════════════════╗");
        System.out.println("║       ORDER BOOK: " + symbol + "              ║");
        System.out.println("╠══════════════════════════════════════╣");

        List<Order> sells = new ArrayList<>(sellOrders);
        sells.sort(Comparator.comparingDouble(Order::getPrice).reversed());
        System.out.println("║  SELL ORDERS (Ask)                   ║");
        sells.stream().limit(5).forEach(o ->
            System.out.printf("║  %-6s  Qty:%-4d  $%-8.2f        ║%n",
                    o.getSymbol(), o.getQuantity(), o.getPrice()));

        System.out.println("║  ────────────── SPREAD ────────────  ║");

        List<Order> buys = new ArrayList<>(buyOrders);
        buys.sort(Comparator.comparingDouble(Order::getPrice).reversed());
        System.out.println("║  BUY ORDERS  (Bid)                   ║");
        buys.stream().limit(5).forEach(o ->
            System.out.printf("║  %-6s  Qty:%-4d  $%-8.2f        ║%n",
                    o.getSymbol(), o.getQuantity(), o.getPrice()));

        System.out.println("╚══════════════════════════════════════╝");
    }
}
