package com.todoapp.logintodoapp;

import java.util.Base64;
import java.security.SecureRandom;

public class SecretKeyGenerator {
    public static void main(String[] args) {
        byte[] key = new byte[32]; // 256-bit key
        new SecureRandom().nextBytes(key);
        String secretKey = Base64.getEncoder().encodeToString(key);
        System.out.println("Generated JWT Secret Key: " + secretKey);
    }
}

