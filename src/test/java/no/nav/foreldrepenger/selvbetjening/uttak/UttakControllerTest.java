package no.nav.foreldrepenger.selvbetjening.uttak;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import no.nav.foreldrepenger.regler.uttak.beregnkontoer.Minsterett;
import no.nav.foreldrepenger.regler.uttak.beregnkontoer.StønadskontoBeregningStønadskontotype;

class UttakControllerTest {

    @Test
    void skal_kaste_exception_hvis_manglende_familiehendelse() {
        var controller = new UttakController();
        LocalDate fødselsdato = null;
        LocalDate termindato = null;
        LocalDate omsorgsovertakelseDato = null;

        assertThrows(ManglendeFamiliehendelseException.class,
            () -> controller.beregn(1, true, true, false, false, fødselsdato, termindato, omsorgsovertakelseDato, "80", true,
                true, false, null));
    }

    @Test
    void bfhr() {
        var controller = new UttakController();
        var resultat = controller.beregn(1, false, true, false, false, LocalDate.now(), LocalDate.now().minusWeeks(1),
            null, "100", false, true, false, null);

        assertThat(resultat.kontoer()).containsEntry(StønadskontoBeregningStønadskontotype.FORELDREPENGER, 200);
        assertThat(resultat.minsteretter().generellMinsterett()).isEqualTo(Minsterett.BFHR_MINSTERETT_DAGER);
        assertThat(resultat.minsteretter().farRundtFødsel()).isEqualTo(Minsterett.UTTAK_RUNDT_FØDSEL_DAGER);

    }

}
