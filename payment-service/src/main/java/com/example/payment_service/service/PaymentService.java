package com.example.payment_service.service;

import com.example.payment_service.config.VNPayConfig;
import com.example.payment_service.dto.PaymentDetailsResponseDTO;
import com.example.payment_service.dto.VNPRequestDTO;
import com.example.payment_service.entity.PaymentDetails;
import com.example.payment_service.repository.PaymentDetailsRepository;
import com.example.payment_service.utils.PaymentUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PaymentService {
    private final PaymentDetailsRepository paymentDetailsRepository;
    private final VNPayConfig vnPayConfig;
    private final ZoneId systemTimeZone;

    public PaymentService(PaymentDetailsRepository paymentDetailsRepository, VNPayConfig vnPayConfig) {
        this.paymentDetailsRepository = paymentDetailsRepository;
        this.vnPayConfig = vnPayConfig;
        this.systemTimeZone = ZoneId.of("Asia/Ho_Chi_Minh");
    }

    public String getVNPayRedirectURL(VNPRequestDTO requestBody, HttpServletRequest request) throws NoSuchAlgorithmException, InvalidKeyException {
        Map<String, String> vnpParams = new HashMap<>();
        vnpParams.put(VNPayConfig.VERSION_PARAM, VNPayConfig.VERSION);
        vnpParams.put(VNPayConfig.COMMAND_PARAM, VNPayConfig.COMMAND);
        vnpParams.put(VNPayConfig.TMNCODE_PARAM, vnPayConfig.getTMNCODE());
        vnpParams.put(VNPayConfig.RETURN_URL_PARAM, VNPayConfig.RETURN_URL);

        vnpParams.put(VNPayConfig.AMOUNT_PARAM, String.valueOf(requestBody.getAmount().intValue() * 100));
        vnpParams.put(VNPayConfig.CURRENCY_PARAM, "VND");
        vnpParams.put(VNPayConfig.IP_ADDRESS_PARAM, request.getRemoteAddr());
        if (requestBody.getLocale() != null && !requestBody.getLocale().isBlank())
            vnpParams.put(VNPayConfig.LOCALE_PARAM, requestBody.getLocale());
        else
            vnpParams.put(VNPayConfig.LOCALE_PARAM, "vn");
        vnpParams.put(VNPayConfig.ORDER_INFO_PARAM, requestBody.getOrderInfo());
        vnpParams.put(VNPayConfig.ORDER_TYPE_PARAM, requestBody.getOrderType());

        ZonedDateTime currentTime = ZonedDateTime.ofInstant(Instant.now(), this.systemTimeZone);
        vnpParams.put(VNPayConfig.CREATE_DATE_PARAM,
                currentTime.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
        vnpParams.put(VNPayConfig.EXPIRE_DATE_PARAM,
                currentTime.plusMinutes(15).format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
        vnpParams.put(VNPayConfig.TXNREF_PARAM, PaymentUtil.getRandomNumbers(8));

        StringBuilder queryParams = new StringBuilder(buildQueryParams(vnpParams));
        String secureHash = PaymentUtil.getVNPSecureHash(vnpParams, vnPayConfig.getHASH_SECRET());
        queryParams.append("&").append(VNPayConfig.SECURE_HASH_PARAM).append("=").append(secureHash);
        return String.format("%s?%s", VNPayConfig.PAYMENT_URL, queryParams);
    }

    public PaymentDetailsResponseDTO savePaymentDetails(Map<String, String> paymentParams) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        LocalDateTime ldt = LocalDateTime.parse(paymentParams.get(VNPayConfig.PAYMENT_DATE_PARAM), dtf);
        ZonedDateTime payDate = ZonedDateTime.of(ldt, this.systemTimeZone);
        PaymentDetails paymentDetails = PaymentDetails.builder()
                .amount(Long.parseLong(paymentParams.get(VNPayConfig.AMOUNT_PARAM)))
                .bankCode(paymentParams.get(VNPayConfig.BANK_CODE_PARAM))
                .paymentMethod(paymentParams.get(VNPayConfig.PAYMENT_METHOD_PARAM))
                .paymentInfo(paymentParams.get(VNPayConfig.PAYMENT_INFO_PARAM))
                .paymentDate(payDate.toInstant())
                .build();

        paymentDetails = paymentDetailsRepository.save(paymentDetails);
        return PaymentDetailsResponseDTO.builder()
                .id(paymentDetails.getId().toString())
                .amount(paymentDetails.getAmount())
                .bankCode(paymentDetails.getBankCode())
                .paymentMethod(paymentDetails.getPaymentMethod())
                .paymentInfo(paymentDetails.getPaymentInfo())
                .payDate(paymentDetails.getPaymentDate())
                .build();
    }

    private String buildQueryParams(Map<String, String> params) {
        return params.keySet().stream().sorted()
                .map(k ->
                        URLEncoder.encode(k, StandardCharsets.US_ASCII) + "=" + URLEncoder.encode(params.get(k), StandardCharsets.UTF_8))
                .collect(Collectors.joining("&"));
    }
}
