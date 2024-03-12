package no.nav.foreldrepenger.selvbetjening.uttak;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import no.nav.foreldrepenger.stønadskonto.regelmodell.StønadskontoBeregningStønadskontotype;


class UttakControllerTest {

    @Test
    void skal_kaste_exception_hvis_manglende_familiehendelse() {
        var controller = new UttakController();
        LocalDate fødselsdato = null;
        LocalDate termindato = null;
        LocalDate omsorgsovertakelseDato = null;

        assertThrows(ManglendeFamiliehendelseException.class,
            () -> controller.beregnMedDekningsgrad(1, true, true, false, false, fødselsdato, termindato, omsorgsovertakelseDato, "80", true,
                true, false, false, null));
        assertThrows(ManglendeFamiliehendelseException.class,
            () -> controller.beregn(new KontoBeregningGrunnlagDto(1, true, true, false, false, fødselsdato, termindato, omsorgsovertakelseDato, true,
                true, false, false, null)));
    }

    @Test
    void bfhr() {
        var controller = new UttakController();
        var resultat = controller.beregnMedDekningsgrad(1, false, true, false, false, LocalDate.now(), LocalDate.now().minusWeeks(1),
            null, "100", false, true, false, false, null);

        assertThat(resultat.kontoer()).containsEntry(StønadskontoBeregningStønadskontotype.FORELDREPENGER, 200);
        assertThat(resultat.minsteretter().generellMinsterett()).isEqualTo(40);
        assertThat(resultat.minsteretter().farRundtFødsel()).isEqualTo(10);

    }
    @Test
    void bfhrNY() {
        var controller = new UttakController();
        var grunnlag = new KontoBeregningGrunnlagDto(1, false, true, false, false, LocalDate.now(), LocalDate.now().minusWeeks(1), null, false, true,
            false, false, null);
        var resultat = controller.beregn(grunnlag);


        assertThat(resultat).containsOnlyKeys("80", "100");
        var kontoberegning100 = resultat.get("100");
        assertThat(kontoberegning100.kontoer()).containsEntry(StønadskontoBeregningStønadskontotype.FORELDREPENGER, 200);
        assertThat(kontoberegning100.minsteretter().generellMinsterett()).isEqualTo(40);
        assertThat(kontoberegning100.minsteretter().farRundtFødsel()).isEqualTo(10);

        var kontoberegning80 = resultat.get("80");
        assertThat(kontoberegning80.kontoer()).containsEntry(StønadskontoBeregningStønadskontotype.FORELDREPENGER, 250);
        assertThat(kontoberegning80.minsteretter().generellMinsterett()).isEqualTo(40);
        assertThat(kontoberegning80.minsteretter().farRundtFødsel()).isEqualTo(10);

    }

    @Test
    void aleneomsorg() {
        var controller = new UttakController();
        var resultat = controller.beregnMedDekningsgrad(1, false, true, false, true, null, LocalDate.now(),
            null, "100", false, true, false, false, null);

        assertThat(resultat.kontoer()).containsEntry(StønadskontoBeregningStønadskontotype.FORELDREPENGER, 46 * 5);
        assertThat(resultat.minsteretter().generellMinsterett()).isZero();
    }

    @Test
    void bfhr_flere_barn_minsterett_skal_ikke_ha_flerbarnsdager() {
        var controller = new UttakController();
        var resultat = controller.beregnMedDekningsgrad(2, false, true, false, false, LocalDate.now(), LocalDate.now().minusWeeks(1),
            null, "100", false, true, false, false, null);

        assertThat(resultat.kontoer()).containsEntry(StønadskontoBeregningStønadskontotype.FORELDREPENGER, 285);
        assertThat(resultat.kontoer()).doesNotContainKey(StønadskontoBeregningStønadskontotype.FLERBARNSDAGER);
        assertThat(resultat.minsteretter().generellMinsterett()).isEqualTo(85);
        assertThat(resultat.minsteretter().farRundtFødsel()).isEqualTo(10);

    }

    @Test
    void morEøs() {
        var controller = new UttakController();
        var resultat = controller.beregnMedDekningsgrad(1, false, true, false, false, LocalDate.now(), LocalDate.now().minusWeeks(1),
            null, "100", false, true, false, true, null);

        assertThat(resultat.kontoer()).doesNotContainKey(StønadskontoBeregningStønadskontotype.FORELDREPENGER);
        assertThat(resultat.minsteretter().generellMinsterett()).isZero();
        assertThat(resultat.minsteretter().farRundtFødsel()).isEqualTo(10);
        assertThat(resultat.kontoer()).containsEntry(StønadskontoBeregningStønadskontotype.FEDREKVOTE, 75);
    }

}
