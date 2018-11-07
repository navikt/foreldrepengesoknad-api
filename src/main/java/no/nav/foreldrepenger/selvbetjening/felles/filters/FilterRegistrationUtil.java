package no.nav.foreldrepenger.selvbetjening.felles.filters;

import static java.util.stream.Collectors.toList;

import java.util.Arrays;
import java.util.List;

final class FilterRegistrationUtil {

    private FilterRegistrationUtil() {
    }

    static List<String> urlPatternsFor(String... patterns) {
        return Arrays.stream(patterns)
                .map(pattern -> pattern + "/*")
                .collect(toList());
    }
}
