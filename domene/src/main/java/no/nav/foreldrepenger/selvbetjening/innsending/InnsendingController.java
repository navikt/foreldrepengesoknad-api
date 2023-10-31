package no.nav.foreldrepenger.selvbetjening.innsending;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import jakarta.validation.Valid;
import no.nav.foreldrepenger.common.domain.Kvittering;
import no.nav.foreldrepenger.selvbetjening.http.ProtectedRestController;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.SøknadDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.endringssøknad.EndringssøknadDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.engangsstønad.EngangsstønadV2Dto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.ettersendelse.EttersendelseDto;

@ProtectedRestController(InnsendingController.INNSENDING_CONTROLLER_PATH)
public class InnsendingController {
    public static final String INNSENDING_CONTROLLER_PATH = "/rest/soknad";

    private final InnsendingTjeneste innsending;

    public InnsendingController(InnsendingTjeneste innsending) {
        this.innsending = innsending;
    }

    @PostMapping
    public Kvittering sendInn(@Valid @RequestBody SøknadDto søknad) {
        return innsending.sendInn(søknad);
    }

    @PostMapping("/engangssoknad")
    public Kvittering sendInnEngangsstønad(@Valid @RequestBody EngangsstønadV2Dto søknad) {
        return innsending.sendInn(søknad);
    }

    @PostMapping("/endre")
    public Kvittering endre(@Valid @RequestBody EndringssøknadDto endringssøknad) {
        return innsending.sendInn(endringssøknad);
    }

    @PostMapping("/ettersend")
    public Kvittering sendInn(@Valid @RequestBody EttersendelseDto ettersending) {
        return innsending.sendInn(ettersending);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[innsending=" + innsending + "]";
    }

}
