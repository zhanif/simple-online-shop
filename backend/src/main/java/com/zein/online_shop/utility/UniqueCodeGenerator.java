package com.zein.online_shop.utility;

import java.security.SecureRandom;

public class UniqueCodeGenerator {
    public static final SecureRandom random = new SecureRandom();
    public static final String CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    public static String generate(int length) {
        if (length <= 0) throw new IllegalArgumentException("Length must be positive");

        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int rand = random.nextInt(CHARS.length());
            sb.append(CHARS.charAt(rand));
        }

        return sb.toString();
    }
}
