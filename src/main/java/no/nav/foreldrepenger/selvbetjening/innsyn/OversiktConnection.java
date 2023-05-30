package no.nav.foreldrepenger.selvbetjening.innsyn;

import java.net.URI;
import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestOperations;

import no.nav.foreldrepenger.common.innsyn.AnnenPartVedtak;
import no.nav.foreldrepenger.common.innsyn.Saker;
import no.nav.foreldrepenger.selvbetjening.http.AbstractRestConnection;

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
    public Optional<AnnenPartVedtak> hentAnnenpartsVedtak() {
        return Optional.ofNullable(getForObject(cfg.annenpartsVedtak(), AnnenPartVedtak.class));
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
