package com.senibo.e_commerce_api.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseEntity {

    //    @Id
    //    @GeneratedValue(strategy = GenerationType.UUID)
    //    private UUID id;
    //
    //    @CreationTimestamp
    //    private LocalDateTime createdAt;
    //
    //    @UpdateTimestamp
    //    private LocalDateTime updatedAt;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @CreatedDate // 3. Replaced @CreationTimestamp
    @Column(nullable = false, updatable = false)
    // Good practice to make it non-nullable and non-updatable
    private LocalDateTime createdAt;

    @LastModifiedDate // 4. Replaced @UpdateTimestamp
    @Column(nullable = false) // Good practice to make it non-nullable
    private LocalDateTime updatedAt;
}
