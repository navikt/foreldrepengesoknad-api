package no.nav.foreldrepenger.selvbetjening.tjeneste.innsyn;

import static no.nav.foreldrepenger.selvbetjening.util.URIUtils.queryParams;
import static no.nav.foreldrepenger.selvbetjening.util.URIUtils.uri;

import java.net.URI;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "innsyn", ignoreUnknownFields = false)
@Component
public class InnsynConfig {

    private static final String PING = "mottak/ping";
    private static final String FPSAK_SAKER = "innsyn/saker";
    private static final String SAK_SAKER = "sak";
    private static final String SAKSNUMMER = "saksnummer";
    private static final String ANNENPART = "annenPart";
    private static final String UTTAKSPLAN = "innsyn/uttaksplan";
    private static final String UTTAKSPLANANNEN = "innsyn/uttaksplanannen";
    private static final String VEDTAK = "innsyn/vedtak";

    boolean enabled;
    URI mottakURI;
    URI oppslagURI;

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

    URI getUttakURIForAnnenPart(String annenPart) {
        return uri(getMottakURI(), UTTAKSPLANANNEN, queryParams(ANNENPART, annenPart));
    }

    public URI getVedtakURI(String saksnummer) {
        return uri(getMottakURI(), VEDTAK, queryParams(SAKSNUMMER, saksnummer));
    }

}
