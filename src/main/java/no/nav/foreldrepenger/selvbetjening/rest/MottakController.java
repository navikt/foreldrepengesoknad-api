package no.nav.foreldrepenger.selvbetjening.rest;

import static java.time.LocalDateTime.now;
import static no.nav.foreldrepenger.selvbetjening.rest.MottakController.REST_ENGANGSSTONAD;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;
import static org.springframework.http.ResponseEntity.ok;

import java.net.URI;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;

import no.nav.foreldrepenger.selvbetjening.consumer.Oppslag;
import no.nav.foreldrepenger.selvbetjening.consumer.json.EngangsstønadDto;
import no.nav.foreldrepenger.selvbetjening.consumer.json.PersonDto;
import no.nav.foreldrepenger.selvbetjening.rest.json.Engangsstønad;
import no.nav.security.spring.oidc.validation.api.ProtectedWithClaims;

@RestController
@ProtectedWithClaims(issuer = "selvbetjening", claimMap = { "acr=Level4" })
@RequestMapping(REST_ENGANGSSTONAD)
public class MottakController {

    public static final String REST_ENGANGSSTONAD = "/rest/engangsstonad";

    private static final Logger LOG = getLogger(MottakController.class);

    private final RestTemplate template;
    private final URI mottakServiceUrl;
    private final Oppslag oppslag;

    @Inject
    private ObjectMapper mapper;

    @Value("${stub.mottak:false}")
    private boolean stub;

    public MottakController(@Value("${FPSOKNAD_MOTTAK_API_URL}") URI baseUri, RestTemplate template,
            Oppslag oppslag) {
        this.mottakServiceUrl = mottakUriFra(baseUri);
        this.template = template;
        this.oppslag = oppslag;
    }

    @PostMapping(consumes = MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Engangsstønad> sendInn(@RequestPart("soknad") Engangsstønad engangsstønad,
            @RequestPart("vedlegg") MultipartFile[] vedlegg) throws Exception {
        LOG.info("Poster engangsstønad");
        engangsstønad.opprettet = now();

        if (stub) {
            LOG.info("Stubber mottak...");

            EngangsstønadDto dto = new EngangsstønadDto(engangsstønad, "STUB_FNR", "STUB_AKTØRID");
            String json = mapper.writeValueAsString(dto);
            LOG.info("Posting JSON (stub): {}", json);
            return ok(engangsstønad);
        }

        String fnr = engangsstønad.fnr; // TODO: mottak bør hente fnr og aktørId fra oidc token selv.
        PersonDto personDto = oppslag.hentPerson();
        String aktørId = personDto.aktorId;

        LOG.info("Mottak URL: " + mottakServiceUrl);
        template.postForEntity(mottakServiceUrl, body(engangsstønad, vedlegg[0], fnr, aktørId), String.class);
        return ok(engangsstønad);
    }

    private HttpEntity<EngangsstønadDto> body(@RequestBody Engangsstønad engangsstønad, MultipartFile vedlegg,
            String aktørId, String fnr) throws Exception {
        EngangsstønadDto dto = new EngangsstønadDto(engangsstønad, fnr, aktørId);
        dto.addVedlegg(vedlegg.getBytes());
        String json = mapper.writeValueAsString(dto);
        LOG.info("Posting JSON: {}", json);
        return new HttpEntity<>(dto);
    }

    private static URI mottakUriFra(URI baseUri) {
        return UriComponentsBuilder
                .fromUri(baseUri)
                .path("/mottak/dokmot/send")
                .build().toUri();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [template=" + template + ", mottakServiceUrl=" + mottakServiceUrl + "]";
    }
}
