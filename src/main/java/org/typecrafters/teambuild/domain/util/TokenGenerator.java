package org.typecrafters.teambuild.domain.util;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.HexFormat;

public final class TokenGenerator {

    private static final SecureRandom RANDOM = new SecureRandom();

    private TokenGenerator() {
    }

    public static String randomBase64Url(int byteLength) {
        byte[] bytes = new byte[byteLength];
        RANDOM.nextBytes(bytes);

        return Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(bytes);
    }

    public static String randomHex(int byteLength) {
        byte[] bytes = new byte[byteLength];
        RANDOM.nextBytes(bytes);
        return HexFormat.of().formatHex(bytes);
    }

    public static String randomNumeric(int digits) {
        if (digits <= 0) {
            throw new IllegalArgumentException("Argument 'digits' must be a positive integer.");
        }
        int min = Math.powExact(10, Math.max(digits - 1, 0));
        int max = Math.powExact(10, digits);
        int randomInt =  RANDOM.nextInt(min, max);
        return String.format("%06d", randomInt);
    }
}