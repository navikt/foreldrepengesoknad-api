package no.nav.foreldrepenger.selvbetjening.innsyn.inntektsmelding;

import no.nav.foreldrepenger.common.domain.Saksnummer;
import no.nav.foreldrepenger.common.innsyn.inntektsmelding.FpOversiktInntektsmeldingDto;
import no.nav.foreldrepenger.selvbetjening.innsyn.Innsyn;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InntektsmeldingTjeneste {

    private final Innsyn innsyn;

    public InntektsmeldingTjeneste(Innsyn innsyn) {
        this.innsyn = innsyn;
    }

    public List<FpOversiktInntektsmeldingDto> hentInntektsmeldinger(Saksnummer saksnummer) {
        return innsyn.inntektsmeldinger(saksnummer);
    }
}
