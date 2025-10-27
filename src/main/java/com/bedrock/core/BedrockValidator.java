package com.bedrock.core;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.nio.file.*;
import java.security.*;
import java.util.Set;

/**
 * BedrockValidator
 * ------------------------------
 * Demonstrates cryptographic license validation flow (AES-GCM + RSA signature check).
 * This version uses redacted keys and stubbed license bundles for portfolio purposes.
 */
public final class BedrockValidator {
    private static final Gson GSON = new Gson();
    private static final String AES_ALGO = "AES/GCM/NoPadding";
    private static final int GCM_TAG_BITS = 128;
    private static final Path CONFIG_DIR = Paths.get("config", "bedrock");
    private static final Path LICENSE_FILE = CONFIG_DIR.resolve("license.json");

    // Redacted public key for demo
    private static final String EMBEDDED_PUBLIC_KEY_B64 = "<PUBLIC_KEY>";

    private static volatile boolean ready = false;
    private static volatile String activeRank = "NOOB";
    private static volatile Set<String> allowedModules;

    private BedrockValidator() {}

    public static void bootstrapOrDie() {
        try {
            ensureFolders();
            LicenseBundle bundle = readLicense(LICENSE_FILE);
            verifySignature(bundle);
            LicensePayload payload = decryptAndParse(bundle);

            activeRank = payload.getRank();
            allowedModules = RankRegistry.getAllowedModules(activeRank);
            ready = true;

            System.out.println("[Bedrock] ✅ License verified! Rank=" + activeRank);
        } catch (IOException e) {
            System.err.println("[Bedrock] ⚠️ No license found. Running in DEMO mode.");
            fallbackDemoMode();
        } catch (Throwable t) {
            System.err.println("[Bedrock] ❌ License validation failed: " + t.getMessage());
            fallbackDemoMode();
        }
    }

    public static boolean canUse(String moduleName) {
        if (!ready) return false;
        if (allowedModules.contains("*")) return true;
        return allowedModules.stream().anyMatch(m -> m.equalsIgnoreCase(moduleName));
    }

    public static String rank() { return activeRank; }

    // ===== Internals =====

    private static void ensureFolders() throws IOException {
        if (!Files.exists(CONFIG_DIR))
            Files.createDirectories(CONFIG_DIR);
        if (!Files.exists(LICENSE_FILE))
            throw new IOException("Missing license.json (demo mode).");
    }

    private static LicenseBundle readLicense(Path path) throws IOException, JsonSyntaxException {
        String json = Files.readString(path);
        LicenseBundle b = GSON.fromJson(json, LicenseBundle.class);
        if (b == null || b.license == null || b.signature == null || b.key == null)
            throw new IOException("Malformed license file.");
        return b;
    }

    private static void verifySignature(LicenseBundle b) throws GeneralSecurityException {
        // Signature verification demo (mocked, no real key usage)
        System.out.println("[Bedrock] 🔑 Verifying RSA signature...");
    }

    private static LicensePayload decryptAndParse(LicenseBundle b) throws Exception {
        // Demo AES-GCM decryption flow (non-functional)
        System.out.println("[Bedrock] 🔐 Simulating AES-GCM decryption...");
        return new LicensePayload("DEV", System.currentTimeMillis(), Long.MAX_VALUE);
    }

    private static void fallbackDemoMode() {
        activeRank = "INIT";
        allowedModules = RankRegistry.getAllowedModules(activeRank);
        ready = true;
    }

    private static class LicenseBundle {
        String license;
        String signature;
        String key;
    }
}