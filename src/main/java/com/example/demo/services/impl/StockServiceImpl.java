package com.example.demo.services.impl;

import com.example.demo.domain.Product;
import com.example.demo.repositories.ProductRepository;
import com.example.demo.services.InsufficientStockQuantityException;
import com.example.demo.services.ProductNotFoundException;
import com.example.demo.services.StockRecommendation;
import com.example.demo.services.StockService;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
@Transactional
public class StockServiceImpl implements StockService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MessageSource messageSource;

    @Override
    public StockRecommendation getStockRecommendations(String product, String retailer) {
        StockRecommendation recommendation = new StockRecommendation();
        recommendation.setProduct(product);

        List<String> recommendations = recommendation.getRecommendations();

        List<Product> products = productRepository.findByProductAndRetailer(product, retailer);
        if (products.isEmpty()) {
            throw new ProductNotFoundException();
        }
        products.stream().findFirst().ifPresent(p -> {
            Integer currentStock = p.getCurrentStock();
            if (p.getStockBlocked()) {
                recommendations.add(messageSource.getMessage("BLOCK_NO_NEW_STOCK", new Object[]{p.getProduct()}, Locale.getDefault()));
            }

            if (p.getMinStock() != null && currentStock < p.getMinStock()) {
                recommendations.add(messageSource.getMessage("MIN_STOCK_LEVEL", new Object[]{p.getProduct(), p.getMinStock()}, Locale.getDefault()));
                if (p.getOneOffOrder() != null) {
                    recommendations.add(messageSource.getMessage("ONE_OFF_ORDER", new Object[]{p.getProduct(), p.getOneOffOrder()}, Locale.getDefault()));
                }
            }
        });

        return recommendation;
    }

    @Override
    public void updateMinimumStockLevel(int productId, int minStock) {
        Optional<Product> product = getProduct(productId);
        Product productToUpdate = product.get();
        if (BooleanUtils.isTrue(productToUpdate.getStockBlocked()) || minStock < 1) {
            throw new InsufficientStockQuantityException();
        }
        Product toUpdate = productToUpdate;
        toUpdate.setMinStock(minStock);
        productRepository.save(toUpdate);
    }

    @Override
    public void addToCurrentStockLevel(int productId, int stockToAdd) {
        Optional<Product> product = getProduct(productId);

        Product productToUpdate = product.get();
        if (BooleanUtils.isTrue(productToUpdate.getStockBlocked()) ||
                (productToUpdate.getOneOffOrder() != null && stockToAdd != productToUpdate.getOneOffOrder())) {
            throw new InsufficientStockQuantityException();
        }

        if (stockToAdd > 0) {
            int currentStock = productToUpdate.getCurrentStock() + stockToAdd;

            productToUpdate.setCurrentStock(currentStock);
            productRepository.save(productToUpdate);
        }
    }

    @Override
    public void markProductBlocked(int productId) {
        Optional<Product> product = getProduct(productId);

        Product toUpdate = product.get();
        toUpdate.setStockBlocked(true);
        productRepository.save(toUpdate);
    }

    private Optional<Product> getProduct(int productId) {
        Optional<Product> product = productRepository.findById(productId);
        if (!product.isPresent()) {
            throw new ProductNotFoundException();
        }
        return product;
    }

}
