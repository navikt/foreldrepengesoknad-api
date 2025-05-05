package no.nav.foreldrepenger.selvbetjening.oppslag;

import no.nav.foreldrepenger.selvbetjening.http.RetryAware;
import no.nav.foreldrepenger.selvbetjening.oppslag.dto.PersonFrontend;
import no.nav.foreldrepenger.selvbetjening.oppslag.dto.Søkerinfo;

public interface Oppslag extends RetryAware {

    PersonFrontend hentPerson();

    Søkerinfo hentSøkerinfo();

}
