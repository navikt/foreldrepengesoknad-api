package no.nav.foreldrepenger.selvbetjening.oppslag;

import no.nav.foreldrepenger.selvbetjening.http.Pingable;
import no.nav.foreldrepenger.selvbetjening.http.RetryAware;
import no.nav.foreldrepenger.selvbetjening.oppslag.domain.Søkerinfo;

public interface Oppslag extends Pingable, RetryAware {

    Søkerinfo hentSøkerinfo();

}
