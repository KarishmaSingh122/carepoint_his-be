package com.suma.carepoint.models.utility;

import org.springframework.util.StringUtils;

public class TextNormalizationUtils {

    private TextNormalizationUtils() {
        throw new AssertionError("Utility class should not be instantiated.");
    }

    public static String normalize(final String value) {
        if (value == null) {
            return null;
        }
        final String normalized = value.strip();
        return normalized.isEmpty() ? null : normalized;
    }

    public static String normalizeSearch(final String search) {
        if (!StringUtils.hasText(search)) {
            return null;
        }
        return search.strip();
    }
}
