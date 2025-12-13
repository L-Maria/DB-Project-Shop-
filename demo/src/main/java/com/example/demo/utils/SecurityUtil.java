package com.example.demo.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;
import java.math.BigInteger;

public class SecurityUtil {

    public static String hashPassword(String originalPassword) {
        try {
            // Folosim algoritmul SHA-256
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            
            // Transformam parola in bytes si aplicam hash-ul
            byte[] encodedhash = digest.digest(
                originalPassword.getBytes(StandardCharsets.UTF_8));
            
            // Convertim rezultatul in format Hexadecimal (String citibil)
            BigInteger number = new BigInteger(1, encodedhash);
            StringBuilder hexString = new StringBuilder(number.toString(16));
            
            // Adaugam zerouri in fata daca e cazul (padding)
            while (hexString.length() < 32) {
                hexString.insert(0, '0');
            }
            
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Eroare la criptarea parolei", e);
        }
    }
}