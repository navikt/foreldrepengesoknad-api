package no.nav.foreldrepenger.selvbetjening.tjeneste.minidialog;

import java.net.URI;
import java.util.List;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(name = "stub.minidialog", havingValue = "false", matchIfMissing = true)
public class MinidialogTjeneste implements Minidialog {
    private final MinidialogConnection connection;

    public MinidialogTjeneste(MinidialogConnection connection) {
        this.connection = connection;
    }

    @Override
    public List<MinidialogInnslag> hentMinidialoger() {
        return connection.hentMinidialoger();
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
