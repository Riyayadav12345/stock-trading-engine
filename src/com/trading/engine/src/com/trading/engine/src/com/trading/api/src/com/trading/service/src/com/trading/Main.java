package com.trading;

import com.trading.api.StockPriceFetcher;
import com.trading.engine.MatchingEngine;
import com.trading.model.*;
import com.trading.service.PortfolioService;
import java.util.*;

public class Main {

    static Scanner scanner            = new Scanner(System.in);
    static MatchingEngine engine      = new MatchingEngine();
    static PortfolioService portfolio = new PortfolioService(10000.0);

    static List<Stock> marketStocks = new ArrayList<>(Arrays.asList(
        new Stock("AAPL",  "Apple Inc.",       178.50),
        new Stock("TSLA",  "Tesla Inc.",        245.30),
        new Stock("GOOGL", "Alphabet Inc.",     142.80),
        new Stock("MSFT",  "Microsoft Corp.",   415.20),
        new Stock("NVDA",  "NVIDIA Corp.",      875.40)
    ));

    public static void main(String[] args) {
        printBanner();
        StockPriceFetcher.startLiveFeed(marketStocks);

        while (true) {
            printMenu();
            System.out.print("> Enter choice: ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> showMarket();
                case "2" -> placeBuyOrder();
                case "3" -> placeSellOrder();
                case "4" -> showOrderBook();
                case "5" -> engine.printTradeHistory();
                case "6" -> portfolio.printPortfolio();
                case "0" -> { System.out.println("Goodbye! 👋"); System.exit(0); }
                default  -> System.out.println("❌ Invalid choice.");
            }
        }
    }

    static void printBanner() {
        System.out.println("╔══════════════════════════════════════════════╗");
        System.out.println("║   📈  REAL-TIME STOCK TRADING ENGINE  📉     ║");
        System.out.println("║          Built with Pure Java                ║");
        System.out.println("╚══════════════════════════════════════════════╝");
    }

    static void printMenu() {
        System.out.println("\n┌─────────────────────────────┐");
        System.out.println("│  1. 📊 View Market Prices   │");
        System.out.println("│  2. 🟢 Place BUY Order      │");
        System.out.println("│  3. 🔴 Place SELL Order     │");
        System.out.println("│  4. 📖 View Order Book      │");
        System.out.println("│  5. 📋 Trade History        │");
        System.out.println("│  6. 💼 My Portfolio         │");
        System.out.println("│  0. 🚪 Exit                 │");
        System.out.println("└─────────────────────────────┘");
    }

    static void showMarket() {
        System.out.println("\n📊 LIVE MARKET PRICES");
        System.out.println("─".repeat(70));
        marketStocks.forEach(System.out::println);
    }

    static void placeBuyOrder() {
        System.out.print("Enter symbol (e.g. AAPL): ");
        String symbol = scanner.nextLine().trim().toUpperCase();
        System.out.print("Enter quantity: ");
        int qty = Integer.parseInt(scanner.nextLine().trim());
        System.out.print("Enter limit price (or 0 for market price): ");
        double price = Double.parseDouble(scanner.nextLine().trim());
        if (price == 0) price = StockPriceFetcher.getLivePrice(symbol);

        Order order = new Order(symbol, Order.OrderType.BUY, price, qty);
        System.out.println("\n📥 Order placed: " + order);
        List<Trade> trades = engine.placeOrder(order);
        trades.forEach(t -> portfolio.processTrade(t, Order.OrderType.BUY));
    }

    static void placeSellOrder() {
        System.out.print("Enter symbol: ");
        String symbol = scanner.nextLine().trim().toUpperCase();
        System.out.print("Enter quantity: ");
        int qty = Integer.parseInt(scanner.nextLine().trim());
        System.out.print("Enter limit price (or 0 for market price): ");
        double price = Double.parseDouble(scanner.nextLine().trim());
        if (price == 0) price = StockPriceFetcher.getLivePrice(symbol);

        Order order = new Order(symbol, Order.OrderType.SELL, price, qty);
        System.out.println("\n📤 Order placed: " + order);
        List<Trade> trades = engine.placeOrder(order);
        trades.forEach(t -> portfolio.processTrade(t, Order.OrderType.SELL));
    }

    static void showOrderBook() {
        System.out.print("Enter symbol to view order book: ");
        String symbol = scanner.nextLine().trim().toUpperCase();
        engine.printOrderBook(symbol);
    }
}
