package no.nav.foreldrepenger.selvbetjening.innsending.tjeneste;

import no.nav.foreldrepenger.selvbetjening.innsending.json.Kvittering;
import no.nav.foreldrepenger.selvbetjening.innsending.json.Søknad;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface Innsending {

    ResponseEntity<Kvittering> sendInn(Søknad søknad, MultipartFile[] vedlegg) throws Exception;

}
