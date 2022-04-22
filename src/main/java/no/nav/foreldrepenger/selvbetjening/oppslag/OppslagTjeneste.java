package no.nav.foreldrepenger.selvbetjening.oppslag;

import static no.nav.foreldrepenger.boot.conditionals.EnvUtil.CONFIDENTIAL;
import static no.nav.foreldrepenger.common.util.StringUtil.partialMask;
import static no.nav.foreldrepenger.selvbetjening.oppslag.mapper.PersonMapper.tilPersonFrontend;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import no.nav.foreldrepenger.selvbetjening.innsyn.InnsynConnection;
import no.nav.foreldrepenger.selvbetjening.oppslag.domain.PersonFrontend;
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
    public PersonFrontend hentPerson() {
        return tilPersonFrontend(oppslag.hentPerson());
    }

    @Override
    public Søkerinfo hentSøkerinfo() {
        return søkerinfo();
    }

    private Søkerinfo søkerinfo() {
        LOG.info("Henter søkerinfo");
        var info = new Søkerinfo(tilPersonFrontend(oppslag.hentPerson()), innsyn.hentArbeidsForhold());
        LOG.info("Hentet søkerinfo for {} med {} arbeidsforhold OK", partialMask(info.søker().fnr().value()), info.arbeidsforhold().size());
        LOG.trace(CONFIDENTIAL, "Hentet søkerinfo {}", info);
        return info;
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
