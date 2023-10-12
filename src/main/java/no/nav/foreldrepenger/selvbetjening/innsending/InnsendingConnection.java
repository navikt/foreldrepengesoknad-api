package no.nav.foreldrepenger.selvbetjening.innsending;

import static no.nav.foreldrepenger.selvbetjening.innsending.mapper.EttersendingMapper.tilEttersending;
import static no.nav.foreldrepenger.selvbetjening.innsending.mapper.SøknadMapper.tilEndringssøknad;
import static no.nav.foreldrepenger.selvbetjening.innsending.mapper.SøknadMapper.tilSøknad;
import static no.nav.foreldrepenger.selvbetjening.util.StringUtils.escapeHtml;

import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestOperations;

import no.nav.foreldrepenger.common.domain.Kvittering;
import no.nav.foreldrepenger.common.domain.Søknad;
import no.nav.foreldrepenger.common.domain.felles.Ettersending;
import no.nav.foreldrepenger.selvbetjening.http.AbstractRestConnection;
import no.nav.foreldrepenger.selvbetjening.innsending.dto.endringssøknad.EndringssøknadDto;
import no.nav.foreldrepenger.selvbetjening.innsending.dto.SøknadDto;
import no.nav.foreldrepenger.selvbetjening.innsending.dto.ettersendelse.EttersendelseDto;

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

    public Kvittering sendInn(SøknadDto søknad) {
        return post(config.innsendingURI(), body(søknad));
    }

    public Kvittering ettersend(EttersendelseDto ettersending) {
        return post(config.ettersendingURI(), body(ettersending));
    }

    public Kvittering endre(EndringssøknadDto endringssøknad) {
        return post(config.endringURI(), body(endringssøknad));
    }

    private Kvittering post(URI uri, Object body) {
        return postForObject(uri, body, Kvittering.class);
    }

    public Søknad body(SøknadDto søknad) {
        SECURE_LOGGER.info("{} mottatt fra frontend med følende innhold: {}", søknad.type(), escapeHtml(søknad));
        vedleggshåndtering.fjernDupliserteVedleggFraSøknad(søknad);
        return tilSøknad(søknad);
    }

    public Søknad body(EndringssøknadDto endringssøknad) {
        SECURE_LOGGER.info("{} mottatt fra frontend med følende innhold: {}", endringssøknad.type(), escapeHtml(endringssøknad));
        vedleggshåndtering.fjernDupliserteVedleggFraSøknad(endringssøknad);
        return tilEndringssøknad(endringssøknad);
    }

    public Ettersending body(EttersendelseDto ettersending) {
        vedleggshåndtering.fjernDupliserteVedleggFraEttersending(ettersending);
        return tilEttersending(ettersending);
    }


    @Override
    public String toString() {
        return getClass().getSimpleName() + " [config=" + config + "]";
    }

}
