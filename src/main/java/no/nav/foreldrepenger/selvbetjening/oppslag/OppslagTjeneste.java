package no.nav.foreldrepenger.selvbetjening.oppslag;

import static no.nav.foreldrepenger.selvbetjening.util.MDCUtil.CONFIDENTIAL;
import static no.nav.foreldrepenger.selvbetjening.util.StringUtil.maskFnr;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import no.nav.foreldrepenger.selvbetjening.innsyn.InnsynConnection;
import no.nav.foreldrepenger.selvbetjening.oppslag.domain.AktørId;
import no.nav.foreldrepenger.selvbetjening.oppslag.domain.Person;
import no.nav.foreldrepenger.selvbetjening.oppslag.domain.Søkerinfo;

@Service
@ConditionalOnProperty(name = "stub.oppslag", havingValue = "false", matchIfMissing = true)
public class OppslagTjeneste implements Oppslag {

    private static final Logger LOG = LoggerFactory.getLogger(OppslagTjeneste.class);
    private final OppslagConnection oppslag;
    private final InnsynConnection innsyn;

    @Inject
    public OppslagTjeneste(OppslagConnection oppslag, InnsynConnection innsyn) {
        this.oppslag = oppslag;
        this.innsyn = innsyn;
    }

    @Override
    public Person hentPerson() {
        try {
            LOG.info("Henter PDL-person");
            var pdl = oppslag.hentPDLPerson();
            LOG.info("PDL-person {}", pdl);
        } catch (Exception e) {
            LOG.warn("Feil ved oppslag av PDL-person");
        }
        return new Person(oppslag.hentPerson());
    }

    @Override
    public Søkerinfo hentSøkerinfo() {
        LOG.info("Henter søkerinfo");
        var info = new Søkerinfo(hentPerson(), innsyn.hentArbeidsForhold());
        LOG.info("Hentet søkerinfo for {} med {} arbeidsforhold OK", maskFnr(info.getSøker().fnr), info.getArbeidsforhold());
        LOG.info(CONFIDENTIAL, "Hentet søkerinfo {}", info);
        return info;
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
