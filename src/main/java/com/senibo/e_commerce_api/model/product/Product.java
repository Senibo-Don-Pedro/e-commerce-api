package com.senibo.e_commerce_api.model.product;

import com.senibo.e_commerce_api.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Product extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private Integer stockQuantity;

    @Column(nullable = false, length = 100)
    @Enumerated(EnumType.STRING)
    private ProductCategory category;

    private String imageUrl;

    @Column(unique = true, nullable = false)
    private String sku;

}
