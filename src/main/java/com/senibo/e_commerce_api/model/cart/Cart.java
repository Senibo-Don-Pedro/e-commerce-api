package com.senibo.e_commerce_api.model.cart;

import com.senibo.e_commerce_api.model.BaseEntity;
import com.senibo.e_commerce_api.model.auth.User;
import com.senibo.e_commerce_api.model.cartItem.CartItem;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "carts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cart extends BaseEntity {

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> cartItems = new ArrayList<>(); // <-- THE FIX
}
