package com.example.user_service.utils;

import org.apache.logging.log4j.util.Strings;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class CommonUtils {
    public static String generateRandomString(int n, int start, int end) {
        Random random = new SecureRandom();
        StringBuilder stringBuilder = new StringBuilder();
        random.ints(n, start, end + 1).mapToObj(i -> (char) i).forEach(
                stringBuilder::append);
        return stringBuilder.toString();
    }

    public static String generateRandomToken() {
        String randomLowerChars = generateRandomString(16, 97, 122);
        String randomUpperChars = generateRandomString(16, 65, 90);
        List<Character> chars = new ArrayList<>(Strings.concat(randomLowerChars, randomUpperChars).chars().mapToObj(i -> (char) i).toList());
        Collections.shuffle(chars);
        StringBuilder stringBuilder = new StringBuilder();
        chars.forEach(stringBuilder::append);
        return stringBuilder.toString();
    }

    public static String hashStringWithSHA256(String str) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(str.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(hash);
    }
}
