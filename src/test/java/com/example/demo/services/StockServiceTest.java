package com.example.demo.services;

import com.example.demo.domain.Product;
import com.example.demo.domain.Retailer;
import com.example.demo.repositories.ProductRepository;
import com.example.demo.services.impl.StockServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.MessageSource;

import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class StockServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private StockServiceImpl serviceToTest;

    @Test
    public void testGetStockRecommendationsForValidProductAndStock() {
        Product product = new Product();
        product.setProduct("a");
        Retailer retailer = new Retailer();
        retailer.setRetailerName("Hello-Retailer");
        product.setRetailer(retailer);
        product.setCurrentStock(5);
        product.setMinStock(4);
        product.setStockBlocked(false);

        when(productRepository.findByProductAndRetailer(anyInt(), anyString()))
                .thenReturn(Stream.of(product).collect(Collectors.toList()));

        StockRecommendation recommendation = serviceToTest.getStockRecommendations(1, "Hello-Retailer");

        verify(productRepository).findByProductAndRetailer(anyInt(), anyString());

        assertTrue(recommendation.getRecommendations().isEmpty());
        assertEquals(product.getProduct(), recommendation.getProduct());
    }

    @Test
    public void testGetStockRecommendationsForValidProductAndInvalidStockAndOneOffOrder() {
        Product product = new Product();
        product.setProduct("d");
        Retailer retailer = new Retailer();
        retailer.setRetailerName("Hello-Retailer");
        product.setRetailer(retailer);
        product.setCurrentStock(0);
        product.setMinStock(8);
        product.setOneOffOrder(15);
        product.setStockBlocked(false);

        Mockito.when(productRepository.findByProductAndRetailer(anyInt(), anyString()))
                .thenReturn(Stream.of(product).collect(Collectors.toList()));

        Mockito.when(messageSource.getMessage(anyString(), (Object[])any(), any(Locale.class)))
                .thenReturn("Product d has a minimum stock level of 8").thenReturn("Product d has a one-off order of 15");

        StockRecommendation recommendation = serviceToTest.getStockRecommendations(4, "Hello-Retailer");

        verify(productRepository).findByProductAndRetailer(anyInt(), anyString());

        assertEquals(2, recommendation.getRecommendations().size());
        assertSame("Product d has a minimum stock level of 8", recommendation.getRecommendations().get(0));
        assertSame("Product d has a one-off order of 15", recommendation.getRecommendations().get(1));
    }

    @Test
    public void testGetStockRecommendationsForValidProductAndInvalidStockAndBlocked() {
        Product product = new Product();
        product.setProduct("c");
        Retailer retailer = new Retailer();
        retailer.setRetailerName("Hello-Retailer");
        product.setRetailer(retailer);
        product.setCurrentStock(2);
        product.setMinStock(4);
        product.setStockBlocked(true);

        when(productRepository.findByProductAndRetailer(anyInt(), anyString()))
                .thenReturn(Stream.of(product).collect(Collectors.toList()));

        when(messageSource.getMessage(anyString(), (Object[])any(), any(Locale.class)))
                .thenReturn("Product c is blocked and new stock should not be ordered").thenReturn("Product c has a minimum stock level of 4");

        StockRecommendation recommendation = serviceToTest.getStockRecommendations(3, "Hello-Retailer");

        verify(productRepository).findByProductAndRetailer(anyInt(), anyString());

        assertEquals(2, recommendation.getRecommendations().size());
        assertSame("Product c is blocked and new stock should not be ordered", recommendation.getRecommendations().get(0));
        assertSame("Product c has a minimum stock level of 4", recommendation.getRecommendations().get(1));
    }

    @Test
    public void testUpdateMinimumStockLevel() {
        Product product = new Product();
        product.setProduct("a");
        Retailer retailer = new Retailer();
        retailer.setRetailerName("Hello-Retailer");
        product.setRetailer(retailer);
        product.setMinStock(4);

        when(productRepository.findById(anyInt())).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        serviceToTest.updateMinimumStockLevel(1, 6);
        verify(productRepository).findById(anyInt());
        verify(productRepository).save(any(Product.class));

        assertEquals(Integer.valueOf(6), product.getMinStock());
    }

    @Test(expected = ProductNotFoundException.class)
    public void testUpdateMinimumStockLevelWhenProductNotFound() {
        Product product = new Product();
        product.setProduct("a");
        Retailer retailer = new Retailer();
        retailer.setRetailerName("Hello-Retailer");
        product.setRetailer(retailer);
        product.setMinStock(4);

        when(productRepository.findById(anyInt())).thenReturn(Optional.empty());

        serviceToTest.updateMinimumStockLevel(1, -1);
        verify(productRepository).findById(anyInt());
    }

    @Test(expected = InsufficientStockQuantityException.class)
    public void testUpdateMinimumStockLevelWhenInvalidQuantityPassed() {
        Product product = new Product();
        product.setProduct("a");
        Retailer retailer = new Retailer();
        retailer.setRetailerName("Hello-Retailer");
        product.setRetailer(retailer);
        product.setMinStock(4);

        when(productRepository.findById(anyInt())).thenReturn(Optional.of(product));

        serviceToTest.updateMinimumStockLevel(1, -1);
        verify(productRepository).findById(anyInt());
    }

    @Test(expected = InsufficientStockQuantityException.class)
    public void testUpdateMinimumStockLevelWhenProductIsBlocked() {
        Product product = new Product();
        product.setProduct("a");
        Retailer retailer = new Retailer();
        retailer.setRetailerName("Hello-Retailer");
        product.setRetailer(retailer);
        product.setMinStock(4);
        product.setStockBlocked(true);

        when(productRepository.findById(anyInt())).thenReturn(Optional.of(product));

        serviceToTest.updateMinimumStockLevel(1, 6);
        verify(productRepository).findById(anyInt());
    }

    @Test
    public void testMarkProductBlocked() {
        Product product = new Product();
        product.setProduct("a");
        Retailer retailer = new Retailer();
        retailer.setRetailerName("Hello-Retailer");
        product.setRetailer(retailer);
        product.setMinStock(4);

        when(productRepository.findById(anyInt())).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        serviceToTest.markProductBlocked(1);
        verify(productRepository).findById(anyInt());
        verify(productRepository).save(any(Product.class));

        assertEquals(true, product.getStockBlocked().booleanValue());
    }

    @Test
    public void testAddToCurrentStockLevel() {
        Product product = new Product();
        product.setProduct("a");
        Retailer retailer = new Retailer();
        retailer.setRetailerName("Hello-Retailer");
        product.setRetailer(retailer);
        product.setMinStock(4);
        product.setCurrentStock(4);

        when(productRepository.findById(anyInt())).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        serviceToTest.addToCurrentStockLevel(1, 6);
        verify(productRepository).findById(anyInt());
        verify(productRepository).save(any(Product.class));

        assertEquals(Integer.valueOf(10), product.getCurrentStock());
    }

    @Test(expected = InsufficientStockQuantityException.class)
    public void testAddToCurrentStockLevelWhenOneOffOrder() {
        Product product = new Product();
        product.setProduct("a");
        Retailer retailer = new Retailer();
        retailer.setRetailerName("Hello-Retailer");
        product.setRetailer(retailer);
        product.setMinStock(4);
        product.setCurrentStock(4);
        product.setOneOffOrder(15);

        when(productRepository.findById(anyInt())).thenReturn(Optional.of(product));

        serviceToTest.addToCurrentStockLevel(1, 6);
        verify(productRepository).findById(anyInt());
    }
}
