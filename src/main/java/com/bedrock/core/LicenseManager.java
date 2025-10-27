package com.bedrock.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.security.*;
import java.security.spec.*;
import java.util.Base64;

/**
 * LicenseManager
 * --------------------------
 * Manages RSA signing & verification for licenses.
 * Loads an existing keypair (private/public) from PEM files,
 * or generates them automatically if missing.
 */
public class LicenseManager {

    private static final String PRIVATE_KEY_PATH = "private.pem";
    private static final String PUBLIC_KEY_PATH  = "public.pem";

    private final PrivateKey privateKey;
    private final PublicKey publicKey;

    public LicenseManager() throws Exception {
        if (Files.exists(Path.of(PRIVATE_KEY_PATH)) && Files.exists(Path.of(PUBLIC_KEY_PATH))) {
            this.privateKey = loadPrivateKey();
            this.publicKey = loadPublicKey();
        } else {
            // First-time setup
            System.out.println("🔧 No existing keypair found — generating new RSA 2048 keypair...");
            KeyPairGenerator gen = KeyPairGenerator.getInstance("RSA");
            gen.initialize(2048);
            KeyPair kp = gen.generateKeyPair();
            this.privateKey = kp.getPrivate();
            this.publicKey = kp.getPublic();
            saveKeyPair(kp);
            System.out.println("✅ New keypair saved to private.pem / public.pem");
        }
    }

    /** Signs the encrypted payload using RSA-SHA256. */
    public String signPayload(String json) throws Exception {
        Signature signer = Signature.getInstance("SHA256withRSA");
        signer.initSign(privateKey);
        signer.update(json.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(signer.sign());
    }

    /** Verifies a license signature using the stored public key. */
    public boolean verifySignature(String json, String signature) throws Exception {
        Signature verifier = Signature.getInstance("SHA256withRSA");
        verifier.initVerify(publicKey);
        verifier.update(json.getBytes(StandardCharsets.UTF_8));
        byte[] sigBytes = Base64.getDecoder().decode(signature);
        return verifier.verify(sigBytes);
    }

    /** Save new keypair as PEM files. */
    private static void saveKeyPair(KeyPair pair) throws Exception {
        Base64.Encoder enc = Base64.getMimeEncoder(64, new byte[]{'\n'});

        String privatePEM = "-----BEGIN PRIVATE KEY-----\n" +
                enc.encodeToString(pair.getPrivate().getEncoded()) +
                "\n-----END PRIVATE KEY-----\n";
        Files.writeString(Path.of(PRIVATE_KEY_PATH), privatePEM);

        String publicPEM = "-----BEGIN PUBLIC KEY-----\n" +
                enc.encodeToString(pair.getPublic().getEncoded()) +
                "\n-----END PUBLIC KEY-----\n";
        Files.writeString(Path.of(PUBLIC_KEY_PATH), publicPEM);
    }

    /** Loads private.pem */
    private static PrivateKey loadPrivateKey() throws Exception {
        String pem = Files.readString(Path.of(PRIVATE_KEY_PATH))
                .replaceAll("-----\\w+ PRIVATE KEY-----", "")
                .replaceAll("\\s+", "");
        byte[] keyBytes = Base64.getDecoder().decode(pem);
        return KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(keyBytes));
    }

    /** Loads public.pem */
    private static PublicKey loadPublicKey() throws Exception {
        String pem = Files.readString(Path.of(PUBLIC_KEY_PATH))
                .replaceAll("-----\\w+ PUBLIC KEY-----", "")
                .replaceAll("\\s+", "");
        byte[] keyBytes = Base64.getDecoder().decode(pem);
        return KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(keyBytes));
    }

    public PublicKey getPublicKey() { return publicKey; }
    public PrivateKey getPrivateKey() { return privateKey; }
}