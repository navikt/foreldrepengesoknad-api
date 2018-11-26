package no.nav.foreldrepenger.selvbetjening.tjeneste.oppslag;

import no.nav.foreldrepenger.selvbetjening.tjeneste.oppslag.domain.AktørId;
import no.nav.foreldrepenger.selvbetjening.tjeneste.oppslag.dto.PersonDto;
import no.nav.foreldrepenger.selvbetjening.tjeneste.oppslag.dto.SøkerinfoDto;

public interface Oppslag {

    PersonDto hentPerson();

    SøkerinfoDto hentSøkerinfo();

    AktørId hentAktørId(String fnr);

}
