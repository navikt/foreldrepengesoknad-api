package no.nav.foreldrepenger.selvbetjening.tjeneste.innsending;

import static no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.InnsendingController.REST_SOKNAD;
import static no.nav.foreldrepenger.selvbetjening.util.Constants.ISSUER;
import static no.nav.foreldrepenger.selvbetjening.util.EnvUtil.CONFIDENTIAL;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.domain.Ettersending;
import no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.domain.Kvittering;
import no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.domain.Søknad;
import no.nav.security.token.support.core.api.ProtectedWithClaims;

@RestController
@ProtectedWithClaims(issuer = ISSUER, claimMap = { "acr=Level4" })
@RequestMapping(path = REST_SOKNAD, produces = APPLICATION_JSON_VALUE)
public class InnsendingController {

    private static final Logger LOG = LoggerFactory.getLogger(InnsendingController.class);

    public static final String REST_SOKNAD = "/rest/soknad";

    private final Innsending innsending;

    public InnsendingController(Innsending innsending) {
        this.innsending = innsending;
    }

    @PostMapping
    public Kvittering sendInn(@RequestBody Søknad søknad) {
        LOG.info("Mottok søknad med {} vedlegg", søknad.getVedlegg().size());
        LOG.info(CONFIDENTIAL, "{}", søknad);
        return innsending.sendInn(søknad);
    }

    @PostMapping("/ettersend")
    public Kvittering sendInn(@RequestBody Ettersending ettersending) {
        LOG.info("Mottok ettersending av {} vedlegg", ettersending.getVedlegg().size());
        LOG.info(CONFIDENTIAL, "{}", ettersending);
        return innsending.ettersend(ettersending);
    }

    @PostMapping("/endre")
    public Kvittering endre(@RequestBody Søknad søknad) {
        LOG.info("Mottok endringssøknad med {} vedlegg", søknad.getVedlegg().size());
        LOG.info(CONFIDENTIAL, "{}", søknad);
        return innsending.endre(søknad);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[innsending=" + innsending + "]";
    }

}