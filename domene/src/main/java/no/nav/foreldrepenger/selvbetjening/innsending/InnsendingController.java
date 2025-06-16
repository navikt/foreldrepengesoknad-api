package no.nav.foreldrepenger.selvbetjening.innsending;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import jakarta.validation.Valid;
import no.nav.foreldrepenger.common.domain.Kvittering;
import no.nav.foreldrepenger.selvbetjening.http.ProtectedRestController;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.endringssøknad.EndringssøknadForeldrepengerDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.engangsstønad.EngangsstønadDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.ettersendelse.EttersendelseDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.foreldrepenger.ForeldrepengesøknadDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.svangerskapspenger.SvangerskapspengesøknadDto;

@ProtectedRestController(InnsendingController.INNSENDING_CONTROLLER_PATH)
public class InnsendingController {
    public static final String INNSENDING_CONTROLLER_PATH = "/rest/soknad";

    private final InnsendingTjeneste innsending;

    public InnsendingController(InnsendingTjeneste innsending) {
        this.innsending = innsending;
    }

    @PostMapping("/foreldrepenger")
    public Kvittering sendInnForeldrepengesøknad(@Valid @RequestBody ForeldrepengesøknadDto søknad) {
        return innsending.sendInn(søknad);
    }

    @PostMapping("/foreldrepenger/endre")
    public Kvittering sendInnEndringssøknadForeldrepenger(@Valid @RequestBody EndringssøknadForeldrepengerDto endringssøknad) {
        return innsending.sendInn(endringssøknad);
    }

    @PostMapping("/svangerskapspenger")
    public Kvittering sendInnForeldrepengesøknad(@Valid @RequestBody SvangerskapspengesøknadDto søknad) {
        return innsending.sendInn(søknad);
    }

    @PostMapping("/engangssoknad")
    public Kvittering sendInnEngangsstønad(@Valid @RequestBody EngangsstønadDto søknad) {
        return innsending.sendInn(søknad);
    }

    @PostMapping("/engangsstonad")
    public Kvittering sendInnEngangsstonad(@Valid @RequestBody EngangsstønadDto søknad) {
        return innsending.sendInn(søknad);
    }

    @PostMapping("/ettersend")
    public Kvittering ettersend(@Valid @RequestBody EttersendelseDto ettersending) {
        return innsending.ettersend(ettersending);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[innsending=" + innsending + "]";
    }

}
