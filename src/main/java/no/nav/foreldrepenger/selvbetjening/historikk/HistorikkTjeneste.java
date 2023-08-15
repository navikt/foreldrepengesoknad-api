package no.nav.foreldrepenger.selvbetjening.historikk;

import no.nav.foreldrepenger.common.domain.Saksnummer;
import no.nav.foreldrepenger.selvbetjening.http.Pingable;
import no.nav.foreldrepenger.selvbetjening.http.RetryAware;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HistorikkTjeneste implements Pingable, RetryAware {
    private final HistorikkConnection connection;

    public HistorikkTjeneste(HistorikkConnection connection) {
        this.connection = connection;
    }

    public List<HistorikkInnslag> historikk() {
        return connection.hentHistorikk();
    }

    public List<String> manglendeVedlegg(Saksnummer saksnr) {
        return connection.manglendeVedlegg(saksnr);
    }

    @Override
    public String ping() {
        return connection.ping();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [connection=" + connection + "]";
    }

}
