package com.example.payment_service.service;

import com.example.payment_service.config.VNPayConfig;
import com.example.payment_service.dto.VNPRequestDTO;
import com.example.payment_service.utils.PaymentUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PaymentService {
    public String getVNPayRedirectURL(VNPRequestDTO requestBody, HttpServletRequest request) throws NoSuchAlgorithmException, InvalidKeyException {
        Map<String, String> vnpParams = new HashMap<>();
        vnpParams.put(VNPayConfig.VERSION_PARAM, VNPayConfig.VERSION);
        vnpParams.put(VNPayConfig.COMMAND_PARAM, VNPayConfig.COMMAND);
        vnpParams.put(VNPayConfig.TMNCODE_PARAM, VNPayConfig.TMNCODE);
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

        LocalDateTime currentTime = LocalDateTime.now();
        vnpParams.put(VNPayConfig.CREATE_DATE_PARAM,
                currentTime.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
        vnpParams.put(VNPayConfig.EXPIRE_DATE_PARAM,
                currentTime.plusMinutes(15).format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
        vnpParams.put(VNPayConfig.TXNREF_PARAM, PaymentUtil.getRandomNumbers(8));
        vnpParams.put(VNPayConfig.SECURE_HASH_PARAM, PaymentUtil.getVNPSecureHash(vnpParams));

        String queryParams = buildQueryParams(vnpParams);
        return String.format("%s?%s", VNPayConfig.PAYMENT_URL, queryParams);
    }

    private String buildQueryParams(Map<String, String> params) {
        return params.keySet().stream().sorted()
                .map(k -> {
                    if (k.equals("vnp_SecureHash"))
                        return k + "=" + params.get(k);
                    return k + "=" + URLEncoder.encode(params.get(k), StandardCharsets.UTF_8);
                })
                .collect(Collectors.joining("&"));
    }
}
