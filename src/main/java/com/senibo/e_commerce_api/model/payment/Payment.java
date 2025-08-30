package com.senibo.e_commerce_api.model.payment;

import com.senibo.e_commerce_api.model.BaseEntity;
import com.senibo.e_commerce_api.model.order.Order;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments", indexes = {
    @Index(name = "idx_payment_order", columnList = "order_id"),
    @Index(name = "idx_payment_status", columnList = "status"),
    @Index(name = "idx_payment_reference", columnList = "paystack_reference"),
    @Index(name = "idx_payment_date", columnList = "payment_date")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment extends BaseEntity {
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false, unique = true)
    private Order order;
    
    @Column(name = "payment_method", nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;
    
    // Paystack specific fields
    @Column(name = "paystack_reference", unique = true, nullable = false)
    private String paystackReference; // Paystack transaction reference
    
    @Column(name = "paystack_access_code")
    private String paystackAccessCode; // For checkout redirects
    
    @Column(name = "paystack_transaction_id")
    private Long paystackTransactionId; // Paystack's internal transaction ID
    
    @Column(name = "amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;
    
    // Paystack works with kobo (smallest currency unit)
    @Column(name = "amount_in_kobo", nullable = false)
    private Long amountInKobo; // Amount * 100 for Paystack API
    
    @Column(name = "currency", nullable = false, length = 3)
    @Builder.Default
    private String currency = "NGN"; // Default to Nigerian Naira
    
    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private PaymentStatus status = PaymentStatus.PENDING;
    
    @Column(name = "payment_date")
    private LocalDateTime paymentDate;
    
    @Column(name = "failure_reason")
    private String failureReason;
    
    // Card information (Paystack provides this safely)
    @Column(name = "card_last_four", length = 4)
    private String cardLastFour;
    
    @Column(name = "card_type")
    private String cardType; // visa, mastercard, verve
    
    @Column(name = "card_bank")
    private String cardBank; // Issuing bank
    
    @Column(name = "authorization_code")
    private String authorizationCode; // For recurring payments
    
    // Customer details from Paystack
    @Column(name = "customer_email")
    private String customerEmail;
    
    @Column(name = "customer_phone")
    private String customerPhone;
    
    // Channel used (card, bank, ussd, qr, mobile_money, bank_transfer)
    @Column(name = "payment_channel")
    private String paymentChannel;
    
    // Refund information
    @Column(name = "refunded_amount", precision = 10, scale = 2)
    @Builder.Default
    private BigDecimal refundedAmount = BigDecimal.ZERO;
    
    @Column(name = "refund_date")
    private LocalDateTime refundDate;
    
    @Column(name = "refund_reason")
    private String refundReason;
    
    // Store Paystack webhook/response data
    @Column(name = "paystack_response", columnDefinition = "TEXT")
    private String paystackResponse; // Full webhook payload for debugging
    
    // Helper methods
    public boolean isSuccessful() {
        return status == PaymentStatus.COMPLETED;
    }
    
    public boolean canBeRefunded() {
        return status == PaymentStatus.COMPLETED && 
               refundedAmount.compareTo(amount) < 0;
    }
    
    public BigDecimal getRemainingRefundableAmount() {
        return amount.subtract(refundedAmount);
    }
    
    // Paystack specific helper
    public void setAmountAndCalculateKobo(BigDecimal amount) {
        this.amount = amount;
        this.amountInKobo = amount.multiply(BigDecimal.valueOf(100)).longValue();
    }
}
