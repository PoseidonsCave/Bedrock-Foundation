# Bedrock Foundation

**A secure, modular runtime framework for Java applications that require signed-license verification, artifact integrity checks, and tier-based module access.**

Bedrock Foundation demonstrates cryptographic enforcement of software authenticity using **AES-GCM encryption** and **RSA-signed license validation**.  
Originally designed for modular tooling environments, it now serves as a public example of how to build secure, extensible client frameworks.

---

## Key Features
- ✅ **Integrity Verification** — Prevents tampering with packaged artifacts via SHA-256 validation.
- 🔑 **RSA Signature Validation** — Verifies authenticity of encrypted licenses using embedded public keys.
- 🧩 **Rank-Based Module Gating** — Modules are loaded according to verified license rank.
- ⚙️ **Cross-Platform Build Automation** — Clean Gradle + Bash workflow for building and packaging releases.

---

## Architecture Overview
The system operates in three stages:
1. **Integrity Sensor** — Computes SHA-256 hash at runtime to ensure JAR authenticity.  
2. **License Validator** — Decrypts AES-GCM payloads and verifies RSA signatures using embedded keys.  
3. **Rank Registry** — Grants or denies module access based on validated license rank.

Each component is isolated and can be reused in any Java project.

---

## Example
```java
public static void main(String[] args) {
    BedrockValidator.bootstrapOrDie();
    if (BedrockGate.canUse("PatternBuilder")) {
        new PatternBuilderModule().runDemo();
    }
}