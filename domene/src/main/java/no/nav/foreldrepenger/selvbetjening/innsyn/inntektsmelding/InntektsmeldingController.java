package no.nav.foreldrepenger.selvbetjening.innsyn.inntektsmelding;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import no.nav.foreldrepenger.common.domain.Saksnummer;
import no.nav.foreldrepenger.selvbetjening.http.ProtectedRestController;
import no.nav.foreldrepenger.selvbetjening.innsyn.InntektsmeldingDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@ProtectedRestController("/rest/innsyn/inntektsmeldinger")
public class InntektsmeldingController {
    private final InntektsmeldingTjeneste inntektsmeldingTjeneste;

    @Autowired
    public InntektsmeldingController(InntektsmeldingTjeneste inntektsmeldingTjeneste) {
        this.inntektsmeldingTjeneste = inntektsmeldingTjeneste;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<InntektsmeldingDto> hentInntektsmeldinger(@RequestParam @Valid @NotNull Saksnummer saksnummer) {
        return  inntektsmeldingTjeneste.hentInntektsmeldinger(saksnummer);
    }

}
