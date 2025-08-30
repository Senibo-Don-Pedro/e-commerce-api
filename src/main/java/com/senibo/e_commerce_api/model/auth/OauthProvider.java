package com.senibo.e_commerce_api.model.auth;

import com.senibo.e_commerce_api.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "oauth_providers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OauthProvider extends BaseEntity {

    @Column(unique = true, nullable = false)
    @Enumerated(EnumType.STRING)
    private ProviderType providerType;

    @Column(nullable = false)
    private String providerName;

    @Column(nullable = false)
    private String clientId;

    @Column(nullable = false)
    private String clientSecret; // This should be encrypted in production

    @Column(nullable = false)
    private String authorizationUrl;

    @Column(nullable = false)
    private String tokenUrl;

    @Column(nullable = false)
    private String userInfoUrl;

    @Column(nullable = false)
    @Builder.Default
    private Boolean isActive = true;

    // Relationships
    @OneToMany(mappedBy = "provider")
    private List<UserOauthLink> userLinks = new ArrayList<>();
}
