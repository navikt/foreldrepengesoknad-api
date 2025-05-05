package no.nav.foreldrepenger.selvbetjening.oppslag;

import no.nav.foreldrepenger.selvbetjening.oppslag.dto.PersonFrontend;
import no.nav.foreldrepenger.selvbetjening.oppslag.dto.Søkerinfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static no.nav.foreldrepenger.selvbetjening.oppslag.mapper.PersonMapper.tilPersonFrontend;

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
        var person = oppslag.hentPerson();
        return tilPersonFrontend(person);
    }

    @Override
    public Søkerinfo hentSøkerinfo() {
        var personMedArbeidsforhold = oppslag.søkerinfo();
        var info = new Søkerinfo(tilPersonFrontend(personMedArbeidsforhold.person()), personMedArbeidsforhold.arbeidsforhold());
        LOG.info("Hentet søkerinfo for med {} arbeidsforhold OK", info.arbeidsforhold().size());
        return info;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [oppslag=" + oppslag + "]";
    }

}
