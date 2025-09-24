package com.senibo.e_commerce_api.model.cartItem;

import com.senibo.e_commerce_api.model.BaseEntity;
import com.senibo.e_commerce_api.model.cart.Cart;
import com.senibo.e_commerce_api.model.product.Product;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "cart_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItem extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;


    private Integer quantity;
}
