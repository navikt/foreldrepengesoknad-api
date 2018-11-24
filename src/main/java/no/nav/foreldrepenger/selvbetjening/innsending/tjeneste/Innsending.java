package no.nav.foreldrepenger.selvbetjening.innsending.tjeneste;

import no.nav.foreldrepenger.selvbetjening.innsending.json.Ettersending;
import no.nav.foreldrepenger.selvbetjening.innsending.json.Kvittering;
import no.nav.foreldrepenger.selvbetjening.innsending.json.Søknad;

public interface Innsending {

    Kvittering sendInn(Søknad søknad);

    Kvittering sendInn(Ettersending søknad);

    Kvittering endre(Søknad søknad);

}
