package no.nav.foreldrepenger.selvbetjening.innsyn;

import java.net.URI;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.boot.context.properties.bind.DefaultValue;

import no.nav.foreldrepenger.selvbetjening.http.AbstractConfig;

@ConfigurationProperties(prefix = "innsyn", ignoreInvalidFields = false)
public class InnsynConfig extends AbstractConfig {

    private static final String PING = "mottak/ping";
    private static final String FPSAK_SAKER = "innsyn/saker";
    private static final String ARBEIDSFORHOLD = "innsyn/arbeidsforhold";
    private static final String SAK_SAKER = "sak";
    private static final String SAKSNUMMER = "saksnummer";
    private static final String ANNENPART = "annenPart";
    private static final String UTTAKSPLAN = "innsyn/uttaksplan";
    private static final String UTTAKSPLANANNEN = "innsyn/uttaksplanannen";
    private static final String VEDTAK = "innsyn/vedtak";

    private final URI oppslag;

    @ConstructorBinding
    public InnsynConfig(URI mottak, URI oppslag, @DefaultValue("true") boolean enabled) {
        super(mottak, enabled);
        this.oppslag = oppslag;
    }

    public URI getOppslag() {
        return oppslag;
    }

    public URI getMottak() {
        return getUri();
    }

    @Override
    public URI pingURI() {
        return uri(getMottak(), PING);
    }

    URI fpsakURI() {
        return uri(getMottak(), FPSAK_SAKER);
    }

    URI sakURI() {
        return uri(getOppslag(), SAK_SAKER);
    }

    URI sakURIViaMottak() {
        return uri(getMottak(), SAK_SAKER);
    }

    URI uttakURI(String saksnummer) {
        return uri(getMottak(), UTTAKSPLAN, queryParams(SAKSNUMMER, saksnummer));
    }

    URI uttakURIForAnnenPart(String annenPart) {
        return uri(getMottak(), UTTAKSPLANANNEN, queryParams(ANNENPART, annenPart));
    }

    public URI vedtakURI(String saksnummer) {
        return uri(getMottak(), VEDTAK, queryParams(SAKSNUMMER, saksnummer));
    }

    public URI arbeidsforholdURI() {
        return uri(getMottak(), ARBEIDSFORHOLD);
    }
}
