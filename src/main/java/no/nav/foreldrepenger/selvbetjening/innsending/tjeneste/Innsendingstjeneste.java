package no.nav.foreldrepenger.selvbetjening.innsending.tjeneste;

import no.nav.foreldrepenger.selvbetjening.felles.attachments.Image2PDFConverter;
import no.nav.foreldrepenger.selvbetjening.felles.attachments.exceptions.AttachmentConversionException;
import no.nav.foreldrepenger.selvbetjening.felles.util.Enabled;
import no.nav.foreldrepenger.selvbetjening.innsending.json.Engangsstønad;
import no.nav.foreldrepenger.selvbetjening.innsending.json.Foreldrepengesøknad;
import no.nav.foreldrepenger.selvbetjening.innsending.json.Kvittering;
import no.nav.foreldrepenger.selvbetjening.innsending.json.Søknad;
import no.nav.foreldrepenger.selvbetjening.innsending.tjeneste.json.EngangsstønadDto;
import no.nav.foreldrepenger.selvbetjening.innsending.tjeneste.json.ForeldrepengesøknadDto;
import no.nav.foreldrepenger.selvbetjening.innsending.tjeneste.json.SøknadDto;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import javax.ws.rs.BadRequestException;
import java.io.IOException;
import java.net.URI;

import static java.time.LocalDateTime.now;
import static java.util.Arrays.stream;
import static org.slf4j.LoggerFactory.getLogger;

@Service
@ConditionalOnProperty(name = "stub.mottak", havingValue = "false", matchIfMissing = true)
public class Innsendingstjeneste implements Innsending {

    private static final Logger LOG = getLogger(Innsendingstjeneste.class);

    private final Image2PDFConverter converter;
    private final URI mottakServiceUrl;
    private final RestTemplate template;

    public Innsendingstjeneste(@Value("${FPSOKNAD_MOTTAK_API_URL}") URI baseUri, RestTemplate template, Image2PDFConverter converter) {
        this.mottakServiceUrl = mottakUriFra(baseUri);
        this.template = template;
        this.converter = converter;
    }

    @Override
    public ResponseEntity<Kvittering> sendInn(Søknad søknad) {
        LOG.info("Poster søknad til {}", mottakServiceUrl);
        søknad.opprettet = now();
        return post(søknad);
    }

    private ResponseEntity<Kvittering> post(Søknad søknad) {
        if (!Enabled.foreldrepengesøknad && søknad instanceof Foreldrepengesøknad) {
            LOG.info("Mottok foreldrepengesøknad, men innsending av foreldrepengesøknad er togglet av!");
            throw new BadRequestException("Application with type foreldrepengesøknad is not supported yet");
        }

        return template.postForEntity(mottakServiceUrl, body(søknad), Kvittering.class);
    }

    private HttpEntity<SøknadDto> body(@RequestBody Søknad søknad) {
        SøknadDto dto;

        if (søknad instanceof Engangsstønad) {
            dto = new EngangsstønadDto((Engangsstønad) søknad);
        } else if (søknad instanceof Foreldrepengesøknad) {
            dto = new ForeldrepengesøknadDto((Foreldrepengesøknad) søknad);
        } else {
            LOG.warn("Mottok en søknad av ukjent type..");
            throw new BadRequestException("Unknown application type");
        }

        søknad.vedlegg.stream()
                .map(v -> v.content)
                .map(converter::convert)
                .forEach(dto::addVedlegg);

        return new HttpEntity<>(dto);
    }

    private static URI mottakUriFra(URI baseUri) {
        return UriComponentsBuilder
                .fromUri(baseUri)
                .path("/mottak/send")
                .build().toUri();
    }
}
