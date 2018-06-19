package no.nav.foreldrepenger.selvbetjening.oppslag.tjeneste;

import no.nav.foreldrepenger.selvbetjening.oppslag.tjeneste.json.SøkerinfoDto;
import no.nav.foreldrepenger.selvbetjening.oppslag.tjeneste.json.PersonDto;

public interface Oppslag {

    PersonDto hentPerson();
    SøkerinfoDto hentSøkerinfo();

}
