package com.example.demo.controllers;

import com.example.demo.services.StockRecommendation;
import com.example.demo.services.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StockController {

    @Autowired
    private StockService stockService;

    @GetMapping("/recommendation/{productId}")
    public StockRecommendation getStockRecommendations(@PathVariable("productId") int productId,
                                                           @RequestParam("retailer") String retailer) {
        return stockService.getStockRecommendations(productId, retailer);
    }

    @PutMapping("/update/minStockLevel/{productId}/{minStockLevel}")
    public void updateMinimumStockLevel(@PathVariable("productId") Integer productId,
                                        @PathVariable("minStockLevel") Integer minStockLevel) {
        stockService.updateMinimumStockLevel(productId, minStockLevel);
    }

    @PutMapping("/add/stockLevel/{productId}/{stockLevel}")
    public void addToCurrentStockLevel(@PathVariable("productId") Integer productId,
                                        @PathVariable("stockLevel") Integer stockLevel) {
        stockService.addToCurrentStockLevel(productId, stockLevel);
    }

    @PutMapping("/markBlocked/{productId}")
    public void markProductBlocked(@PathVariable("productId") Integer productId) {
        stockService.markProductBlocked(productId);
    }

}
