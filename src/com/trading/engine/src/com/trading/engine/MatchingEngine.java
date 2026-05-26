package com.trading.engine;

import com.trading.model.Order;
import com.trading.model.Trade;
import java.util.*;

public class MatchingEngine {

    private Map<String, OrderBook> orderBooks = new HashMap<>();
    private List<Trade> tradeHistory          = new ArrayList<>();

    public void addSymbol(String symbol) {
        orderBooks.put(symbol, new OrderBook(symbol));
    }

    public List<Trade> placeOrder(Order order) {
        String symbol = order.getSymbol();
        orderBooks.putIfAbsent(symbol, new OrderBook(symbol));
        OrderBook book = orderBooks.get(symbol);
        book.addOrder(order);
        return matchOrders(book);
    }

    private List<Trade> matchOrders(OrderBook book) {
        List<Trade> newTrades = new ArrayList<>();

        while (book.hasBuyOrders() && book.hasSellOrders()) {
            Order bestBuy  = book.peekBest(Order.OrderType.BUY);
            Order bestSell = book.peekBest(Order.OrderType.SELL);

            if (bestBuy.getPrice() >= bestSell.getPrice()) {
                book.pollBest(Order.OrderType.BUY);
                book.pollBest(Order.OrderType.SELL);

                int matchedQty       = Math.min(bestBuy.getQuantity(), bestSell.getQuantity());
                double executedPrice = bestSell.getPrice();

                Trade trade = new Trade(bestBuy.getSymbol(), executedPrice, matchedQty, Order.OrderType.BUY);
                tradeHistory.add(trade);
                newTrades.add(trade);

                System.out.println("\n✅ TRADE EXECUTED: " + trade);

                int buyRemainder  = bestBuy.getQuantity()  - matchedQty;
                int sellRemainder = bestSell.getQuantity() - matchedQty;

                if (buyRemainder > 0) {
                    bestBuy.setQuantity(buyRemainder);
                    book.addOrder(bestBuy);
                } else {
                    bestBuy.setStatus(Order.Status.EXECUTED);
                }

                if (sellRemainder > 0) {
                    bestSell.setQuantity(sellRemainder);
                    book.addOrder(bestSell);
                } else {
                    bestSell.setStatus(Order.Status.EXECUTED);
                }

            } else {
                break;
            }
        }
        return newTrades;
    }

    public void printOrderBook(String symbol) {
        OrderBook book = orderBooks.get(symbol);
        if (book != null) book.printOrderBook();
        else System.out.println("No order book found for " + symbol);
    }

    public void printTradeHistory() {
        System.out.println("\n📋 TRADE HISTORY");
        System.out.println("═".repeat(60));
        if (tradeHistory.isEmpty()) {
            System.out.println("No trades executed yet.");
        } else {
            tradeHistory.forEach(System.out::println);
        }
    }

    public List<Trade> getTradeHistory() { return tradeHistory; }
}
