package no.nav.foreldrepenger.selvbetjening.minidialog;

import no.nav.foreldrepenger.selvbetjening.historikk.MinidialogInnslag;
import no.nav.foreldrepenger.selvbetjening.http.Pingable;
import no.nav.foreldrepenger.selvbetjening.http.RetryAware;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MinidialogTjeneste implements Pingable, RetryAware {
    private final MinidialogConnection connection;

    public MinidialogTjeneste(MinidialogConnection connection) {
        this.connection = connection;
    }

    public List<MinidialogInnslag> aktive() {
        return connection.hentAktiveSpørsmål();
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
