package com.senibo.e_commerce_api.model.orderItem;

import com.senibo.e_commerce_api.model.BaseEntity;
import com.senibo.e_commerce_api.model.order.Order;
import com.senibo.e_commerce_api.model.product.Product;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "order_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItem extends BaseEntity {

    private Integer quantity;
    private BigDecimal pricePerUnit;

    //Relationships
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
}
