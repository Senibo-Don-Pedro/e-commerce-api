package com.senibo.e_commerce_api.model.address;

public enum AddressType {
    SHIPPING("Shipping"),
    BILLING("Billing"),
    BOTH("Shipping & Billing");
    
    private final String displayName;
    
    AddressType(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
}
