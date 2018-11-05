package no.nav.foreldrepenger.selvbetjening.oppslag.tjeneste;

import java.util.List;

import no.nav.foreldrepenger.selvbetjening.oppslag.json.AktørId;
import no.nav.foreldrepenger.selvbetjening.oppslag.json.Sak;
import no.nav.foreldrepenger.selvbetjening.oppslag.tjeneste.json.PersonDto;
import no.nav.foreldrepenger.selvbetjening.oppslag.tjeneste.json.SøkerinfoDto;

public interface Oppslag {

    PersonDto hentPerson();

    SøkerinfoDto hentSøkerinfo();

    List<Sak> hentSaker();

    AktørId hentAktørId(String fnr);

}
