package no.nav.foreldrepenger.selvbetjening.util;

import static java.util.stream.Collectors.toList;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
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

    public static <T> List<T> distinct(List<T> list) {
        return safeStream(list).distinct().collect(toList());
    }

    public static <T> Predicate<T> not(Predicate<T> t) {
        return t.negate();
    }

    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }
}
