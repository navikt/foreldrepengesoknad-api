package no.nav.foreldrepenger.selvbetjening.util;

import static no.nav.foreldrepenger.selvbetjening.util.DateUtil.erNyopprettet;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.Month;

import org.junit.jupiter.api.Test;

class DateUtilTest {

    private static final LocalDate FØR = LocalDate.of(LocalDate.now().getYear(), Month.MAY, 1);
    private static final LocalDate ETTER = LocalDate.of(LocalDate.now().getYear(), Month.DECEMBER, 1);
    private static final LocalDate PÅ = LocalDate.of(LocalDate.now().getYear(), Month.OCTOBER, 20);

    @Test
    void testFørLigningsårAvsluttet() {
        assertTrue(erNyopprettet(FØR, LocalDate.of(LocalDate.now().minusYears(4).getYear(), Month.JUNE, 24)));
        assertFalse(erNyopprettet(FØR, LocalDate.of(LocalDate.now().minusYears(5).getYear(), Month.JUNE, 24)));
    }

    @Test
    void testEtterLigningsårAvsluttet() {
        assertTrue(erNyopprettet(ETTER, LocalDate.of(LocalDate.now().minusYears(3).getYear(), Month.JUNE, 24)));
        assertFalse(erNyopprettet(ETTER, LocalDate.of(LocalDate.now().minusYears(4).getYear(), Month.JUNE, 24)));
    }

    @Test
    void testAkkuratAvsluttet() {
        assertTrue(erNyopprettet(PÅ, PÅ.minusYears(4)));
    }

    @Test
    void testAkkuratEtterAvsluttet() {
        assertFalse(erNyopprettet(PÅ.plusDays(1), PÅ.minusYears(4)));

    }
}
