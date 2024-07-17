package com.example.payment_service.controller;

import com.example.payment_service.config.VNPayConfig;
import com.example.payment_service.dto.PaymentDetailsResponseDTO;
import com.example.payment_service.dto.PaymentRedirectResponseDTO;
import com.example.payment_service.dto.VNPRequestDTO;
import com.example.payment_service.exception.ExceptionMessage;
import com.example.payment_service.exception.FailedTransactionException;
import com.example.payment_service.service.PaymentService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.stream.Collectors;

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

    @GetMapping(
            path = "/vnpay/result",
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<PaymentDetailsResponseDTO> getTransactionResult(@RequestParam Map<String, String> params) throws FailedTransactionException {
        if (!params.get(VNPayConfig.RESPONSE_CODE_PARAM).equals("00"))
            throw new FailedTransactionException(ExceptionMessage.FAILED_TRANSACTION);
        return ResponseEntity.ok(paymentService.savePaymentDetails(params));
    }

}
