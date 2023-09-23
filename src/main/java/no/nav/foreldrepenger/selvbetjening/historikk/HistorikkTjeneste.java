package no.nav.foreldrepenger.selvbetjening.historikk;

import java.util.List;

import org.springframework.stereotype.Service;

import no.nav.foreldrepenger.common.domain.Saksnummer;
import no.nav.foreldrepenger.selvbetjening.http.RetryAware;

@Service
@Deprecated
public class HistorikkTjeneste implements RetryAware {
    private final HistorikkConnection connection;

    public HistorikkTjeneste(HistorikkConnection connection) {
        this.connection = connection;
    }

    public List<String> manglendeVedlegg(Saksnummer saksnr) {
        return connection.manglendeVedlegg(saksnr);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [connection=" + connection + "]";
    }

}
