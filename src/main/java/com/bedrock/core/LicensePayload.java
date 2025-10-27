package com.bedrock.core;

/**
 * LicensePayload
 * ------------------------------
 * Represents decrypted license data (simplified for demo use).
 */
public class LicensePayload {
    private String rank;
    private long issued;
    private long expires;

    public LicensePayload(String rank, long issued, long expires) {
        this.rank = rank;
        this.issued = issued;
        this.expires = expires;
    }

    public String getRank() { return rank; }
    public long getIssued() { return issued; }
    public long getExpires() { return expires; }

    @Override
    public String toString() {
        return "LicensePayload{" +
                "rank='" + rank + '\'' +
                ", issued=" + issued +
                ", expires=" + expires +
                '}';
    }
}