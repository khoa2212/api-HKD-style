package com.example.payment_service.controller;

import com.example.payment_service.dto.PaymentRedirectResponseDTO;
import com.example.payment_service.dto.VNPRequestDTO;
import com.example.payment_service.service.PaymentService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {
    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping(
            path = "/vnpay",
            produces = {MediaType.APPLICATION_JSON_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<PaymentRedirectResponseDTO> getVNPayRedirectURL(
            @RequestBody VNPRequestDTO requestBody, HttpServletRequest request
            ) throws NoSuchAlgorithmException, InvalidKeyException {
        String redirectURL = paymentService.getVNPayRedirectURL(requestBody, request);
        return ResponseEntity.ok(new PaymentRedirectResponseDTO(redirectURL));
//        return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY)
//                .header(HttpHeaders.LOCATION, redirectURL)
//                .build();
    }

}
