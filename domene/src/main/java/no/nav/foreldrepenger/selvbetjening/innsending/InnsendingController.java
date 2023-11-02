package no.nav.foreldrepenger.selvbetjening.innsending;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import jakarta.validation.Valid;
import no.nav.foreldrepenger.common.domain.Kvittering;
import no.nav.foreldrepenger.selvbetjening.http.ProtectedRestController;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.SøknadDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.endringssøknad.EndringssøknadDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.ettersendelse.EttersendelseDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.engangsstønad.EngangsstønadDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.foreldrepenger.ForeldrepengesøknadDto;

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

    @PostMapping("/foreldrepenger")
    public Kvittering sendInnForeldrepengesøknad(@Valid @RequestBody ForeldrepengesøknadDto søknad) {
        return innsending.sendInn(søknad);
    }

    @PostMapping("/engangssoknad")
    public Kvittering sendInnEngangsstønad(@Valid @RequestBody EngangsstønadDto søknad) {
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
