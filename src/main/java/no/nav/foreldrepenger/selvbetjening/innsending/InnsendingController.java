package no.nav.foreldrepenger.selvbetjening.innsending;

import static no.nav.boot.conditionals.EnvUtil.CONFIDENTIAL;
import static org.apache.commons.lang3.StringEscapeUtils.escapeHtml4;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import jakarta.validation.Valid;
import no.nav.foreldrepenger.common.domain.Kvittering;
import no.nav.foreldrepenger.selvbetjening.http.ProtectedRestController;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.EttersendingFrontend;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.SøknadFrontend;

@ProtectedRestController(InnsendingController.INNSENDING_CONTROLLER_PATH)
public class InnsendingController {
    private static final Logger LOG = LoggerFactory.getLogger(InnsendingController.class);

    public static final String INNSENDING_CONTROLLER_PATH = "/rest/soknad";

    private final Innsending innsending;

    public InnsendingController(Innsending innsending) {
        this.innsending = innsending;
    }

    @PostMapping
    public Kvittering sendInn(@Valid @RequestBody SøknadFrontend søknad) {
        LOG.info("Mottok søknad med målform {} og {} vedlegg", søknad.getSøker().språkkode(), søknad.getVedlegg().size());
        if (LOG.isInfoEnabled() && LOG.isInfoEnabled(CONFIDENTIAL)) {
            LOG.info(CONFIDENTIAL, "{}", escapeHtml4(søknad.toString()));
            LOG.info(CONFIDENTIAL, "Søker er {}", escapeHtml4(søknad.getSøker().toString()));
        }
        return innsending.sendInn(søknad);
    }

    @PostMapping("/ettersend")
    public Kvittering sendInn(@Valid @RequestBody EttersendingFrontend ettersending) {
        LOG.info("Mottok ettersending av {} vedlegg", ettersending.vedlegg().size());
        if (LOG.isInfoEnabled() && LOG.isInfoEnabled(CONFIDENTIAL)) {
            LOG.info(CONFIDENTIAL, "{}", escapeHtml4(ettersending.toString()));
        }
        return innsending.ettersend(ettersending);
    }

    @PostMapping("/endre")
    public Kvittering endre(@Valid @RequestBody SøknadFrontend søknad) {
        LOG.info("Mottok endringssøknad med {} vedlegg", søknad.getVedlegg().size());
        if (LOG.isInfoEnabled() && LOG.isInfoEnabled(CONFIDENTIAL)) {
            LOG.info(CONFIDENTIAL, "{}", escapeHtml4(søknad.toString()));
        }
        return innsending.endre(søknad);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[innsending=" + innsending + "]";
    }

}
