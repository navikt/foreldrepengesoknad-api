package no.nav.foreldrepenger.selvbetjening.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public final class StreamUtil {
    private StreamUtil() {
    }

    @SafeVarargs
    public static <T> Stream<T> safeStream(T... elems) {
        return safeStream(Arrays.asList(elems));
    }

    public static <T> Stream<T> safeStream(List<T> list) {
        return Optional.ofNullable(list)
                .orElseGet(Collections::emptyList)
                .stream();
    }

}
