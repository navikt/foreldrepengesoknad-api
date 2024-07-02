package no.nav.foreldrepenger.selvbetjening.uttak;

import static no.nav.foreldrepenger.selvbetjening.uttak.KontoBeregningDto.KontoDto.KontoType.AKTIVITETSFRI_KVOTE;
import static no.nav.foreldrepenger.selvbetjening.uttak.KontoBeregningDto.KontoDto.KontoType.FEDREKVOTE;
import static no.nav.foreldrepenger.selvbetjening.uttak.KontoBeregningDto.KontoDto.KontoType.FELLESPERIODE;
import static no.nav.foreldrepenger.selvbetjening.uttak.KontoBeregningDto.KontoDto.KontoType.FORELDREPENGER;
import static no.nav.foreldrepenger.selvbetjening.uttak.KontoBeregningDto.KontoDto.KontoType.FORELDREPENGER_FØR_FØDSEL;
import static no.nav.foreldrepenger.selvbetjening.uttak.KontoBeregningDto.KontoDto.KontoType.MØDREKVOTE;
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
        var grunnlag = new KontoBeregningGrunnlagDto(
            Rettighetstype.BEGGE_RETT,
            Brukerrolle.MOR,
            1,
            null,
            null,
            null,
            false,
            null);
        assertThrows(ManglendeFamiliehendelseException.class, () -> controller.beregn(grunnlag));
    }

    @Test
    void mor_og_far_rett_to_tette() {
        var controller = new UttakController(uttakCore2024);
        var grunnlag = new KontoBeregningGrunnlagDto(
            Rettighetstype.BEGGE_RETT,
            Brukerrolle.MOR,
            1,
            LocalDate.now().minusMonths(7),
            LocalDate.now().minusMonths(7).minusWeeks(1),
            null,
            false,
            LocalDate.now()
        );
        var resultat = controller.beregn(grunnlag);

        assertThat(resultat).containsOnlyKeys("80", "100");
        var kontoberegning100 = resultat.get("100");
        assertThat(kontoberegning100.kontoer()).containsExactlyInAnyOrder(
            new KontoBeregningDto.KontoDto(FORELDREPENGER_FØR_FØDSEL, 15),
            new KontoBeregningDto.KontoDto(MØDREKVOTE, 75),
            new KontoBeregningDto.KontoDto(FEDREKVOTE, 75),
            new KontoBeregningDto.KontoDto(FELLESPERIODE, 80)
        );

        var kontoberegning80 = resultat.get("80");
        assertThat(kontoberegning80.kontoer()).containsExactlyInAnyOrder(
            new KontoBeregningDto.KontoDto(FORELDREPENGER_FØR_FØDSEL, 15),
            new KontoBeregningDto.KontoDto(MØDREKVOTE, 95),
            new KontoBeregningDto.KontoDto(FEDREKVOTE, 95),
            new KontoBeregningDto.KontoDto(FELLESPERIODE, 90)
        );

        assertThat(kontoberegning100.minsteretter().farRundtFødsel())
            .isEqualTo(kontoberegning80.minsteretter().farRundtFødsel())
            .isEqualTo(10);
        assertThat(kontoberegning100.minsteretter().toTette())
            .isEqualTo(kontoberegning80.minsteretter().toTette())
            .isEqualTo(110);
    }

    @Test
    void bfhr() {
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

        var kontoberegning80 = resultat.get("80");
        assertThat(kontoberegning80.kontoer()).contains(new KontoBeregningDto.KontoDto(FORELDREPENGER, 221));
        assertThat(kontoberegning100.kontoer()).contains(new KontoBeregningDto.KontoDto(AKTIVITETSFRI_KVOTE, 40));
        assertThat(kontoberegning100.minsteretter().farRundtFødsel())
            .isEqualTo(kontoberegning80.minsteretter().farRundtFødsel())
            .isEqualTo(10);
    }

    @Test
    void mor_aleneomsorg() {
        var controller = new UttakController(uttakCore2024);
        var grunnlag = new KontoBeregningGrunnlagDto(
            Rettighetstype.ALENEOMSORG,
            Brukerrolle.MOR,
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
        assertThat(kontoberegning100.kontoer()).containsExactlyInAnyOrder(
            new KontoBeregningDto.KontoDto(FORELDREPENGER_FØR_FØDSEL, 15),
            new KontoBeregningDto.KontoDto(FORELDREPENGER, 230)
        );

        var kontoberegning80 = resultat.get("80");
        assertThat(kontoberegning80.kontoer()).containsExactlyInAnyOrder(
            new KontoBeregningDto.KontoDto(FORELDREPENGER_FØR_FØDSEL, 15),
            new KontoBeregningDto.KontoDto(FORELDREPENGER, 291)
            );

        assertThat(kontoberegning100.minsteretter().farRundtFødsel())
            .isEqualTo(kontoberegning80.minsteretter().farRundtFødsel())
            .isZero();
    }

    @Test
    void bfhr_flere_barn_minsterett_skal_ikke_ha_flerbarnsdager() {
        var controller = new UttakController(uttakCore2024);
        var grunnlag = new KontoBeregningGrunnlagDto(
            Rettighetstype.BARE_SØKER_RETT,
            Brukerrolle.FAR,
            2,
            null,
            LocalDate.now().minusWeeks(1),
            null,
            false,
            null
        );
        var resultat = controller.beregn(grunnlag);


        assertThat(resultat).containsOnlyKeys("80", "100");
        var kontoberegning100 = resultat.get("100");
        assertThat(kontoberegning100.kontoer()).containsExactlyInAnyOrder(
            new KontoBeregningDto.KontoDto(AKTIVITETSFRI_KVOTE, 85),
            new KontoBeregningDto.KontoDto(FORELDREPENGER, 200)
        );

        var kontoberegning80 = resultat.get("80");
        assertThat(kontoberegning80.kontoer()).containsExactlyInAnyOrder(
            new KontoBeregningDto.KontoDto(AKTIVITETSFRI_KVOTE, 105),
            new KontoBeregningDto.KontoDto(FORELDREPENGER, 250)
        );
        assertThat(kontoberegning100.minsteretter().farRundtFødsel())
            .isEqualTo(kontoberegning80.minsteretter().farRundtFødsel())
            .isEqualTo(10);
    }
}
