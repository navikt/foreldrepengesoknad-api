package no.nav.foreldrepenger.selvbetjening.innsending;

import static no.nav.boot.conditionals.EnvUtil.isGcp;
import static no.nav.foreldrepenger.common.domain.felles.InnsendingsType.LASTET_OPP;
import static no.nav.foreldrepenger.common.util.StreamUtil.safeStream;
import static no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.mapper.EttersendingMapper.tilEttersending;
import static no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.mapper.SøknadMapper.tilSøknad;
import static no.nav.foreldrepenger.selvbetjening.vedlegg.VedleggUtil.mediaType;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_PDF;
import static org.springframework.http.MediaType.MULTIPART_MIXED;

import java.time.LocalDate;
import java.util.List;

import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestOperations;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import no.nav.foreldrepenger.common.domain.Kvittering;
import no.nav.foreldrepenger.selvbetjening.http.AbstractRestConnection;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.Innsending;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.VedleggDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.endringssøknad.EndringssøknadDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.SøknadDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.ettersendelse.EttersendelseDto;

@Component
public class InnsendingConnection extends AbstractRestConnection {
    private static final String BODY_PART_NAME = "body";
    private static final String VEDLEGG_PART_NAME = "vedlegg";
    private static final String VEDLEGG_REFERANSE_HEADER = "vedleggsreferanse";

    private final ObjectMapper mapper;
    private final Environment env;
    private final InnsendingConfig config;

    public InnsendingConnection(RestOperations operations, ObjectMapper mapper, Environment env, InnsendingConfig config) {
        super(operations);
        this.mapper = mapper;
        this.env = env;
        this.config = config;
    }

    public Kvittering sendInn(Innsending innsending) {
        if (innsending instanceof no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.SøknadDto || innsending instanceof SøknadDto) {
            return postForEntity(config.innsendingURI(), body(innsending), Kvittering.class);
        } else if (innsending instanceof no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.endringssøknad.EndringssøknadDto || innsending instanceof EndringssøknadDto) {
            return postForEntity(config.endringURI(), body(innsending), Kvittering.class);
        } else if (innsending instanceof EttersendelseDto ettersendelse) {
            return postForEntity(config.ettersendingURI(), body(ettersendelse), Kvittering.class);
        } else {
            throw new IllegalStateException("Utviklerfeil: Innsending støtter bare søknad, endringssøknad og ettersendelse");
        }
    }

    private HttpEntity<MultiValueMap<String, HttpEntity<?>>> body(Innsending innsending) {
        return body(tilJson(innsending), innsending.vedlegg());
    }

    private HttpEntity<MultiValueMap<String, HttpEntity<?>>> body(EttersendelseDto ettersendelse) {
        return body(tilJson(ettersendelse), ettersendelse.vedlegg());
    }

    private String tilJson(Innsending innsending) {
        return tilJson(tilSøknad(innsending, mottattDato(innsending)));
    }

    private String tilJson(EttersendelseDto ettersendelse) {
        return tilJson(tilEttersending(ettersendelse));
    }

    private static HttpEntity<MultiValueMap<String, HttpEntity<?>>> body(String jsonBody, List<VedleggDto> vedlegg) {
        var builder = new MultipartBodyBuilder();
        builder.part(BODY_PART_NAME, jsonBody, APPLICATION_JSON);
        safeStream(vedlegg)
            .distinct()
            .filter(v -> v.getInnsendingsType() == null || LASTET_OPP.name().equals(v.getInnsendingsType()))
            .filter(v -> v.getContent() != null)
            .forEach(v -> {
                guardIkkePDF(v);
                builder.part(VEDLEGG_PART_NAME, v.getContent(), APPLICATION_PDF).headers(headers -> headers.set(VEDLEGG_REFERANSE_HEADER, v.getId().referanse()));
            });

        return new HttpEntity<>(builder.build(), headers());
    }

    private static void guardIkkePDF(VedleggDto vedlegg) {
        var mediatype = mediaType(vedlegg.getContent());
        if (!APPLICATION_PDF.equals(mediatype)) {
            throw new IllegalStateException("Utviklerfeil: Mottok noe annet en PDF ved innsending " + mediatype);
        }
    }

    private static HttpHeaders headers() {
        var headers = new HttpHeaders();
        headers.setContentType(MULTIPART_MIXED);
        return headers;
    }

    private String tilJson(Object innsending) {
        try {
            return mapper.writeValueAsString(innsending);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Klarte ikke å oversette innsending til JSON!", e);
        }
    }

    private LocalDate mottattDato(Innsending i) {
        if (isGcp(env) || i.mottattdato() == null) {
            return LocalDate.now();
        } else {
            return i.mottattdato(); // Brukes av autotest for å spesifisere mottatttidspunkt annet enn dagens dato
        }
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [config=" + config + "]";
    }

}
