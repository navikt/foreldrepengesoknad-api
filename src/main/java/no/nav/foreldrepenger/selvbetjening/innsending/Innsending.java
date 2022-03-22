package no.nav.foreldrepenger.selvbetjening.innsending;

import no.nav.foreldrepenger.common.domain.Kvittering;
import no.nav.foreldrepenger.selvbetjening.http.Pingable;
import no.nav.foreldrepenger.selvbetjening.http.RetryAware;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.EttersendingFrontend;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.SøknadFrontend;

public interface Innsending extends Pingable, RetryAware {

    Kvittering sendInn(SøknadFrontend søknad);

    Kvittering ettersend(EttersendingFrontend søknad);

    Kvittering endre(SøknadFrontend søknad);

}
