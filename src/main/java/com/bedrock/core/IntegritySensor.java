package com.bedrock.core;

import java.io.FileInputStream;
import java.nio.file.*;
import java.security.MessageDigest;

/**
 * IntegritySensor
 * ------------------------------
 * Demonstrates tamper detection via SHA-256 checksum validation.
 */
public final class IntegritySensor {
    public static String computeJarHash() {
        try {
            Path jar = Paths.get(IntegritySensor.class
                    .getProtectionDomain()
                    .getCodeSource()
                    .getLocation()
                    .toURI());
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            try (FileInputStream fis = new FileInputStream(jar.toFile())) {
                byte[] buffer = new byte[8192];
                int n;
                while ((n = fis.read(buffer)) > 0)
                    digest.update(buffer, 0, n);
            }
            StringBuilder sb = new StringBuilder();
            for (byte b : digest.digest())
                sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception e) {
            return "UNKNOWN";
        }
    }

    public static void verifyJarIntegrity(String expectedHash) throws SecurityException {
        String actualHash = computeJarHash();
        if (!actualHash.equalsIgnoreCase(expectedHash)) {
            throw new SecurityException("Jar integrity check failed! Expected: " + expectedHash + ", Found: " + actualHash);
        }
    }
}