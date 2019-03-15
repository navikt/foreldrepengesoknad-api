package no.nav.foreldrepenger.selvbetjening.tjeneste.innsyn;

import static no.nav.foreldrepenger.selvbetjening.util.URIUtils.queryParams;
import static no.nav.foreldrepenger.selvbetjening.util.URIUtils.uri;

import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "innsyn")
@Configuration
public class InnsynConfig {

    private static final String PING = "mottak/ping";
    private static final String FPSAK_SAKER = "innsyn/saker";
    private static final String SAK_SAKER = "sak";
    private static final String SAKSNUMMER = "saksnummer";
    private static final String UTTAKSPLAN = "innsyn/uttaksplan";
    private static final String VEDTAK = "innsyn/vedtak";

    boolean enabled;
    URI mottakURI;
    URI oppslagURI;

    public InnsynConfig(@Value("${FPSOKNAD_MOTTAK_API_URL}") URI mottakURI,
            @Value("${FPSOKNAD_OPPSLAG_API_URL}") URI oppslagURI) {
        this.mottakURI = mottakURI;
        this.oppslagURI = oppslagURI;
    }

    public URI getOppslagURI() {
        return oppslagURI;
    }

    public void setOppslagURI(URI oppslagURI) {
        this.oppslagURI = oppslagURI;
    }

    private URI getMottakURI() {
        return mottakURI;
    }

    public void setMottakURI(URI mottakURI) {
        this.mottakURI = mottakURI;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    URI getPingURI() {
        return uri(getMottakURI(), PING);
    }

    URI getFpsakURI() {
        return uri(getMottakURI(), FPSAK_SAKER);
    }

    URI getSakURI() {
        return uri(getOppslagURI(), SAK_SAKER);
    }

    URI getUttakURI(String saksnummer) {
        return uri(getMottakURI(), UTTAKSPLAN, queryParams(SAKSNUMMER, saksnummer));
    }

    public URI getVedtakURI(String saksnummer) {
        return uri(getMottakURI(), VEDTAK, queryParams(SAKSNUMMER, saksnummer));
    }

}
