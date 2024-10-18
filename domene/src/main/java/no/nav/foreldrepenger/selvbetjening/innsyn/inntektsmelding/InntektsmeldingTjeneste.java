package no.nav.foreldrepenger.selvbetjening.innsyn.inntektsmelding;

import no.nav.foreldrepenger.common.domain.Saksnummer;
import no.nav.foreldrepenger.selvbetjening.innsyn.Innsyn;
import no.nav.foreldrepenger.selvbetjening.innsyn.InntektsmeldingDto;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InntektsmeldingTjeneste {

    private final Innsyn innsyn;

    public InntektsmeldingTjeneste(Innsyn innsyn) {
        this.innsyn = innsyn;
    }

    public List<InntektsmeldingDto> hentInntektsmeldinger(Saksnummer saksnummer) {
        return innsyn.inntektsmeldinger(saksnummer);
    }
}
