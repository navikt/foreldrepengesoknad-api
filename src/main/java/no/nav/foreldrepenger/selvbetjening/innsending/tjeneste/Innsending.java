package no.nav.foreldrepenger.selvbetjening.innsending.tjeneste;

import org.springframework.http.ResponseEntity;

import no.nav.foreldrepenger.selvbetjening.innsending.json.Ettersending;
import no.nav.foreldrepenger.selvbetjening.innsending.json.Kvittering;
import no.nav.foreldrepenger.selvbetjening.innsending.json.Søknad;

public interface Innsending {

    ResponseEntity<Kvittering> sendInn(Søknad søknad);
    ResponseEntity<Kvittering> sendInn(Ettersending søknad);
    ResponseEntity<Kvittering> endre(Søknad søknad);

}
