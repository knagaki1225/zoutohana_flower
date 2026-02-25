package com.example.zoutohanafansite.service;

import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
public class GenerateSecurityKeyService {
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final SecureRandom random = new SecureRandom();
    private static final int length = 4;

    /**
     * パスワードリセット用セキュリティーキー生成
     *
     * @return String セキュリティーキー(未ハッシュ化)
     */
    public static String generateSecurityKey() {
        StringBuilder sb1 = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb1.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }

        StringBuilder sb2 = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb2.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }

        String securityKey = sb1.toString() + "-" + sb2.toString();
        return securityKey;
    }
}
