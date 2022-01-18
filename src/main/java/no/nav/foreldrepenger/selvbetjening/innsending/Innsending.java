package no.nav.foreldrepenger.selvbetjening.innsending;

import no.nav.foreldrepenger.selvbetjening.http.Pingable;
import no.nav.foreldrepenger.selvbetjening.http.RetryAware;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.Ettersending;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.Kvittering;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.SøknadFrontend;

public interface Innsending extends Pingable, RetryAware {

    Kvittering sendInn(SøknadFrontend søknad);

    Kvittering ettersend(Ettersending søknad);

    Kvittering endre(SøknadFrontend søknad);

}
