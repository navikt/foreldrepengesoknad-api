package no.nav.foreldrepenger.selvbetjening.tjeneste.innsending;

import no.nav.foreldrepenger.selvbetjening.tjeneste.Pingable;
import no.nav.foreldrepenger.selvbetjening.tjeneste.felles.RetryAware;
import no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.domain.Ettersending;
import no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.domain.Kvittering;
import no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.domain.Søknad;

public interface Innsending extends Pingable, RetryAware {

    Kvittering sendInn(Søknad søknad);

    Kvittering ettersend(Ettersending søknad);

    Kvittering endre(Søknad søknad);

}
