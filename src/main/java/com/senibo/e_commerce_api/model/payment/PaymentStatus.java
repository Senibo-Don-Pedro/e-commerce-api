package com.senibo.e_commerce_api.model.payment;

public enum PaymentStatus {
    PENDING("Pending"),           // Payment initiated
    PROCESSING("Processing"),     // Being processed
    COMPLETED("Success"),         // Payment successful  
    FAILED("Failed"),            // Payment failed
    ABANDONED("Abandoned"),       // User abandoned payment
    CANCELLED("Cancelled"),       // Payment cancelled
    REFUNDED("Refunded"),        // Fully refunded
    PARTIALLY_REFUNDED("Partially Refunded");
    
    private final String displayName;
    
    PaymentStatus(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
}
