package com.example.payment_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PaymentDetailsResponseDTO {
    private String id;
    private Long amount;
    private String bankCode;
    private String paymentInfo;
    private String paymentMethod;
    private Instant payDate;
    private Instant createdAt;
    private Instant updatedAt;
}
