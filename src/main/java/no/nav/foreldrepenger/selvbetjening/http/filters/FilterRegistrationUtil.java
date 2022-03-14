package no.nav.foreldrepenger.selvbetjening.http.filters;

import java.util.Arrays;
import java.util.List;

final class FilterRegistrationUtil {

    private static final String WILDCARD = "/*";

    private FilterRegistrationUtil() {
    }

    static List<String> urlPatternsFor(String... patterns) {
        return Arrays.stream(patterns)
            .map(pattern -> pattern + WILDCARD)
            .toList();
    }

    static List<String> always() {
        return List.of(WILDCARD);
    }
}
