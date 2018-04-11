package no.nav.foreldrepenger.selvbetjening.rest;

import static java.time.LocalDateTime.now;
import static no.nav.foreldrepenger.selvbetjening.rest.MottakController.REST_ENGANGSSTONAD;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

import java.net.URI;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
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
import no.nav.foreldrepenger.selvbetjening.rest.json.Kvittering;
import no.nav.foreldrepenger.selvbetjening.rest.util.ImageByteArray2PdfConverter;
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

    @Inject
    private ImageByteArray2PdfConverter converter;

    @Value("${stub.mottak:false}")
    private boolean stub;

    public MottakController(@Value("${FPSOKNAD_MOTTAK_API_URL}") URI baseUri, RestTemplate template,
            Oppslag oppslag) {
        this.mottakServiceUrl = mottakUriFra(baseUri);
        this.template = template;
        this.oppslag = oppslag;
    }

    @PostMapping(consumes = MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Kvittering> sendInn(@RequestPart("soknad") Engangsstønad engangsstønad,
            @RequestPart("vedlegg") MultipartFile... vedlegg) throws Exception {
        LOG.info("Poster engangsstønad til {}", mottakServiceUrl);
        engangsstønad.opprettet = now();

        if (stub) {
            LOG.info("Stubber mottak...");

            EngangsstønadDto dto = new EngangsstønadDto(engangsstønad,
                    new PersonDto("STUB_FNR", "STUB_AKTOR", "STUB", "STUBBE", "STUBNES", "STUB_MÅLFORM"));
            String json = mapper.writeValueAsString(dto);
            LOG.info("Posting JSON (stub): {}", json);
            return new ResponseEntity<>(Kvittering.STUB, HttpStatus.OK);
        }
        return template.postForEntity(mottakServiceUrl, body(engangsstønad, oppslag.hentPerson(), vedlegg),
                Kvittering.class);
    }

    private HttpEntity<EngangsstønadDto> body(@RequestBody Engangsstønad engangsstønad, PersonDto person,
            MultipartFile... vedlegg) throws Exception {
        EngangsstønadDto dto = new EngangsstønadDto(engangsstønad, person);
        String json = mapper.writeValueAsString(dto);
        LOG.info("Posting JSON (without attachment): {})", json);

        for (MultipartFile multipartFile : vedlegg) {
            byte[] vedleggBytes = multipartFile.getBytes();
            dto.addVedlegg(converter.convert(vedleggBytes));
        }
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
