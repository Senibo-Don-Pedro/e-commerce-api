package com.senibo.e_commerce_api.model.payment;

// Simplified enums for Paystack
public enum PaymentMethod {
    CARD("Card"),
    BANK_TRANSFER("Bank Transfer"),
    USSD("USSD"),
    QR("QR Code"),
    MOBILE_MONEY("Mobile Money"),
    BANK("Bank"); // Direct bank payment
    
    private final String displayName;
    
    PaymentMethod(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
}
