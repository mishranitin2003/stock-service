package com.example.demo.services;

import java.util.ArrayList;
import java.util.List;

public class StockRecommendation {
    private String product;
    private List<String> recommendations = new ArrayList<>();

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public List<String> getRecommendations() {
        return recommendations;
    }

    public void setRecommendation(List<String> recommendations) {
        this.recommendations = recommendations;
    }

}
