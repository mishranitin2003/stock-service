package com.example.demo.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Audited
@Entity
public class Product {

    @Id
    @GeneratedValue
    private Integer id;

    private String product;

    @ManyToOne
    @JoinColumn(name="retailer_id")
    @NotAudited
    private Retailer retailer;

    private Integer currentStock;
    private Integer minStock;
    private Integer oneOffOrder;
    private Boolean stockBlocked;

    public Product() {}

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public Retailer getRetailer() {
        return retailer;
    }

    public void setRetailer(Retailer retailer) {
        this.retailer = retailer;
    }

    public Integer getCurrentStock() {
        return currentStock;
    }

    public void setCurrentStock(Integer currentStock) {
        this.currentStock = currentStock;
    }

    public Integer getMinStock() {
        return minStock;
    }

    public void setMinStock(Integer minStock) {
        this.minStock = minStock;
    }

    public Integer getOneOffOrder() {
        return oneOffOrder;
    }

    public void setOneOffOrder(Integer maxStock) {
        this.oneOffOrder = maxStock;
    }

    public Boolean getStockBlocked() {
        return stockBlocked;
    }

    public void setStockBlocked(Boolean stockBlocked) {
        this.stockBlocked = stockBlocked;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Product product = (Product) o;

        return new EqualsBuilder()
                .append(id, product.id)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .toHashCode();
    }
}
