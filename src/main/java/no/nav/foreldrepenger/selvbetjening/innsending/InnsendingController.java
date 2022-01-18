package no.nav.foreldrepenger.selvbetjening.innsending;

import static no.nav.foreldrepenger.boot.conditionals.EnvUtil.CONFIDENTIAL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import no.nav.foreldrepenger.selvbetjening.http.ProtectedRestController;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.Ettersending;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.Kvittering;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.SøknadFrontend;

@ProtectedRestController(InnsendingController.REST_SOKNAD)
public class InnsendingController {

    private static final Logger LOG = LoggerFactory.getLogger(InnsendingController.class);

    public static final String REST_SOKNAD = "/rest/soknad";

    private final Innsending innsending;

    public InnsendingController(Innsending innsending) {
        this.innsending = innsending;
    }

    @PostMapping
    public Kvittering sendInn(@RequestBody SøknadFrontend søknad) {
        LOG.info("Mottok søknad med målform {} og {} vedlegg", søknad.getSøker().språkkode(), søknad.getVedlegg().size());
        LOG.info(CONFIDENTIAL, "{}", søknad);
        LOG.info(CONFIDENTIAL, "Søker er {}", søknad.getSøker());
        return innsending.sendInn(søknad);
    }

    @PostMapping("/ettersend")
    public Kvittering sendInn(@RequestBody Ettersending ettersending) {
        LOG.info("Mottok ettersending av {} vedlegg", ettersending.vedlegg().size());
        LOG.info(CONFIDENTIAL, "{}", ettersending);
        return innsending.ettersend(ettersending);
    }

    @PostMapping("/endre")
    public Kvittering endre(@RequestBody SøknadFrontend søknad) {
        LOG.info("Mottok endringssøknad med {} vedlegg", søknad.getVedlegg().size());
        LOG.info(CONFIDENTIAL, "{}", søknad);
        return innsending.endre(søknad);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[innsending=" + innsending + "]";
    }

}
