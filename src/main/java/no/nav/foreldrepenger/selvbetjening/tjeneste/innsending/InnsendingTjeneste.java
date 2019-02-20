package no.nav.foreldrepenger.selvbetjening.tjeneste.innsending;

import static org.slf4j.LoggerFactory.getLogger;

import java.net.URI;

import org.slf4j.Logger;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.domain.Ettersending;
import no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.domain.Kvittering;
import no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.domain.Søknad;

@Service
@ConditionalOnProperty(name = "stub.mottak", havingValue = "false", matchIfMissing = true)
public class InnsendingTjeneste implements Innsending {

    private static final Logger LOG = getLogger(InnsendingTjeneste.class);

    private final InnsendingConnection connection;

    public InnsendingTjeneste(InnsendingConnection connection) {
        this.connection = connection;
    }

    @Override
    public Kvittering sendInn(Søknad søknad) {
        LOG.info("Sender inn søknad av type {}", søknad.type);
        return connection.sendInn(søknad);
    }

    @Override
    public Kvittering sendInn(Ettersending ettersending) {
        LOG.info("Sender inn ettersending for foreldrepenger på sak {}", ettersending.saksnummer);
        return connection.ettersend(ettersending);
    }

    @Override
    public Kvittering sendInnForEngangsstonad(Ettersending ettersending) {
        LOG.info("Sender inn ettersending for en engangsstonad på sak {}", ettersending.saksnummer);
        return connection.ettersendForEngangsstonad(ettersending);
    }

    @Override
    public Kvittering endre(Søknad søknad) {
        LOG.info("Sender inn endringssøknad av type {}", søknad.type);
        return connection.endre(søknad);
    }

    @Override
    public String ping() {
        return connection.ping();
    }

    @Override
    public URI pingURI() {
        return connection.pingURI();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [connection=" + connection + "]";
    }
}
