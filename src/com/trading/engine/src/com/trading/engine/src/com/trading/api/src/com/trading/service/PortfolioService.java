package com.trading.service;

import com.trading.model.Order;
import com.trading.model.Trade;
import com.trading.api.StockPriceFetcher;
import java.util.*;

public class PortfolioService {

    private double cashBalance;
    private Map<String, Integer> holdings    = new HashMap<>();
    private Map<String, Double>  avgBuyPrice = new HashMap<>();

    public PortfolioService(double initialBalance) {
        this.cashBalance = initialBalance;
    }

    public boolean processTrade(Trade trade, Order.OrderType type) {
        double totalCost = trade.getExecutedPrice() * trade.getQuantity();
        String symbol    = trade.getSymbol();

        if (type == Order.OrderType.BUY) {
            if (cashBalance < totalCost) {
                System.out.println("❌ Insufficient funds! Need $"
                        + String.format("%.2f", totalCost)
                        + " | Available: $" + String.format("%.2f", cashBalance));
                return false;
            }
            cashBalance -= totalCost;
            holdings.merge(symbol, trade.getQuantity(), Integer::sum);

            double oldAvg = avgBuyPrice.getOrDefault(symbol, 0.0);
            int oldQty    = holdings.get(symbol) - trade.getQuantity();
            double newAvg = (oldAvg * oldQty + totalCost) / holdings.get(symbol);
            avgBuyPrice.put(symbol, newAvg);

        } else {
            int currentHolding = holdings.getOrDefault(symbol, 0);
            if (currentHolding < trade.getQuantity()) {
                System.out.println("❌ Not enough shares! Have: " + currentHolding);
                return false;
            }
            cashBalance += totalCost;
            holdings.merge(symbol, -trade.getQuantity(), Integer::sum);
            if (holdings.get(symbol) == 0) {
                holdings.remove(symbol);
                avgBuyPrice.remove(symbol);
            }
        }
        return true;
    }

    public void printPortfolio() {
        System.out.println("\n╔══════════════════════════════════════════════════════════╗");
        System.out.println("║                  📊 YOUR PORTFOLIO                       ║");
        System.out.println("╠══════════════════════════════════════════════════════════╣");
        System.out.printf("║  💵 Cash Balance : $%-38.2f║%n", cashBalance);
        System.out.println("╠══════════════════════════════════════════════════════════╣");

        double totalInvested = 0, totalCurrent = 0;

        if (holdings.isEmpty()) {
            System.out.println("║  No holdings yet.                                        ║");
        } else {
            System.out.println("║  SYMBOL  QTY    AVG BUY   CURR PRICE   P&L        P&L%  ║");
            System.out.println("║  ──────────────────────────────────────────────────────  ║");
            for (Map.Entry<String, Integer> entry : holdings.entrySet()) {
                String symbol  = entry.getKey();
                int qty        = entry.getValue();
                double avgBuy  = avgBuyPrice.get(symbol);
                double currPx  = StockPriceFetcher.getLivePrice(symbol);
                double invested = avgBuy * qty;
                double current  = currPx * qty;
                double pnl      = current - invested;
