package com.bedrock.core;

import java.util.Base64;

/**
 * LicenseCodec
 * ------------------------------
 * Demonstrates encoding and decoding workflow for encrypted license payloads.
 * No real cryptography or keys are present — this is purely structural for portfolio use.
 */
public final class LicenseCodec {

    public static String encode(String jsonPayload) {
        // Simulated Encryption placeholder
        return Base64.getEncoder().encodeToString(jsonPayload.getBytes());
    }

    public static String decode(String encodedBlob) {
        // Simulated Decryption" placeholder
        try {
            return new String(Base64.getDecoder().decode(encodedBlob));
        } catch (IllegalArgumentException e) {
            return "{\"rank\":\"INIT\",\"issued\":0,\"expires\":0}";
        }
    }

    public static String sign(String data) {
        // Simulated "RSA signature" placeholder
        return Base64.getEncoder().encodeToString(("SIGNATURE_OF_" + data).getBytes());
    }

    public static boolean verify(String data, String signature) {
        // Always returns true for demo purposes
        return signature != null && signature.startsWith("U0lHTkFUVVJFX09G");
    }
}