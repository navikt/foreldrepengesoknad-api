package no.nav.foreldrepenger.selvbetjening.innsyn;

import static no.nav.boot.conditionals.EnvUtil.isDevOrLocal;
import static no.nav.boot.conditionals.EnvUtil.isVTP;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import no.nav.foreldrepenger.common.innsyn.AnnenPartVedtak;
import no.nav.foreldrepenger.common.innsyn.Saker;

@Service
public class InnsynTjeneste implements Innsyn, EnvironmentAware {

    private Environment env;
    private final InnsynConnection connection;
    private final OversiktConnection oversiktConnection;

    private static final Logger LOG = LoggerFactory.getLogger(InnsynTjeneste.class);

    public InnsynTjeneste(InnsynConnection innsynConnection,
                          OversiktConnection oversiktConnection) {
        this.connection = innsynConnection;
        this.oversiktConnection = oversiktConnection;
    }

    @Override
    public Saker hentSaker() {
        try {
            if (isDevOrLocal(env) || isVTP(env)) {
                var saker = oversiktConnection.hentSaker();
                LOG.trace("Mottatt saker fra fpoversikt: {}", saker);
            }
        } catch (Exception e) {
            LOG.warn("Noe gikk galt med henting av saker fra fpoversikt", e);
        }

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

    @Override
    public void setEnvironment(Environment env) {
        this.env = env;
    }
}
