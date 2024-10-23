package no.nav.foreldrepenger.selvbetjening.innsyn.inntektsmelding;

import no.nav.foreldrepenger.common.domain.Saksnummer;
import no.nav.foreldrepenger.common.innsyn.inntektsmelding.FpOversiktInntektsmeldingDto;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class InntektsmeldingControllerTest {


    private static final String FAKE_SAKSNUMMER = "123456789";
    private InntektsmeldingTjeneste inntektsmeldingTjeneste = mock(InntektsmeldingTjeneste.class);

    @Test
    void sakErOppdatertHvisOppdateringstidspunktErEtterMottattidspunktetTilJournalposten() {
        var imController = new InntektsmeldingController(inntektsmeldingTjeneste);

        when(inntektsmeldingTjeneste.hentInntektsmeldinger(new Saksnummer(FAKE_SAKSNUMMER))).thenReturn(List.of(standardInntektsmelding()));

        var result = imController.hentInntektsmeldinger(new Saksnummer(FAKE_SAKSNUMMER));
        assertThat(result).hasSize(1);
    }

    private static FpOversiktInntektsmeldingDto standardInntektsmelding() {
        return new FpOversiktInntektsmeldingDto(
            2,
            true,
            BigDecimal.valueOf(70),
            BigDecimal.valueOf(5000),
            BigDecimal.valueOf(5000),
            "Arbeidsgiver",
            "123123123",
            "1",
            LocalDateTime.now(),
            LocalDate.now(),
            List.of(),
            List.of()
        );
    }

}
