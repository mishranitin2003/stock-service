package com.example.demo.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.Set;

@Entity
public class Retailer {

    @Id
    @GeneratedValue
    private Integer id;

    private String retailerName;

    @OneToMany(mappedBy = "retailer")
    private Set<Product> products;

    public Retailer() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRetailerName() {
        return retailerName;
    }

    public void setRetailerName(String retailer) {
        this.retailerName = retailer;
    }

    public Set<Product> getProducts() {
        return products;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }
}
