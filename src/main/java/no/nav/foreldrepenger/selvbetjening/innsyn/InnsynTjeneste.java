package no.nav.foreldrepenger.selvbetjening.innsyn;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import no.nav.foreldrepenger.common.innsyn.AnnenPartVedtak;
import no.nav.foreldrepenger.common.innsyn.Saker;

@Service
public class InnsynTjeneste implements Innsyn {
    private final OversiktConnection fpoversiktConnection;

    private static final Logger LOG = LoggerFactory.getLogger(InnsynTjeneste.class);

    public InnsynTjeneste(OversiktConnection fpoversiktConnection) {
        this.fpoversiktConnection = fpoversiktConnection;
    }

    @Override
    public Saker hentSaker() {
        LOG.info("Henter saker for pålogget bruker");
        return fpoversiktConnection.hentSaker();
    }

    @Override
    public Optional<AnnenPartVedtak> annenPartVedtak(AnnenPartVedtakIdentifikator annenPartVedtakIdentifikator) {
        return fpoversiktConnection.hentAnnenpartsVedtak(annenPartVedtakIdentifikator);
    }

    @Override
    public String ping() {
        return fpoversiktConnection.ping();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [innsynConnection=" + fpoversiktConnection + "]";
    }
}
