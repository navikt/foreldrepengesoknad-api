package no.nav.foreldrepenger.selvbetjening.util;

import static java.time.LocalDate.now;
import static java.time.temporal.TemporalAdjusters.firstDayOfYear;

import java.time.LocalDate;
import java.time.Month;

public final class DateUtil {

    private DateUtil() {
    }

    public static boolean erNyopprettet(LocalDate fom) {
        return erNyoppstartet(LocalDate.now(), fom);
    }

    static boolean erNyoppstartet(LocalDate nå, LocalDate fom) {
        return fom.isAfter(now().minusYears(nå.isAfter(LocalDate.of(nå.getYear(), Month.OCTOBER, 20)) ? 3 : 4).with(firstDayOfYear()).minusDays(1));
    }

}
