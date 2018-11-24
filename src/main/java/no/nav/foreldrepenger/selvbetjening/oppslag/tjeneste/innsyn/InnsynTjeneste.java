package no.nav.foreldrepenger.selvbetjening.oppslag.tjeneste.innsyn;

import java.util.List;

import org.springframework.stereotype.Service;

import no.nav.foreldrepenger.selvbetjening.oppslag.json.Sak;

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
}
