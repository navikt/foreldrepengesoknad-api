package no.nav.foreldrepenger.selvbetjening.tjeneste.innsyn;

import java.net.URI;
import java.util.List;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import no.nav.foreldrepenger.selvbetjening.tjeneste.innsyn.saker.Sak;
import no.nav.foreldrepenger.selvbetjening.tjeneste.innsyn.uttaksplan.Uttaksplan;
import no.nav.foreldrepenger.selvbetjening.tjeneste.innsyn.vedtak.Vedtak;
import no.nav.foreldrepenger.selvbetjening.tjeneste.oppslag.domain.AktørId;

@Service
@ConditionalOnProperty(name = "stub.oppslag", havingValue = "false", matchIfMissing = true)
public class InnsynTjeneste implements Innsyn {

    private final InnsynConnection innsynConnection;

    public InnsynTjeneste(InnsynConnection innsynConnection) {
        this.innsynConnection = innsynConnection;
    }

    @Override
    public Uttaksplan hentUttaksplan(String saksnummer) {
        return innsynConnection.hentUttaksplan(saksnummer);
    }

    @Override
    public Uttaksplan hentUttaksplan(AktørId annenPart) {
        return innsynConnection.hentUttaksplan(annenPart);
    }

    @Override
    public Vedtak hentVedtak(String saksnummer) {
        return innsynConnection.hentVedtak(saksnummer);
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
