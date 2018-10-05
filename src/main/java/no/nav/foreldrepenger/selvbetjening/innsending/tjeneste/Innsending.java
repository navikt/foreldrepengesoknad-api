package no.nav.foreldrepenger.selvbetjening.innsending.tjeneste;

import no.nav.foreldrepenger.selvbetjening.innsending.json.Ettersending;
import org.springframework.http.ResponseEntity;

import no.nav.foreldrepenger.selvbetjening.innsending.json.Kvittering;
import no.nav.foreldrepenger.selvbetjening.innsending.json.Søknad;

public interface Innsending {

    ResponseEntity<Kvittering> sendInn(Søknad søknad);
    ResponseEntity<Kvittering> sendInn(Ettersending søknad);
}
