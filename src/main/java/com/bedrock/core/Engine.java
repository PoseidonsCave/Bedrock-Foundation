package com.bedrock.core;

import java.nio.file.*;
import java.io.IOException;

/**
 * Engine
 * ------------------------------
 * Simulates a secure license generation pipeline.
 * In the private version, this would handle cryptographic key management and payload signing.
 * This sanitized version only demonstrates structure and build flow.
 */
public final class Engine {

    private static final Path LICENSE_DIR = Paths.get("licenses");

    public static void main(String[] args) {
        try {
            if (!Files.exists(LICENSE_DIR)) {
                Files.createDirectories(LICENSE_DIR);
            }

            String rank = (args.length > 0) ? args[0].toUpperCase() : "DEV";
            String json = LicenseGenerator.generateMockLicense(rank);

            Path output = LICENSE_DIR.resolve(rank.toLowerCase() + "_license.json");
            Files.writeString(output, json);
            System.out.println("✅ Mock license generated for rank " + rank + " at " + output);
        } catch (IOException e) {
            System.err.println("❌ Failed to generate mock license: " + e.getMessage());
        }
    }
}