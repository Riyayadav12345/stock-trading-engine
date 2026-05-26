package com.trading.model;

public class Stock {

    private String symbol;
    private String companyName;
    private double currentPrice;
    private double previousPrice;
    private double dayHigh;
    private double dayLow;

    public Stock(String symbol, String companyName, double currentPrice) {
        this.symbol        = symbol;
        this.companyName   = companyName;
        this.currentPrice  = currentPrice;
        this.previousPrice = currentPrice;
        this.dayHigh       = currentPrice;
        this.dayLow        = currentPrice;
    }

    public void updatePrice(double newPrice) {
        this.previousPrice = this.currentPrice;
        this.currentPrice  = newPrice;
        if (newPrice > dayHigh) dayHigh = newPrice;
        if (newPrice < dayLow)  dayLow  = newPrice;
    }

    public double getChangePercent() {
        return ((currentPrice - previousPrice) / previousPrice) * 100;
    }

    public String getSymbol()       { return symbol; }
    public String getCompanyName()  { return companyName; }
    public double getCurrentPrice() { return currentPrice; }
    public double getDayHigh()      { return dayHigh; }
    public double getDayLow()       { return dayLow; }

    @Override
    public String toString() {
        String arrow = currentPrice >= previousPrice ? "▲" : "▼";
        return String.format("%-6s | %-20s | $%-8.2f | %s %.2f%% | H:$%.2f L:$%.2f",
                symbol, companyName, currentPrice, arrow,
                Math.abs(getChangePercent()), dayHigh, dayLow);
    }
}
