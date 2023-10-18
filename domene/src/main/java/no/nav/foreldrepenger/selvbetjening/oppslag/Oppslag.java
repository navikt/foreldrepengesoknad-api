package no.nav.foreldrepenger.selvbetjening.oppslag;

import no.nav.foreldrepenger.selvbetjening.http.RetryAware;
import no.nav.foreldrepenger.selvbetjening.oppslag.domain.PersonFrontend;
import no.nav.foreldrepenger.selvbetjening.oppslag.domain.Søkerinfo;

public interface Oppslag extends RetryAware {

    PersonFrontend hentPerson();

    Søkerinfo hentSøkerinfo();

}
