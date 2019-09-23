package no.nav.foreldrepenger.selvbetjening.tjeneste.minidialog;

import java.net.URI;
import java.util.List;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import no.nav.foreldrepenger.selvbetjening.tjeneste.historikk.MinidialogInnslag;
import no.nav.foreldrepenger.selvbetjening.tjeneste.oppslag.domain.Fødselsnummer;

@Service
@ConditionalOnProperty(name = "stub.minidialog", havingValue = "false", matchIfMissing = true)
public class MinidialogTjeneste implements Minidialog {
    private final MinidialogConnection connection;

    public MinidialogTjeneste(MinidialogConnection connection) {
        this.connection = connection;
    }

    @Override
    public List<MinidialogInnslag> hentMinidialoger(boolean activeOnly) {
        return connection.hentMinidialoger(activeOnly);
    }

    @Override
    public List<MinidialogInnslag> hentMinidialoger(Fødselsnummer fnr, boolean activeOnly) {
        return connection.hentMinidialoger(fnr, activeOnly);
    }

    @Override
    public List<MinidialogInnslag> hentAktiveMinidialogSpørsmål() {
        return connection.hentAktiveSpørsmål();
    }

    @Override
    public List<MinidialogInnslag> hentAktiveMinidialogSpørsmål(Fødselsnummer fnr) {
        return connection.hentAktiveSpørsmål(fnr);

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
