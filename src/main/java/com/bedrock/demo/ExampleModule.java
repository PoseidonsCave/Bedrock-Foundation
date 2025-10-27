package com.bedrock.demo;

import com.bedrock.core.BedrockValidator;
import com.bedrock.core.IntegritySensor;

/**
 * PatternBuilderModule
 * ------------------------------
 * Demo module showcasing runtime checks and feature access gating.
 */
public class ExampleModule {
    public void runDemo() {
        System.out.println("[Bedrock] Starting ExampleModule demo...");
        String jarHash = IntegritySensor.computeJarHash();
        System.out.println("[Bedrock] JAR Integrity: " + jarHash);
        System.out.println("[Bedrock] Module active under rank: " + BedrockValidator.rank());
    }

    public static void main(String[] args) {
        BedrockValidator.bootstrapOrDie();
        new ExampleModule().runDemo();
    }
}