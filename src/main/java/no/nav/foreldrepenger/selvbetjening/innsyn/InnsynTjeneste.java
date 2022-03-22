package no.nav.foreldrepenger.selvbetjening.innsyn;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import no.nav.foreldrepenger.common.domain.Fødselsnummer;
import no.nav.foreldrepenger.selvbetjening.innsyn.saker.Sak;
import no.nav.foreldrepenger.selvbetjening.innsyn.sakerv2.Saker;
import no.nav.foreldrepenger.selvbetjening.innsyn.uttaksplan.Uttaksplan;

@Service
@ConditionalOnProperty(name = "stub.oppslag", havingValue = "false", matchIfMissing = true)
public class InnsynTjeneste implements Innsyn {

    private final InnsynConnection connection;

    private static final Logger LOG = LoggerFactory.getLogger(InnsynTjeneste.class);

    public InnsynTjeneste(InnsynConnection innsynConnection) {
        this.connection = innsynConnection;
    }

    @Override
    public Uttaksplan hentUttaksplan(String saksnummer) {
        LOG.info("Henter uttaksplan for sak {}", saksnummer);
        return connection.hentUttaksplan(saksnummer);
    }

    @Override
    public Uttaksplan hentUttaksplanAnnenPart(Fødselsnummer annenPart) {
        LOG.info("Henter uttaksplan for annen part {}", annenPart);
        return connection.hentUttaksplanAnnenPart(annenPart);
    }

    @Override
    public List<Sak> hentSaker() {
        LOG.info("Henter saker for pålogget bruker");
        return connection.hentSaker();
    }

    @Override
    public Saker hentSakerV2() {
        LOG.info("Henter sakerV2 for pålogget bruker");
        return connection.hentSakerV2();
    }

    @Override
    public String ping() {
        return connection.ping();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [innsynConnection=" + connection + "]";
    }

}
