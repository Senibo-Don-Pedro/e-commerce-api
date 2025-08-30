package com.senibo.e_commerce_api.model.order;

import com.senibo.e_commerce_api.model.BaseEntity;
import com.senibo.e_commerce_api.model.auth.User;
import com.senibo.e_commerce_api.model.payment.Payment;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders", indexes = {
    @Index(name = "idx_order_user", columnList = "user_id"),
    @Index(name = "idx_order_status", columnList = "status"),
    @Index(name = "idx_order_number", columnList = "order_number"),
    @Index(name = "idx_order_date", columnList = "order_date")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"user", "items", "payment"})
public class Order extends BaseEntity {
    
    @Column(name = "order_number", unique = true, nullable = false)
    private String orderNumber; // Human-readable order number (e.g., ORD-2024-001234)
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @Column(name = "order_date", nullable = false)
    @Builder.Default
    private LocalDateTime orderDate = LocalDateTime.now();
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private OrderStatus status = OrderStatus.PENDING;
    
    // Pricing
    @Column(name = "subtotal", nullable = false, precision = 10, scale = 2)
    private BigDecimal subtotal;
    
    @Column(name = "tax_amount", nullable = false, precision = 10, scale = 2)
    @Builder.Default
    private BigDecimal taxAmount = BigDecimal.ZERO;
    
    @Column(name = "shipping_amount", nullable = false, precision = 10, scale = 2)
    @Builder.Default
    private BigDecimal shippingAmount = BigDecimal.ZERO;
    
    @Column(name = "discount_amount", nullable = false, precision = 10, scale = 2)
    @Builder.Default
    private BigDecimal discountAmount = BigDecimal.ZERO;
    
    @Column(name = "total_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;
    
    // Shipping Information
    @Column(name = "shipping_address_line1", nullable = false)
    private String shippingAddressLine1;
    
    @Column(name = "shipping_address_line2")
    private String shippingAddressLine2;
    
    @Column(name = "shipping_city", nullable = false)
    private String shippingCity;
    
    @Column(name = "shipping_state", nullable = false)
    private String shippingState;
    
    @Column(name = "shipping_postal_code", nullable = false)
    private String shippingPostalCode;
    
    @Column(name = "shipping_country", nullable = false)
    private String shippingCountry;
    
    // Billing Information (can be same as shipping)
    @Column(name = "billing_address_line1", nullable = false)
    private String billingAddressLine1;
    
    @Column(name = "billing_address_line2")
    private String billingAddressLine2;
    
    @Column(name = "billing_city", nullable = false)
    private String billingCity;
    
    @Column(name = "billing_state", nullable = false)
    private String billingState;
    
    @Column(name = "billing_postal_code", nullable = false)
    private String billingPostalCode;
    
    @Column(name = "billing_country", nullable = false)
    private String billingCountry;
    
    // Tracking
    @Column(name = "tracking_number")
    private String trackingNumber;
    
    @Column(name = "shipped_date")
    private LocalDateTime shippedDate;
    
    @Column(name = "delivered_date")
    private LocalDateTime deliveredDate;
    
    // Notes
    @Column(name = "customer_notes", columnDefinition = "TEXT")
    private String customerNotes;
    
    @Column(name = "internal_notes", columnDefinition = "TEXT")
    private String internalNotes; // Admin-only notes
    
    // Relationships
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<OrderItem> items = new ArrayList<>();
    
    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
    private Payment payment;
    
    // Helper methods
    public void calculateTotalAmount() {
        this.totalAmount = subtotal
            .add(taxAmount)
            .add(shippingAmount)
            .subtract(discountAmount);
    }
    
    public boolean canBeCancelled() {
        return status == OrderStatus.PENDING || status == OrderStatus.CONFIRMED;
    }
    
    public boolean isShippable() {
        return status == OrderStatus.CONFIRMED || status == OrderStatus.PROCESSING;
    }
    
    public int getTotalItems() {
        return items.stream().mapToInt(OrderItem::getQuantity).sum();
    }
}
