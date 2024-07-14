package com.example.payment_service.utils;

import com.example.payment_service.config.VNPayConfig;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PaymentUtil {
    private static final String HMAC_SHA512 = "HmacSHA512";
    public static String getRandomNumbers(int size) {
        Random random = new SecureRandom();
        StringBuilder ans = new StringBuilder();
        random.ints(size, 0, 10).mapToObj(String::valueOf).forEach(ans::append);
        return ans.toString();
    }
    public static String hashStringWithHMAC(String content, String secretKey) throws NoSuchAlgorithmException, InvalidKeyException {
        SecretKeySpec spec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), HMAC_SHA512);
        Mac mac = Mac.getInstance(HMAC_SHA512);
        mac.init(spec);
        byte[] hashedData = mac.doFinal(content.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(hashedData);
    }

    public static String getVNPSecureHash(Map<String, String> params) throws NoSuchAlgorithmException, InvalidKeyException {
        String contentToHash = params.keySet().stream().sorted()
                .map(k -> k + "=" + URLEncoder.encode(params.get(k), StandardCharsets.UTF_8))
                .collect(Collectors.joining("&"));
        return hashStringWithHMAC(contentToHash, VNPayConfig.HASH_SECRET);
    }
}
