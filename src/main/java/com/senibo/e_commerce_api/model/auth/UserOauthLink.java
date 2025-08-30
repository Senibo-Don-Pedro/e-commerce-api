package com.senibo.e_commerce_api.model.auth;

import com.senibo.e_commerce_api.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_oauth_links", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "provider_id"})})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserOauthLink extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "provider_id", nullable = false)
    private OauthProvider provider;

    @Column(name = "provider_user_id", nullable = false)
    private String providerUserId; // The user's ID from the OAuth provider

    @Column(name = "provider_email")
    private String providerEmail; // Email from OAuth provider

    @Column(name = "provider_display_name")
    private String providerDisplayName; // Display name from provider

    @Column(name = "access_token", columnDefinition = "TEXT")
    private String accessToken; // Should be encrypted in production

    @Column(name = "refresh_token", columnDefinition = "TEXT")
    private String refreshToken; // Should be encrypted in production

    @Column(name = "token_expires_at")
    private LocalDateTime tokenExpiresAt;

    @Column(name = "last_login")
    private LocalDateTime lastLogin;
}
