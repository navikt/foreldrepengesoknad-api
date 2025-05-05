package no.nav.foreldrepenger.selvbetjening.oppslag.domain;

import no.nav.foreldrepenger.selvbetjening.oppslag.dto.Arbeidsforhold;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class ArbeidsforholdTest {


    @Test
    void hvis_arbeidsforhold_er_gyldig_sammenlignes_stillingsprosent_er_da_ulik() {
        var arbeidsforhold1 = new Arbeidsforhold(
                "123456789",
                "orgnr",
                "NAVN",
                100.0,
                LocalDate.now().minusYears(2),
                null
        );
        var arbeidsforhold2 = new Arbeidsforhold(
                "123456789",
                "orgnr",
                "NAVN",
                50.0,
                LocalDate.now().minusYears(2),
                null
        );
        assertThat(arbeidsforhold1).isNotEqualTo(arbeidsforhold2);
    }

    @Test
    void hvis_arbeidsforhold_er_gyldig_sammenlignes_stillingsprosent_er_da_lik() {
        var arbeidsforhold1 = new Arbeidsforhold(
                "123456789",
                "orgnr",
                "NAVN",
                100.0,
                LocalDate.now().minusYears(2),
                null
        );
        var arbeidsforhold2 = new Arbeidsforhold(
                "123456789",
                "orgnr",
                "NAVN",
                100.0,
                LocalDate.now().minusYears(2),
                null
        );
        assertThat(arbeidsforhold1).isEqualTo(arbeidsforhold2);
    }



    @Test
    void utgått_AF_skal_ikke_sammenling_stillingsprosent_siden_OVERSIKT_er_mer_riktig() {
        var arbeidsforhold1 = new Arbeidsforhold(
                "123456789",
                "orgnr",
                "NAVN",
                100.0,
                LocalDate.now().minusYears(2),
                LocalDate.now().minusMonths(5)
        );
        var arbeidsforhold2 = new Arbeidsforhold(
                "123456789",
                "orgnr",
                "NAVN",
                null,
                LocalDate.now().minusYears(2),
                LocalDate.now().minusMonths(5)
        );
        assertThat(arbeidsforhold1).isEqualTo(arbeidsforhold2);
    }
}