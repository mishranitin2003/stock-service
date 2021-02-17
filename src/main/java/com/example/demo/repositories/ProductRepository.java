package com.example.demo.repositories;

import com.example.demo.domain.Product;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends CrudRepository<Product, Integer> {

    @Query("FROM Product p WHERE p.product = :product AND p.retailer.retailerName = :retailerName")
    List<Product> findByProductAndRetailer(@Param("product") String product, @Param("retailerName")String retailerName);
}
