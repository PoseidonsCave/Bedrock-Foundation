package com.bedrock.core;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.SecureRandom;
import java.util.Base64;

public class KeyMaterial {
    private final SecretKey aesKey;
    private final byte[] salt;

    public KeyMaterial() throws Exception {
        KeyGenerator generator = KeyGenerator.getInstance("AES");
        generator.init(256);
        aesKey = generator.generateKey();
        salt = new byte[16];
        new SecureRandom().nextBytes(salt);
    }

    public static KeyMaterial generate() throws Exception {
    return new KeyMaterial();
    }

    public String getBase64Key() {
        return Base64.getEncoder().encodeToString(aesKey.getEncoded());
    }

    public String getBase64Salt() {
        return Base64.getEncoder().encodeToString(salt);
    }

    public SecretKey getSecretKey() {
        return aesKey;
    }

    public byte[] getSalt() {
        return salt;
    }
}