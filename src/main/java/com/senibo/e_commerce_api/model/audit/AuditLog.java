package com.senibo.e_commerce_api.model.audit;

import com.senibo.e_commerce_api.model.BaseEntity;
import com.senibo.e_commerce_api.model.auth.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "audit_logs", indexes = {@Index(name = "idx_audit_user", columnList = "user_id"), @Index(name = "idx_audit_action", columnList = "action"), @Index(name = "idx_audit_timestamp", columnList = "timestamp"), @Index(name = "idx_audit_entity", columnList = "entity_type, entity_id"), @Index(name = "idx_audit_ip", columnList = "ip_address")})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditLog extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user; // Can be null for system actions

    @Column(name = "action", nullable = false)
    @Enumerated(EnumType.STRING)
    private AuditAction action;

    @Column(name = "entity_type")
    private String entityType; // "User", "Order", "Product", etc.

    @Column(name = "entity_id")
    private String entityId; // UUID of the affected entity

    @Column(name = "timestamp", nullable = false)
    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();

    @Column(name = "ip_address")
    private String ipAddress;

    @Column(name = "user_agent", columnDefinition = "TEXT")
    private String userAgent;

    @Column(name = "session_id")
    private String sessionId;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description; // Human-readable description

    // For data changes - store old and new values as JSON
    @Column(name = "old_values", columnDefinition = "TEXT")
    private String oldValues; // JSON of old values

    @Column(name = "new_values", columnDefinition = "TEXT")
    private String newValues; // JSON of new values

    @Column(name = "request_id")
    private String requestId; // For correlating related audit entries

    @Column(name = "success", nullable = false)
    @Builder.Default
    private Boolean success = true;

    @Column(name = "error_message")
    private String errorMessage; // If action failed

    // Helper methods
    public boolean isSecurityEvent() {
        return action == AuditAction.LOGIN_SUCCESS || action == AuditAction.LOGIN_FAILED || action == AuditAction.LOGOUT || action == AuditAction.PASSWORD_CHANGED || action == AuditAction.ACCOUNT_LOCKED;
    }

    public boolean isDataModification() {
        return action == AuditAction.CREATE || action == AuditAction.UPDATE || action == AuditAction.DELETE;
    }
}
