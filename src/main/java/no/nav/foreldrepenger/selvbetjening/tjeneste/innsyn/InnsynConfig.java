package no.nav.foreldrepenger.selvbetjening.tjeneste.innsyn;

import static no.nav.foreldrepenger.selvbetjening.tjeneste.UriUtil.queryParams;
import static no.nav.foreldrepenger.selvbetjening.tjeneste.UriUtil.uri;

import java.net.URI;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

//@ConstructorBinding
@ConfigurationProperties(prefix = "innsyn", ignoreInvalidFields = false)
@Component
public class InnsynConfig /* extends AbstractConfig */ {

    public URI getOppslag() {
        return oppslag;
    }

    public void setOppslag(URI oppslag) {
        this.oppslag = oppslag;
    }

    public URI getMottak() {
        return mottak;
    }

    public void setMottak(URI mottak) {
        this.mottak = mottak;
    }

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

    public boolean isEnabled() {
        return enabled;
    }

    public URI pingURI() {
        return uri(mottak, PING);
    }

    URI fpsakURI() {
        return uri(mottak, FPSAK_SAKER);
    }

    URI sakURI() {
        return uri(oppslag, SAK_SAKER);
    }

    URI uttakURI(String saksnummer) {
        return uri(oppslag, UTTAKSPLAN, queryParams(SAKSNUMMER, saksnummer));
    }

    URI uttakURIForAnnenPart(String annenPart) {
        return uri(mottak, UTTAKSPLANANNEN, queryParams(ANNENPART, annenPart));
    }

    public URI vedtakURI(String saksnummer) {
        return uri(mottak, VEDTAK, queryParams(SAKSNUMMER, saksnummer));
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[key=" + key + ", oppslag=" + oppslag + ", mottak=" + mottak
                + ", enabled=" + enabled + "]";
    }

}
