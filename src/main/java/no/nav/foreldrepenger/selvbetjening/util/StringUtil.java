package no.nav.foreldrepenger.selvbetjening.util;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.util.CollectionUtils;

import com.google.common.base.Strings;

public final class StringUtil {
    private static final String DEFAULT_FLERTALL = "er";
    private static final int DEFAULT_LENGTH = 50;

    private StringUtil() {
    }

    public static String flertall(List<?> liste) {
        if (CollectionUtils.isEmpty(liste)) {
            return DEFAULT_FLERTALL;
        }
        return liste.size() == 1 ? "" : DEFAULT_FLERTALL;
    }

    public static String limit(String tekst) {
        return limit(tekst, DEFAULT_LENGTH);
    }

    public static String limit(String tekst, int max) {
        return Optional.ofNullable(tekst)
                .filter(t -> t.length() >= max)
                .map(s -> s.substring(0, max - 1) + "...")
                .orElse(tekst);
    }

    public static String limit(byte[] bytes) {
        return limit(bytes, DEFAULT_LENGTH);
    }

    public static String limit(byte[] bytes, int max) {
        return limit(Arrays.toString(bytes), max);
    }

    public static String mask(String value) {
        return (value != null) && (value.length() == 11) ? Strings.padEnd(value.substring(0, 6), 11, '*') : value;
    }

    public static String flertall(int n) {
        return flertall(n, DEFAULT_FLERTALL);
    }

    public static String flertall(int n, String flertall) {
        if (n != 1) {
            return flertall;
        }
        return "";
    }
}
