package no.nav.foreldrepenger.selvbetjening.innsyn;

import no.nav.foreldrepenger.common.domain.Fødselsnummer;
import no.nav.foreldrepenger.selvbetjening.http.TokenUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.env.MockEnvironment;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class InnsynControllerTest {

    private static final Fødselsnummer DUMMY_FNR = new Fødselsnummer("0000");
    private Innsyn innsyn = mock(Innsyn.class);
    private TokenUtil tokenUtil = mock(TokenUtil.class);

    @BeforeEach
    void setUp() {
        when(tokenUtil.innloggetBrukerOrElseThrowException()).thenReturn(DUMMY_FNR);
    }


    @Test
    void skalAlltidKreveDokumentasjonAvMorIArbeid() {
        var environment = new MockEnvironment();
        environment.setActiveProfiles("dev-gcp");
        var innsynController = new InnsynController(innsyn, environment);
        when(innsyn.trengerDokumentereMorsArbeid(any())).thenReturn(false);

        var perioder = List.of(new MorArbeidRequestDto.Periode(LocalDate.now().minusYears(2), LocalDate.now()));
        var måDokumentereMorIArbeid = innsynController.trengerDokumentereMorsArbeid(new MorArbeidRequestDto(
                DUMMY_FNR,
                DUMMY_FNR,
                LocalDate.now(),
                perioder));

        assertThat(måDokumentereMorIArbeid).isFalse();
    }

    @Test
    void skalAlltidKreveDokumentasjonAvMorIArbeidIProd() {
        var environment = new MockEnvironment();
        environment.setActiveProfiles("prod-gcp");
        var innsynController = new InnsynController(innsyn, environment);

        var perioder = List.of(new MorArbeidRequestDto.Periode(LocalDate.now().minusYears(2), LocalDate.now()));
        var måDokumentereMorIArbeid = innsynController.trengerDokumentereMorsArbeid(new MorArbeidRequestDto(
                DUMMY_FNR,
                DUMMY_FNR,
                LocalDate.now(),
                perioder));

        assertThat(måDokumentereMorIArbeid).isTrue();
    }
}
