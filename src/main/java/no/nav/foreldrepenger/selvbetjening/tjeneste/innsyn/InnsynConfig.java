package no.nav.foreldrepenger.selvbetjening.tjeneste.innsyn;

import java.net.URI;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.boot.context.properties.bind.DefaultValue;

import no.nav.foreldrepenger.selvbetjening.tjeneste.AbstractConfig;

@ConfigurationProperties("innsyn")
@ConstructorBinding
public class InnsynConfig extends AbstractConfig {

    private static final String PING = "mottak/ping";
    private static final String FPSAK_SAKER = "innsyn/saker";
    private static final String SAK_SAKER = "sak";
    private static final String SAKSNUMMER = "saksnummer";
    private static final String ANNENPART = "annenPart";
    private static final String UTTAKSPLAN = "innsyn/uttaksplan";
    private static final String UTTAKSPLANANNEN = "innsyn/uttaksplanannen";
    private static final String VEDTAK = "innsyn/vedtak";

    private final boolean enabled;
    private final URI mottak;
    private final URI oppslag;

    public InnsynConfig(URI mottak, URI oppslag, @DefaultValue("true") boolean enabled) {
        this.mottak = mottak;
        this.oppslag = oppslag;
        this.enabled = enabled;
    }

    public URI getOppslagURI() {
        return oppslag;
    }

    private URI getMottakURI() {
        return mottak;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public URI pingURI() {
        return uri(getMottakURI(), PING);
    }

    URI fpsakURI() {
        return uri(getMottakURI(), FPSAK_SAKER);
    }

    URI sakURI() {
        return uri(getOppslagURI(), SAK_SAKER);
    }

    URI uttakURI(String saksnummer) {
        return uri(getMottakURI(), UTTAKSPLAN, queryParams(SAKSNUMMER, saksnummer));
    }

    URI uttakURIForAnnenPart(String annenPart) {
        return uri(getMottakURI(), UTTAKSPLANANNEN, queryParams(ANNENPART, annenPart));
    }

    public URI vedtakURI(String saksnummer) {
        return uri(getMottakURI(), VEDTAK, queryParams(SAKSNUMMER, saksnummer));
    }

}
