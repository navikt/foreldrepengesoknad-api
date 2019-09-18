package no.nav.foreldrepenger.selvbetjening.tjeneste.historikk;

import java.net.URI;
import java.util.List;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import no.nav.foreldrepenger.selvbetjening.tjeneste.oppslag.domain.Fødselsnummer;

@Service
@ConditionalOnProperty(name = "stub.historikk", havingValue = "false", matchIfMissing = true)
public class HistorikkTjeneste implements Historikk {
    private final HistorikkConnection connection;

    public HistorikkTjeneste(HistorikkConnection connection) {
        this.connection = connection;
    }

    @Override
    public List<SøknadInnslag> hentHistorikk() {
        return connection.hentHistorikk();
    }

    @Override
    public List<SøknadInnslag> hentHistorikkFor(Fødselsnummer fnr) {
        return connection.hentHistorikk(fnr);
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
