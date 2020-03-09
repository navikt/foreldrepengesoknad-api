package no.nav.foreldrepenger.selvbetjening.util;

import static no.nav.foreldrepenger.selvbetjening.util.TimeUtil.erNyoppstartet;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.Month;

import org.junit.jupiter.api.Test;

public class TimeUtilTest {

    private static final LocalDate FØR = LocalDate.of(LocalDate.now().getYear(), Month.MAY, 1);
    private static final LocalDate ETTER = LocalDate.of(LocalDate.now().getYear(), Month.DECEMBER, 1);
    private static final LocalDate PÅ = LocalDate.of(LocalDate.now().getYear(), Month.OCTOBER, 20);

    @Test
    public void testFørLigningsårAvsluttet() {
        assertTrue(erNyoppstartet(FØR, LocalDate.of(LocalDate.now().minusYears(4).getYear(), Month.JUNE, 24)));
        assertFalse(erNyoppstartet(FØR, LocalDate.of(LocalDate.now().minusYears(5).getYear(), Month.JUNE, 24)));
    }

    @Test
    public void testEtterLigningsårAvsluttet() {
        assertTrue(erNyoppstartet(ETTER, LocalDate.of(LocalDate.now().minusYears(3).getYear(), Month.JUNE, 24)));
        assertFalse(erNyoppstartet(ETTER, LocalDate.of(LocalDate.now().minusYears(4).getYear(), Month.JUNE, 24)));
    }

    @Test
    public void testAkkuratAvsluttet() {
        assertTrue(erNyoppstartet(PÅ, PÅ.minusYears(4)));
    }

    @Test
    public void testAkkuratEtterAvsluttet() {
        assertFalse(erNyoppstartet(PÅ.plusDays(1), PÅ.minusYears(4)));

    }
}
