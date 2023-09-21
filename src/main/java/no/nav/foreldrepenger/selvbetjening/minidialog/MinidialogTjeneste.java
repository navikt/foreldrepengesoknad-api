package no.nav.foreldrepenger.selvbetjening.minidialog;

import java.util.List;

import org.springframework.stereotype.Service;

import no.nav.foreldrepenger.selvbetjening.historikk.MinidialogInnslag;
import no.nav.foreldrepenger.selvbetjening.http.RetryAware;

@Service
public class MinidialogTjeneste implements RetryAware {
    private final MinidialogConnection connection;

    public MinidialogTjeneste(MinidialogConnection connection) {
        this.connection = connection;
    }

    public List<MinidialogInnslag> aktive() {
        return connection.hentAktiveSpørsmål();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [connection=" + connection + "]";
    }

}
