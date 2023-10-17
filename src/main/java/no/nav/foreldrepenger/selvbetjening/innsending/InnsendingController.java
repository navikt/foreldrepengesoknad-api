package no.nav.foreldrepenger.selvbetjening.innsending;

import static no.nav.boot.conditionals.EnvUtil.CONFIDENTIAL;
import static no.nav.foreldrepenger.selvbetjening.util.StringUtils.escapeHtml;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import jakarta.validation.Valid;
import no.nav.foreldrepenger.common.domain.Kvittering;
import no.nav.foreldrepenger.selvbetjening.http.ProtectedRestController;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.endringssøknad.EndringssøknadDto;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.SøknadDto;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.ettersendelse.EttersendelseDto;

@ProtectedRestController(InnsendingController.INNSENDING_CONTROLLER_PATH)
public class InnsendingController {
    private static final Logger LOG = LoggerFactory.getLogger(InnsendingController.class);

    public static final String INNSENDING_CONTROLLER_PATH = "/rest/soknad";

    private final InnsendingTjeneste innsending;

    public InnsendingController(InnsendingTjeneste innsending) {
        this.innsending = innsending;
    }

    @PostMapping
    public Kvittering sendInn(@Valid @RequestBody SøknadDto søknad) {
        LOG.info("Mottok søknad med målform {} og {} vedlegg", søknad.søker().språkkode(), søknad.vedlegg().size());
        if (LOG.isInfoEnabled() && LOG.isInfoEnabled(CONFIDENTIAL)) {
            LOG.info(CONFIDENTIAL, "Søker er {}", escapeHtml(søknad.søker()));
        }
        return innsending.sendInn(søknad);
    }

    @PostMapping("/ettersend")
    public Kvittering sendInn(@Valid @RequestBody EttersendelseDto ettersending) {
        LOG.info("Mottok ettersending av {} vedlegg", ettersending.vedlegg().size());
        return innsending.ettersend(ettersending);
    }

    @PostMapping("/endre")
    public Kvittering endre(@Valid @RequestBody EndringssøknadDto endringssøknad) {
        LOG.info("Mottok endringssøknad med {} vedlegg", endringssøknad.vedlegg().size());
        return innsending.endre(endringssøknad);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[innsending=" + innsending + "]";
    }

}
