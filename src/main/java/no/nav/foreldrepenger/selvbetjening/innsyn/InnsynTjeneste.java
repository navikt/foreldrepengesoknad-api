package no.nav.foreldrepenger.selvbetjening.innsyn;

import no.nav.foreldrepenger.common.innsyn.AnnenPartVedtak;
import no.nav.foreldrepenger.common.innsyn.Saker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class InnsynTjeneste implements Innsyn {

    private final InnsynConnection connection;

    private static final Logger LOG = LoggerFactory.getLogger(InnsynTjeneste.class);

    public InnsynTjeneste(InnsynConnection innsynConnection) {
        this.connection = innsynConnection;
    }

    @Override
    public Saker hentSaker() {
        LOG.info("Henter saker for pålogget bruker");
        return connection.hentSaker();
    }

    @Override
    public Optional<AnnenPartVedtak> annenPartVedtak(AnnenPartVedtakIdentifikator annenPartVedtakIdentifikator) {
        return connection.annenPartVedtak(annenPartVedtakIdentifikator);
    }

    @Override
    public String ping() {
        return connection.ping();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [innsynConnection=" + connection + "]";
    }

}
