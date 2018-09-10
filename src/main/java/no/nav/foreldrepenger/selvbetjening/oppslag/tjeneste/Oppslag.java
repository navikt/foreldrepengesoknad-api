package no.nav.foreldrepenger.selvbetjening.oppslag.tjeneste;

import java.util.List;

import no.nav.foreldrepenger.selvbetjening.oppslag.tjeneste.json.Sak;
import no.nav.foreldrepenger.selvbetjening.oppslag.tjeneste.json.PersonDto;
import no.nav.foreldrepenger.selvbetjening.oppslag.tjeneste.json.SøkerinfoDto;

public interface Oppslag {

    PersonDto hentPerson();

    SøkerinfoDto hentSøkerinfo();

    List<Sak> hentSaker();

    String hentSøknad(String behandlingId);

}
