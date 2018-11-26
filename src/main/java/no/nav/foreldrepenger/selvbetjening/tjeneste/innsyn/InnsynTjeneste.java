package no.nav.foreldrepenger.selvbetjening.tjeneste.innsyn;

import java.net.URI;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class InnsynTjeneste implements Innsyn {

    private final InnsynConnection innsynConnection;

    public InnsynTjeneste(InnsynConnection innsynConnection) {
        this.innsynConnection = innsynConnection;
    }

    @Override
    public List<UttaksPeriode> hentUttaksplan(String saksnummer) {
        return innsynConnection.hentUttaksplan(saksnummer);
    }

    @Override
    public List<Sak> hentSaker() {
        return innsynConnection.hentSaker();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [innsynConnection=" + innsynConnection + "]";
    }

    @Override
    public String ping() {
        return innsynConnection.ping();
    }

    @Override
    public URI pingURI() {
        return innsynConnection.pingURI();
    }
}
