package no.nav.foreldrepenger.selvbetjening.tjeneste.oppslag;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import no.nav.foreldrepenger.selvbetjening.tjeneste.innsyn.InnsynConnection;
import no.nav.foreldrepenger.selvbetjening.tjeneste.oppslag.domain.AktørId;
import no.nav.foreldrepenger.selvbetjening.tjeneste.oppslag.domain.Person;
import no.nav.foreldrepenger.selvbetjening.tjeneste.oppslag.domain.Søkerinfo;

@Service
@ConditionalOnProperty(name = "stub.oppslag", havingValue = "false", matchIfMissing = true)
public class OppslagTjeneste implements Oppslag {

    private static final Logger LOG = org.slf4j.LoggerFactory.getLogger(OppslagTjeneste.class);
    private final OppslagConnection oppslag;
    private final InnsynConnection innsyn;

    @Inject
    public OppslagTjeneste(OppslagConnection oppslag, InnsynConnection innsyn) {
        this.oppslag = oppslag;
        this.innsyn = innsyn;
    }

    @Override
    public Person hentPerson() {
        return new Person(oppslag.hentPerson());
    }

    @Override
    public Søkerinfo hentSøkerinfo() {
        try {
            LOG.info("Henter søkerinfo");
            var info = new Søkerinfo(hentPerson(), innsyn.hentArbeidsForhold());
            LOG.info("Hentet søkerinfo OK {}", info);
            return info;
        } catch (Exception e) {
            LOG.info("Henter søkerinfo alternativt");
            var info = new Søkerinfo(oppslag.hentSøkerInfo());
            LOG.info("Hentet søkerinfo alternativt OK {}", info);
            return info;
        }
    }

    @Override
    public AktørId hentAktørId(String fnr) {
        return oppslag.HentAktørId(fnr);
    }

    @Override
    public String ping() {
        return oppslag.ping();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [oppslag=" + oppslag + "]";
    }

}
