package no.nav.foreldrepenger.selvbetjening.oppslag;

import static no.nav.boot.conditionals.EnvUtil.CONFIDENTIAL;
import static no.nav.foreldrepenger.selvbetjening.oppslag.mapper.PersonMapper.tilPersonFrontend;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.nav.foreldrepenger.selvbetjening.oppslag.domain.PersonFrontend;
import no.nav.foreldrepenger.selvbetjening.oppslag.domain.Søkerinfo;

@Service
public class OppslagTjeneste implements Oppslag {

    private static final Logger LOG = LoggerFactory.getLogger(OppslagTjeneste.class);
    private final OppslagConnection oppslag;

    @Autowired
    public OppslagTjeneste(OppslagConnection oppslag) {
        this.oppslag = oppslag;
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
        var info = new Søkerinfo(tilPersonFrontend(oppslag.hentPerson()), oppslag.hentArbeidsForhold());
        LOG.info("Hentet søkerinfo for med {} arbeidsforhold OK", info.arbeidsforhold().size());
        LOG.trace(CONFIDENTIAL, "Hentet søkerinfo {}", info);
        return info;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [oppslag=" + oppslag + "]";
    }

}
