package com.trading.api;

import com.trading.model.Stock;
import java.util.*;
import java.util.concurrent.*;

public class StockPriceFetcher {

    private static final Map<String, Double> MOCK_PRICES = new HashMap<>();
    private static final Random random = new Random();

    static {
        MOCK_PRICES.put("AAPL",  178.50);
        MOCK_PRICES.put("TSLA",  245.30);
        MOCK_PRICES.put("GOOGL", 142.80);
        MOCK_PRICES.put("MSFT",  415.20);
        MOCK_PRICES.put("AMZN",  185.60);
        MOCK_PRICES.put("NVDA",  875.40);
        MOCK_PRICES.put("META",  520.10);
    }

    public static double getLivePrice(String symbol) {
        double basePrice = MOCK_PRICES.getOrDefault(symbol.toUpperCase(), 100.0);
        double change    = basePrice * (random.nextDouble() * 0.04 - 0.02);
        double newPrice  = Math.round((basePrice + change) * 100.0) / 100.0;
        MOCK_PRICES.put(symbol.toUpperCase(), newPrice);
        return newPrice;
    }

    public static ScheduledFuture<?> startLiveFeed(List<Stock> stocks) {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        return scheduler.scheduleAtFixedRate(() -> {
            System.out.println("\n🔴 LIVE PRICES — "
                    + java.time.LocalTime.now().toString().substring(0, 8));
            System.out.println("─".repeat(70));
            for (Stock stock : stocks) {
                double newPrice = getLivePrice(stock.getSymbol());
                stock.updatePrice(newPrice);
                System.out.println(stock);
            }
            System.out.print("\n> Enter choice: ");
        }, 0, 10, TimeUnit.SECONDS);
    }

    public static Map<String, Double> getAllPrices() {
        return Collections.unmodifiableMap(MOCK_PRICES);
    }
}
