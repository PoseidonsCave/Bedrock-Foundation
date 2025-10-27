package com.bedrock.core;

import java.util.*;

/**
 * RankRegistry
 * ------------------------------
 * Defines which modules are accessible under each license rank.
 */
public final class RankRegistry {
    private static final Map<String, Set<String>> RANKS = new HashMap<>();

    static {
        RANKS.put("NOOB", Set.of("PatternBuilder"));
        RANKS.put("GOD", Set.of("*"));
    }

    public static boolean isValidRank(String rank) {
        return RANKS.containsKey(rank.toUpperCase());
    }

    public static Set<String> getAllowedModules(String rank) {
        return RANKS.getOrDefault(rank.toUpperCase(), Set.of());
    }
}