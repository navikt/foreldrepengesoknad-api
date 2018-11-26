package no.nav.foreldrepenger.selvbetjening.tjeneste.oppslag;

import no.nav.foreldrepenger.selvbetjening.tjeneste.Pingable;
import no.nav.foreldrepenger.selvbetjening.tjeneste.oppslag.domain.AktørId;
import no.nav.foreldrepenger.selvbetjening.tjeneste.oppslag.domain.Person;
import no.nav.foreldrepenger.selvbetjening.tjeneste.oppslag.domain.Søkerinfo;

public interface Oppslag extends Pingable {

    Person hentPerson();

    Søkerinfo hentSøkerinfo();

    AktørId hentAktørId(String fnr);

}
