package com.senibo.e_commerce_api.model.auth;

public enum ProviderType {
    GOOGLE("Google"),
    GITHUB("GitHub"),
    FACEBOOK("Facebook"),
    MICROSOFT("Microsoft"),
    LINKEDIN("LinkedIn");

    private final String displayName;

    ProviderType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
