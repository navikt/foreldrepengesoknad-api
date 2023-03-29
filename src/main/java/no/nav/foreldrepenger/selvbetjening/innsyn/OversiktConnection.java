package no.nav.foreldrepenger.selvbetjening.innsyn;

import no.nav.foreldrepenger.common.innsyn.Saker;
import no.nav.foreldrepenger.selvbetjening.http.AbstractRestConnection;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestOperations;

import java.net.URI;

@Component
public class OversiktConnection extends AbstractRestConnection {

    private final OversiktConfig cfg;

    public OversiktConnection(RestOperations operations, OversiktConfig cfg) {
        super(operations);
        this.cfg = cfg;
    }

    public Saker hentSaker() {
        return getForObject(cfg.saker(), Saker.class);
    }

    @Override
    public URI pingURI() {
        return cfg.pingURI();
    }

    @Override
    public boolean isEnabled() {
        return cfg.isEnabled();
    }
}
