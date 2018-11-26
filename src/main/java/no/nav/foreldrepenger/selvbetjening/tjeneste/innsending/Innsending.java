package no.nav.foreldrepenger.selvbetjening.tjeneste.innsending;

import no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.domain.Ettersending;
import no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.domain.Kvittering;
import no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.domain.Søknad;

public interface Innsending {

    Kvittering sendInn(Søknad søknad);

    Kvittering sendInn(Ettersending søknad);

    Kvittering endre(Søknad søknad);

}
