package no.nav.foreldrepenger.selvbetjening.innsending.tjeneste;

import static java.time.LocalDateTime.now;
import static org.slf4j.LoggerFactory.getLogger;

import java.net.URI;

import javax.inject.Inject;
import javax.ws.rs.BadRequestException;

import no.nav.foreldrepenger.selvbetjening.innsending.json.*;
import no.nav.foreldrepenger.selvbetjening.innsending.tjeneste.json.EttersendingDto;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import no.nav.foreldrepenger.selvbetjening.felles.attachments.Image2PDFConverter;
import no.nav.foreldrepenger.selvbetjening.felles.util.Enabled;
import no.nav.foreldrepenger.selvbetjening.innsending.tjeneste.json.EngangsstønadDto;
import no.nav.foreldrepenger.selvbetjening.innsending.tjeneste.json.ForeldrepengesøknadDto;
import no.nav.foreldrepenger.selvbetjening.innsending.tjeneste.json.SøknadDto;

@Service
@ConditionalOnProperty(name = "stub.mottak", havingValue = "false", matchIfMissing = true)
public class Innsendingstjeneste implements Innsending {

    private static final Logger LOG = getLogger(Innsendingstjeneste.class);

    private final Image2PDFConverter converter;
    private final URI mottakServiceSøknadUrl;
    private final URI mottakServiceEttersendingUrl;
    private final RestTemplate template;
    @Inject
    private ObjectMapper mapper;

    public Innsendingstjeneste(@Value("${FPSOKNAD_MOTTAK_API_URL}") URI baseUri, RestTemplate template,
            Image2PDFConverter converter) {
        this.mottakServiceSøknadUrl = mottakSøknadUriFra(baseUri);
        this.mottakServiceEttersendingUrl = mottaEttersendingUriFra(baseUri);
        this.template = template;
        this.converter = converter;
    }

    private static URI mottakSøknadUriFra(URI baseUri) {
        return UriComponentsBuilder
                .fromUri(baseUri)
                .path("/mottak/send")
                .build().toUri();
    }

    private static URI mottaEttersendingUriFra(URI baseUri) {
        return UriComponentsBuilder
                .fromUri(baseUri)
                .path("/mottak/ettersend")
                .build().toUri();
    }

    @Override
    public ResponseEntity<Kvittering> sendInn(Søknad søknad) {
        LOG.trace("Poster søknad {} til {}", søknad, mottakServiceSøknadUrl);
        søknad.opprettet = now();
        return post(søknad);
    }

    private ResponseEntity<Kvittering> post(Søknad søknad) {
        if (!Enabled.foreldrepengesøknad && søknad instanceof Foreldrepengesøknad) {
            LOG.info("Mottok foreldrepengesøknad, men innsending av foreldrepengesøknad er togglet av!");
            throw new BadRequestException("Application with type foreldrepengesøknad is not supported yet");
        }

        return template.postForEntity(mottakServiceSøknadUrl, body(søknad), Kvittering.class);
    }


    private HttpEntity<SøknadDto> body(@RequestBody Søknad søknad) {
        SøknadDto dto;
        if (søknad instanceof Engangsstønad) {
            dto = new EngangsstønadDto((Engangsstønad) søknad);
        }
        else if (søknad instanceof Foreldrepengesøknad) {
            LOG.trace("Mottatt søknad er {}", søknad);
            dto = new ForeldrepengesøknadDto((Foreldrepengesøknad) søknad);
            LOG.trace("DTO til mottak er {}", dto);
            logJSON(dto);
        }
        else {
            LOG.warn("Mottok en søknad av ukjent type..");
            throw new BadRequestException("Unknown application type");
        }

        søknad.vedlegg.forEach(v -> {
            v.content = converter.convert(v.content);
            dto.addVedlegg(v);
        });

        return new HttpEntity<>(dto);
    }

    @Override
    public ResponseEntity<Kvittering> sendInn(Ettersending ettersending) {
        LOG.trace("Poster ettersending {} til {}", ettersending, mottakServiceEttersendingUrl);
        return post(ettersending);
    }

    private ResponseEntity<Kvittering> post(Ettersending ettersending) {
        return template.postForEntity(mottakServiceEttersendingUrl, body(ettersending), Kvittering.class);
    }

    private HttpEntity<EttersendingDto> body(@RequestBody Ettersending ettersending) {
        EttersendingDto dto = new EttersendingDto(ettersending);
        ettersending.vedlegg.forEach(v -> {
            v.content = converter.convert(v.content);
            dto.addVedlegg(v);
        });
        return new HttpEntity<>(dto);
    }

    private void logJSON(SøknadDto dto) {
        try {
            LOG.trace("JSON er {}", mapper.writerWithDefaultPrettyPrinter().writeValueAsString(dto));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
