package no.nav.foreldrepenger.selvbetjening.filters;

import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;

import java.util.Arrays;
import java.util.List;

final class FilterRegistrationUtil {

    private static final String ALWAYS = "/*";

    private FilterRegistrationUtil() {
    }

    static List<String> urlPatternsFor(String... patterns) {
        return Arrays.stream(patterns)
                .map(pattern -> pattern + ALWAYS)
                .collect(toList());
    }

    static List<String> always() {
        return singletonList(ALWAYS);
    }
}
