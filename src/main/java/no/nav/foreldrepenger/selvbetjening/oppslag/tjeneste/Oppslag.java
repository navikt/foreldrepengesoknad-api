package no.nav.foreldrepenger.selvbetjening.oppslag.tjeneste;

import java.util.List;

import no.nav.foreldrepenger.selvbetjening.oppslag.tjeneste.json.Fagsak;
import no.nav.foreldrepenger.selvbetjening.oppslag.tjeneste.json.PersonDto;
import no.nav.foreldrepenger.selvbetjening.oppslag.tjeneste.json.SøkerinfoDto;

public interface Oppslag {

    PersonDto hentPerson();

    SøkerinfoDto hentSøkerinfo();

    List<Fagsak> hentFagsaker();

    String hentSøknad(String behandlingId);

}
