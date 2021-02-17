package com.example.demo.services;

public interface StockService {
    StockRecommendation getStockRecommendations(int productId, String retailer);
    void updateMinimumStockLevel(int productId, int minStock);
    void addToCurrentStockLevel(int productId, int stockToAdd);
    void markProductBlocked(int productId);
}
