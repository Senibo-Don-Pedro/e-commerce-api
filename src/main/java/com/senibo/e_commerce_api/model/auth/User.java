package com.senibo.e_commerce_api.model.auth;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.senibo.e_commerce_api.model.BaseEntity;
import com.senibo.e_commerce_api.model.cart.Cart;
import com.senibo.e_commerce_api.model.order.Order;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"password"})
public class User extends BaseEntity {

    @Column(nullable = false)
    private String firstname;

    @Column(nullable = false)
    private String lastname;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    // For JWT authentication - can be null for OAuth-only users
    @Column
    @JsonIgnore
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private Role role = Role.CUSTOMER;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private Provider provider = Provider.LOCAL;

    //Relationships
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Cart cart;

    // A User can have MANY Orders, so this should be OneToMany
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Order> orders; // <-- Change to a List and plural name
}
