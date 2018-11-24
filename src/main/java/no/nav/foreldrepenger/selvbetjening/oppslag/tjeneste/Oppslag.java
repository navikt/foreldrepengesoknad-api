package no.nav.foreldrepenger.selvbetjening.oppslag.tjeneste;

import no.nav.foreldrepenger.selvbetjening.oppslag.json.AktørId;
import no.nav.foreldrepenger.selvbetjening.oppslag.tjeneste.json.PersonDto;
import no.nav.foreldrepenger.selvbetjening.oppslag.tjeneste.json.SøkerinfoDto;

public interface Oppslag {

    PersonDto hentPerson();

    SøkerinfoDto hentSøkerinfo();

    AktørId hentAktørId(String fnr);

}
