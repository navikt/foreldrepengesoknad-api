package no.nav.foreldrepenger.selvbetjening.innsending;

import static no.nav.foreldrepenger.selvbetjening.innsending.mapper.EttersendingMapper.tilEttersending;
import static no.nav.foreldrepenger.selvbetjening.innsending.mapper.SøknadMapper.tilSøknad;
import static no.nav.foreldrepenger.selvbetjening.util.StringUtils.escapeHtml;

import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestOperations;

import no.nav.foreldrepenger.common.domain.Kvittering;
import no.nav.foreldrepenger.common.domain.Søknad;
import no.nav.foreldrepenger.selvbetjening.http.AbstractRestConnection;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.EttersendingFrontend;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.SøknadFrontend;

@Component
public class InnsendingConnection extends AbstractRestConnection {
    private static final Logger SECURE_LOGGER = LoggerFactory.getLogger("secureLogger");

    private final InnsendingConfig config;
    private final VedleggsHåndteringTjeneste vedleggshåndtering;

    public InnsendingConnection(RestOperations operations, InnsendingConfig config, VedleggsHåndteringTjeneste vedleggshåndtering) {
        super(operations);
        this.config = config;
        this.vedleggshåndtering = vedleggshåndtering;
    }

    public Kvittering sendInn(SøknadFrontend søknad) {
        return post(config.innsendingURI(), body(søknad));
    }

    public Kvittering ettersend(EttersendingFrontend ettersending) {
        return post(config.ettersendingURI(), body(ettersending));
    }

    public Kvittering endre(SøknadFrontend søknad) {
        return post(config.endringURI(), body(søknad));
    }

    private Kvittering post(URI uri, Object body) {
        return postForObject(uri, body, Kvittering.class);
    }

    public Søknad body(SøknadFrontend søknadFrontend) {
        SECURE_LOGGER.info("{} mottatt fra frontend med følende innhold: {}", søknadFrontend.getType(), escapeHtml(søknadFrontend));
        søknadFrontend = vedleggshåndtering.fjernDupliserteVedlegg(søknadFrontend);
        return tilSøknad(søknadFrontend);
    }

    public no.nav.foreldrepenger.common.domain.felles.Ettersending body(EttersendingFrontend ettersending) {
        ettersending = vedleggshåndtering.fjernDupliserteVedlegg(ettersending);
        return tilEttersending(ettersending);
    }


    @Override
    public String toString() {
        return getClass().getSimpleName() + " [config=" + config + "]";
    }

}
