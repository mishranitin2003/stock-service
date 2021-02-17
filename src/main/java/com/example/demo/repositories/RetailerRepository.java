package com.example.demo.repositories;

import com.example.demo.domain.Retailer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RetailerRepository extends CrudRepository<Retailer, Integer> {
}
