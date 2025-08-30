package com.senibo.e_commerce_api.model.order;

public enum OrderStatus {
    PENDING("Pending"),           // Just created, payment pending
    CONFIRMED("Confirmed"),       // Payment successful
    PROCESSING("Processing"),     // Being prepared for shipment
    SHIPPED("Shipped"),          // In transit
    DELIVERED("Delivered"),      // Successfully delivered
    CANCELLED("Cancelled"),      // Cancelled by customer/admin
    REFUNDED("Refunded"),        // Payment refunded
    FAILED("Failed");            // Payment failed
    
    private final String displayName;
    
    OrderStatus(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
}
