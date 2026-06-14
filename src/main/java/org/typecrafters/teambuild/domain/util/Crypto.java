package org.typecrafters.teambuild.domain.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HexFormat;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.typecrafters.teambuild.domain.exception.AppException;

public final class Crypto {
    public static final class Hash {
        public static String sha256(String value) {
            try {
                MessageDigest digest = MessageDigest.getInstance("SHA-256");

                byte[] hash = digest.digest(
                        value.getBytes(StandardCharsets.UTF_8));

                return HexFormat.of().formatHex(hash);
            } catch (Exception e) {
                throw AppException.internalServerError(e.getMessage());
            }
        }
    }

    public static final class Hmac {
        public static String sha256(String value, String secret) {
            try {
                Mac mac = Mac.getInstance("HmacSHA256");

                SecretKeySpec key = new SecretKeySpec(
                        secret.getBytes(StandardCharsets.UTF_8),
                        "HmacSHA256");

                mac.init(key);

                byte[] signature = mac.doFinal(
                        value.getBytes(StandardCharsets.UTF_8));

                return HexFormat.of().formatHex(signature);
            } catch (Exception e) {
                throw AppException.internalServerError(e.getMessage());
            }
        }
    }
}