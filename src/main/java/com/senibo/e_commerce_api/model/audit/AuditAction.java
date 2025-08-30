package com.senibo.e_commerce_api.model.audit;

public enum AuditAction {
    // Authentication
    LOGIN_SUCCESS("User logged in successfully"),
    LOGIN_FAILED("User login failed"),
    LOGOUT("User logged out"),
    PASSWORD_CHANGED("Password changed"),
    OAUTH_LOGIN("OAuth login"),
    
    // Account Management
    ACCOUNT_CREATED("Account created"),
    ACCOUNT_UPDATED("Account updated"),
    ACCOUNT_DELETED("Account deleted"),
    ACCOUNT_LOCKED("Account locked"),
    ACCOUNT_UNLOCKED("Account unlocked"),
    
    // Product Management
    PRODUCT_CREATED("Product created"),
    PRODUCT_UPDATED("Product updated"),
    PRODUCT_DELETED("Product deleted"),
    PRODUCT_VIEWED("Product viewed"),
    
    // Order Management
    ORDER_CREATED("Order created"),
    ORDER_UPDATED("Order updated"),
    ORDER_CANCELLED("Order cancelled"),
    ORDER_SHIPPED("Order shipped"),
    ORDER_DELIVERED("Order delivered"),
    
    // Cart Management
    CART_ITEM_ADDED("Item added to cart"),
    CART_ITEM_REMOVED("Item removed from cart"),
    CART_ITEM_UPDATED("Cart item updated"),
    CART_CLEARED("Cart cleared"),
    
    // Payment
    PAYMENT_INITIATED("Payment initiated"),
    PAYMENT_COMPLETED("Payment completed"),
    PAYMENT_FAILED("Payment failed"),
    PAYMENT_REFUNDED("Payment refunded"),
    
    // Admin Actions
    ADMIN_LOGIN("Admin logged in"),
    ADMIN_ACTION("Admin action performed"),
    SYSTEM_ACTION("System action"),
    
    // Generic CRUD
    CREATE("Entity created"),
    READ("Entity read"),
    UPDATE("Entity updated"),
    DELETE("Entity deleted"),
    
    // Security Events
    SUSPICIOUS_ACTIVITY("Suspicious activity detected"),
    RATE_LIMIT_EXCEEDED("Rate limit exceeded"),
    UNAUTHORIZED_ACCESS("Unauthorized access attempt");
    
    private final String description;
    
    AuditAction(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
}
