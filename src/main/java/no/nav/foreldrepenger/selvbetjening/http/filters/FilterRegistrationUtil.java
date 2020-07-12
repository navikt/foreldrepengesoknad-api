package no.nav.foreldrepenger.selvbetjening.http.filters;

import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;

import java.util.Arrays;
import java.util.List;

final class FilterRegistrationUtil {

    static final String WILDCARD = "/*";

    private FilterRegistrationUtil() {
    }

    static List<String> urlPatternsFor(String... patterns) {
        return Arrays.stream(patterns)
                .map(pattern -> pattern + WILDCARD)
                .collect(toList());
    }

    static List<String> always() {
        return singletonList(WILDCARD);
    }
}
