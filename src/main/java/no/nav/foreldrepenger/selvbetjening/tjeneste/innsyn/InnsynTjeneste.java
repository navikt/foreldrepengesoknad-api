package no.nav.foreldrepenger.selvbetjening.tjeneste.innsyn;

import no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.domain.UttaksplanPeriode;
import no.nav.foreldrepenger.selvbetjening.tjeneste.innsyn.saker.Sak;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@ConditionalOnProperty(name = "stub.oppslag", havingValue = "false", matchIfMissing = true)
public class InnsynTjeneste implements Innsyn {

    private final InnsynConnection innsynConnection;

    public InnsynTjeneste(InnsynConnection innsynConnection) {
        this.innsynConnection = innsynConnection;
    }

    @Override
    public List<UttaksplanPeriode> hentUttaksplan(String saksnummer) {
        return innsynConnection.hentUttaksplan(saksnummer).stream().map(UttaksplanPeriode::new).collect(toList());
    }

    @Override
    public List<Sak> hentSaker() {
        return innsynConnection.hentSaker();
    }

    @Override
    public String ping() {
        return innsynConnection.ping();
    }

    @Override
    public URI pingURI() {
        return innsynConnection.pingURI();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [innsynConnection=" + innsynConnection + "]";
    }
}
