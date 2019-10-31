package no.nav.foreldrepenger.selvbetjening.tjeneste.innsyn;

import static no.nav.foreldrepenger.selvbetjening.tjeneste.UriUtil.queryParams;
import static no.nav.foreldrepenger.selvbetjening.tjeneste.UriUtil.uri;

import java.net.URI;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

//@ConstructorBinding
@ConfigurationProperties("innsyn")
@Component
public class InnsynConfig /* extends AbstractConfig */ {

    private static final String PING = "mottak/ping";
    private static final String FPSAK_SAKER = "innsyn/saker";
    private static final String SAK_SAKER = "sak";
    private static final String SAKSNUMMER = "saksnummer";
    private static final String ANNENPART = "annenPart";
    private static final String UTTAKSPLAN = "innsyn/uttaksplan";
    private static final String UTTAKSPLANANNEN = "innsyn/uttaksplanannen";
    private static final String VEDTAK = "innsyn/vedtak";

    private /* final */ String key;
    private /* final */ URI oppslag;
    private /* final */ URI mottak;

    private /* final */ boolean enabled;

    /*
     * public InnsynConfig(URI mottak, URI oppslag, @DefaultValue("true") boolean
     * enabled) { super(mottak, enabled); this.oppslag = oppslag; this.enabled =
     * enabled; }
     */

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setEnabled(boolean enabled) {
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
