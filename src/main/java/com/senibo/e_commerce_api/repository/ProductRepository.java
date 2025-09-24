package com.senibo.e_commerce_api.repository;

import com.senibo.e_commerce_api.model.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID>,
        JpaSpecificationExecutor<Product> {


    /**
     * Add this new method for an efficient uniqueness check
     */
    boolean existsBySku(String sku);

    /**
     * Checks to see if the product exists by name
     */
    boolean existsByName(String name); // <-- Add this


}
