package no.nav.foreldrepenger.selvbetjening.innsending;

import static org.slf4j.LoggerFactory.getLogger;

import org.slf4j.Logger;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import jakarta.validation.Valid;
import no.nav.foreldrepenger.common.domain.Kvittering;
import no.nav.foreldrepenger.selvbetjening.http.ProtectedRestController;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.SøknadDtoOLD;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.endringssøknad.EndringssøknadDtoOLD;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.ettersendelse.EttersendelseDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.endringssøknad.EndringssøknadForeldrepengerDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.engangsstønad.EngangsstønadDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.foreldrepenger.ForeldrepengesøknadDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.svangerskapspenger.SvangerskapspengesøknadDto;

@ProtectedRestController(InnsendingController.INNSENDING_CONTROLLER_PATH)
public class InnsendingController {
    private static final Logger LOG = getLogger(InnsendingController.class);
    public static final String INNSENDING_CONTROLLER_PATH = "/rest/soknad";

    private final InnsendingTjeneste innsending;

    public InnsendingController(InnsendingTjeneste innsending) {
        this.innsending = innsending;
    }

    @Deprecated
    @PostMapping
    public Kvittering sendInn(@Valid @RequestBody SøknadDtoOLD søknad) {
        LOG.info("Kall på deprecated endpunkt for {}", søknad.navn());
        return innsending.sendInn(søknad);
    }

    @Deprecated
    @PostMapping("/endre")
    public Kvittering endre(@Valid @RequestBody EndringssøknadDtoOLD endringssøknad) {
        LOG.info("Kall på deprecated endpunkt for {}", endringssøknad.navn());
        return innsending.sendInn(endringssøknad);
    }

    @PostMapping("/foreldrepenger")
    public Kvittering sendInnForeldrepengesøknad(@Valid @RequestBody ForeldrepengesøknadDto søknad) {
        return innsending.sendInn(søknad);
    }

    @PostMapping("/foreldrepenger/endre")
    public Kvittering endre(@Valid @RequestBody EndringssøknadForeldrepengerDto endringssøknad) {
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
