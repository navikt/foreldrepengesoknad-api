package no.nav.foreldrepenger.selvbetjening.innsyn;

import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestOperations;

import no.nav.foreldrepenger.common.innsyn.AnnenPartVedtak;
import no.nav.foreldrepenger.common.innsyn.Saker;
import no.nav.foreldrepenger.selvbetjening.http.AbstractRestConnection;

@Component
public class InnsynConnection extends AbstractRestConnection {

    private final InnsynConfig cfg;

    public InnsynConnection(RestOperations operations, InnsynConfig cfg) {
        super(operations);
        this.cfg = cfg;
    }

    public Saker hentSaker() {
        return getForObject(cfg.saker(), Saker.class);
    }
    public Optional<AnnenPartVedtak> hentAnnenpartsVedtak(AnnenPartVedtakIdentifikator annenPartVedtakIdentifikator) {
        return Optional.ofNullable(postForObject(cfg.annenpartsVedtak(), annenPartVedtakIdentifikator, AnnenPartVedtak.class));
    }
}
