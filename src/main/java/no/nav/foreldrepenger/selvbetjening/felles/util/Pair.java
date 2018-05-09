package no.nav.foreldrepenger.selvbetjening.felles.util;

import java.util.Objects;

public final class Pair<T1, T2> {

    private final T1 first;
    private final T2 second;

    public static <T1, T2> Pair<T1, T2> of(T1 first, T2 second) {
        return new Pair<T1, T2>(first, second);
    }

    private Pair(T1 first, T2 second) {
        this.first = first;
        this.second = second;
    }

    public T1 getFirst() {
        return first;
    }

    public T2 getSecond() {
        return second;
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Pair<?, ?> other = (Pair<?, ?>) obj;
        return Objects.equals(this.first, other.first) && Objects.equals(this.second, other.second);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [first=" + first + ", second=" + second + "]";
    }

}
