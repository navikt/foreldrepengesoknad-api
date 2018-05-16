package no.nav.foreldrepenger.selvbetjening.innsending.tjeneste;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import no.nav.foreldrepenger.selvbetjening.innsending.json.Engangsstønad;
import no.nav.foreldrepenger.selvbetjening.innsending.json.Kvittering;
import no.nav.foreldrepenger.selvbetjening.innsending.json.Søknad;
import no.nav.foreldrepenger.selvbetjening.innsending.tjeneste.json.EngangsstønadDto;
import org.slf4j.Logger;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.inject.Inject;

import static java.time.LocalDateTime.now;
import static no.nav.foreldrepenger.selvbetjening.oppslag.tjeneste.OppslagstjenesteStub.person;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.http.HttpStatus.OK;

@Service
@ConditionalOnProperty(name = "stub.oppslag", havingValue = "true")
public class InnsendingstjenesteStub implements Innsending {

    private static final Logger LOG = getLogger(InnsendingstjenesteStub.class);

    @Inject
    private ObjectMapper mapper;

    @Override
    public ResponseEntity<Kvittering> sendInn(Søknad søknad, MultipartFile[] vedlegg) throws Exception {
        søknad.opprettet = now();
        return postStub(søknad);
    }

    private ResponseEntity<Kvittering> postStub(Søknad søknad) throws JsonProcessingException {
        EngangsstønadDto dto = new EngangsstønadDto((Engangsstønad) søknad, person());
        LOG.info("Posting JSON (stub): {}", mapper.writeValueAsString(dto));
        return new ResponseEntity<>(Kvittering.STUB, OK);
    }
}
