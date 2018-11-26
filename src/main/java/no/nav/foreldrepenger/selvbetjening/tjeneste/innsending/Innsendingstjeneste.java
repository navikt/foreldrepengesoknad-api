package no.nav.foreldrepenger.selvbetjening.tjeneste.innsending;

import static java.time.LocalDateTime.now;
import static org.slf4j.LoggerFactory.getLogger;

import java.net.URI;

import javax.inject.Inject;
import javax.ws.rs.BadRequestException;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import no.nav.foreldrepenger.selvbetjening.attachments.Image2PDFConverter;
import no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.domain.Engangsstønad;
import no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.domain.Ettersending;
import no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.domain.Foreldrepengesøknad;
import no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.domain.Kvittering;
import no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.domain.Søknad;
import no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.dto.EngangsstønadDto;
import no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.dto.EttersendingDto;
import no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.dto.ForeldrepengesøknadDto;
import no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.dto.SøknadDto;

@Service
@ConditionalOnProperty(name = "stub.mottak", havingValue = "false", matchIfMissing = true)
public class Innsendingstjeneste implements Innsending {

    private static final Logger LOG = getLogger(Innsendingstjeneste.class);

    @Inject
    private ObjectMapper mapper;

    private final URI mottakServiceSøknadUrl;
    private final URI mottakServiceEttersendingUrl;
    private final URI mottakServiceEndringssøknadUrl;
    private final RestTemplate template;
    private final Image2PDFConverter converter;

    public Innsendingstjeneste(@Value("${FPSOKNAD_MOTTAK_API_URL}") URI baseUri, RestTemplate template,
            Image2PDFConverter converter) {
        this.mottakServiceSøknadUrl = innsendingUriFra(baseUri);
        this.mottakServiceEttersendingUrl = ettersendingUriFra(baseUri);
        this.mottakServiceEndringssøknadUrl = endringssøknadUriFra(baseUri);
        this.template = template;
        this.converter = converter;
    }

    @Override
    public Kvittering sendInn(Søknad søknad) {
        LOG.info("Sender inn søknad av type {}", søknad.type);
        LOG.trace("Poster søknad {} til {}", søknad, mottakServiceSøknadUrl);
        søknad.opprettet = now();
        return post(søknad);
    }

    @Override
    public Kvittering sendInn(Ettersending ettersending) {
        LOG.info("Sender inn ettersending på sak {}", ettersending.saksnummer);
        LOG.trace("Poster ettersending {} til {}", ettersending, mottakServiceEttersendingUrl);
        return post(ettersending);
    }

    @Override
    public Kvittering endre(Søknad søknad) {
        LOG.info("Sender inn endringssøknad av type {}", søknad.type);
        LOG.trace("Poster endringssøknad {} til {}", søknad, mottakServiceEndringssøknadUrl);
        return postEndring(søknad);
    }

    private Kvittering post(Søknad søknad) {
        return template.postForEntity(mottakServiceSøknadUrl, body(søknad), Kvittering.class).getBody();
    }

    private Kvittering post(Ettersending ettersending) {
        return template.postForEntity(mottakServiceEttersendingUrl, body(ettersending), Kvittering.class).getBody();
    }

    private Kvittering postEndring(Søknad søknad) {
        return template.postForEntity(mottakServiceEndringssøknadUrl, body(søknad), Kvittering.class).getBody();
    }

    private HttpEntity<SøknadDto> body(@RequestBody Søknad søknad) {
        SøknadDto dto;
        if (søknad instanceof Engangsstønad) {
            dto = new EngangsstønadDto((Engangsstønad) søknad);
        }
        else if (søknad instanceof Foreldrepengesøknad) {
            dto = new ForeldrepengesøknadDto((Foreldrepengesøknad) søknad);
            logJSON(dto);
        }
        else {
            LOG.warn("Mottok en søknad av ukjent type..");
            throw new BadRequestException("Unknown application type");
        }

        søknad.vedlegg.forEach(v -> {
            if (v.content != null) {
                v.content = converter.convert(v.content);
            }
            dto.addVedlegg(v);
        });

        return new HttpEntity<>(dto);
    }

    private EttersendingDto body(@RequestBody Ettersending ettersending) {
        EttersendingDto dto = new EttersendingDto(ettersending);
        ettersending.vedlegg.forEach(v -> {
            v.content = converter.convert(v.content);
            dto.addVedlegg(v);
        });
        return dto;
    }

    private void logJSON(SøknadDto dto) {
        try {
            LOG.trace("JSON er {}", mapper.writerWithDefaultPrettyPrinter().writeValueAsString(dto));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    private static URI innsendingUriFra(URI baseUri) {
        return UriComponentsBuilder
                .fromUri(baseUri)
                .path("/mottak/send")
                .build().toUri();
    }

    private static URI ettersendingUriFra(URI baseUri) {
        return UriComponentsBuilder
                .fromUri(baseUri)
                .path("/mottak/ettersend")
                .build().toUri();
    }

    private static URI endringssøknadUriFra(URI baseUri) {
        return UriComponentsBuilder
                .fromUri(baseUri)
                .path("/mottak/endre")
                .build().toUri();
    }
}
