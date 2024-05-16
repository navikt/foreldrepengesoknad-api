package no.nav.foreldrepenger.selvbetjening.uttak;

import static no.nav.foreldrepenger.selvbetjening.uttak.KontoBeregningDto.KontoDto.KontoType.AKTIVITETSFRI_KVOTE;
import static no.nav.foreldrepenger.selvbetjening.uttak.KontoBeregningDto.KontoDto.KontoType.FORELDREPENGER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.core.env.Environment;
import org.springframework.mock.env.MockEnvironment;

import no.nav.foreldrepenger.stønadskonto.regelmodell.grunnlag.Brukerrolle;
import no.nav.foreldrepenger.stønadskonto.regelmodell.grunnlag.Rettighetstype;


class UttakControllerTest {

    private Environment environment = new MockEnvironment();
    private UttakCore2024 uttakCore2024 = new UttakCore2024(null, null, environment);

    @Test
    void skal_kaste_exception_hvis_manglende_familiehendelse() {
        var controller = new UttakController(uttakCore2024);
        LocalDate fødselsdato = null;
        LocalDate termindato = null;
        LocalDate omsorgsovertakelseDato = null;

        assertThrows(ManglendeFamiliehendelseException.class,
            () -> controller.beregnMedDekningsgrad(
                1,
                true,
                true,
                false,
                false,
                fødselsdato,
                termindato,
                omsorgsovertakelseDato,
                "80",
                true,
                true,
                false,
                false,
                null));
        assertThrows(ManglendeFamiliehendelseException.class,
            () -> controller.beregn(
                new KontoBeregningGrunnlagDto(
                    Rettighetstype.BEGGE_RETT,
                    Brukerrolle.MOR,
                    1,
                    fødselsdato,
                    termindato,
                    omsorgsovertakelseDato,
                    false,
                    null
                    )
            ));
    }

    @Test
    void bfhr() {
        var controller = new UttakController(uttakCore2024);
        var resultat = controller.beregnMedDekningsgrad(1, false, true, false, false, LocalDate.now(), LocalDate.now().minusWeeks(1),
            null, "100", false, true, false, false, null);

        assertThat(resultat.kontoer()).containsEntry(KontoBeregning.StønadskontoBeregningStønadskontotype.FORELDREPENGER, 200);
        assertThat(resultat.minsteretter().generellMinsterett()).isEqualTo(40);
        assertThat(resultat.minsteretter().farRundtFødsel()).isEqualTo(10);

    }
    @Test
    void bfhrNY() {
        var controller = new UttakController(uttakCore2024);
        var grunnlag = new KontoBeregningGrunnlagDto(
            Rettighetstype.BARE_SØKER_RETT,
            Brukerrolle.FAR,
            1,
            LocalDate.now(),
            LocalDate.now().minusWeeks(1),
            null,
            false,
            null
        );
        var resultat = controller.beregn(grunnlag);


        assertThat(resultat).containsOnlyKeys("80", "100");
        var kontoberegning100 = resultat.get("100");
        assertThat(kontoberegning100.kontoer()).contains(new KontoBeregningDto.KontoDto(FORELDREPENGER, 160));
        assertThat(kontoberegning100.kontoer()).contains(new KontoBeregningDto.KontoDto(AKTIVITETSFRI_KVOTE, 40));
        assertThat(kontoberegning100.minsteretter().farRundtFødsel()).isEqualTo(10);

        var kontoberegning80 = resultat.get("80");
        assertThat(kontoberegning80.kontoer()).contains(new KontoBeregningDto.KontoDto(FORELDREPENGER, 210));
        assertThat(kontoberegning100.kontoer()).contains(new KontoBeregningDto.KontoDto(AKTIVITETSFRI_KVOTE, 40));
        assertThat(kontoberegning80.minsteretter().farRundtFødsel()).isEqualTo(10);

    }

    @Test
    void aleneomsorg() {
        var controller = new UttakController(uttakCore2024);
        var resultat = controller.beregnMedDekningsgrad(1, false, true, false, true, null, LocalDate.now(),
            null, "100", false, true, false, false, null);

        assertThat(resultat.kontoer()).containsEntry(KontoBeregning.StønadskontoBeregningStønadskontotype.FORELDREPENGER, 46 * 5);
        assertThat(resultat.minsteretter().generellMinsterett()).isZero();
    }

    @Test
    void bfhr_flere_barn_minsterett_skal_ikke_ha_flerbarnsdager() {
        var controller = new UttakController(uttakCore2024);
        var resultat = controller.beregnMedDekningsgrad(2, false, true, false, false, LocalDate.now(), LocalDate.now().minusWeeks(1),
            null, "100", false, true, false, false, null);

        assertThat(resultat.kontoer()).containsEntry(KontoBeregning.StønadskontoBeregningStønadskontotype.FORELDREPENGER, 285);
        assertThat(resultat.kontoer()).doesNotContainKey(KontoBeregning.StønadskontoBeregningStønadskontotype.FLERBARNSDAGER);
        assertThat(resultat.minsteretter().generellMinsterett()).isEqualTo(85);
        assertThat(resultat.minsteretter().farRundtFødsel()).isEqualTo(10);

    }

    @Test
    void morEøs() {
        var controller = new UttakController(uttakCore2024);
        var resultat = controller.beregnMedDekningsgrad(1, false, true, false, false, LocalDate.now(), LocalDate.now().minusWeeks(1),
            null, "100", false, true, false, true, null);

        assertThat(resultat.kontoer()).doesNotContainKey(KontoBeregning.StønadskontoBeregningStønadskontotype.FORELDREPENGER);
        assertThat(resultat.minsteretter().generellMinsterett()).isZero();
        assertThat(resultat.minsteretter().farRundtFødsel()).isEqualTo(10);
        assertThat(resultat.kontoer()).containsEntry(KontoBeregning.StønadskontoBeregningStønadskontotype.FEDREKVOTE, 75);
    }

}
