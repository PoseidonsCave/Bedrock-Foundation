package com.bedrock.core;

import com.google.gson.Gson;
import java.util.HashMap;
import java.util.Map;

/**
 * LicenseGeneratorStub
 * ------------------------------
 * Non-functional demonstration of how license payloads are built and signed.
 * Uses LicenseCodec to simulate AES-GCM encryption and RSA signatures.
 */
public final class LicenseGenerator {

    private static final Gson GSON = new Gson();

    public static void main(String[] args) {
        String rank = (args.length > 0) ? args[0].toUpperCase() : "DEV";
        String license = generateMockLicense(rank);
        System.out.println(license);
    }

    public static String generateMockLicense(String rank) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("rank", rank);
        payload.put("issued", System.currentTimeMillis());
        payload.put("expires", System.currentTimeMillis() + 31536000000L); // +1 year

        String jsonPayload = GSON.toJson(payload);
        String encodedLicense = LicenseCodec.encode(jsonPayload);
        String signature = LicenseCodec.sign(encodedLicense);
        String aesKey = "<REDACTED_AES_KEY>";

        Map<String, Object> bundle = new HashMap<>();
        bundle.put("license", encodedLicense);
        bundle.put("signature", signature);
        bundle.put("key", aesKey);
        return GSON.toJson(bundle);
    }
}