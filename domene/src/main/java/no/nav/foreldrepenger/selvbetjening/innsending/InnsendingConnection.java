package no.nav.foreldrepenger.selvbetjening.innsending;

import static no.nav.boot.conditionals.EnvUtil.isGcp;
import static no.nav.foreldrepenger.common.domain.felles.InnsendingsType.LASTET_OPP;
import static no.nav.foreldrepenger.common.util.StreamUtil.safeStream;
import static no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.mapper.EttersendingMapper.tilEttersending;
import static no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.mapper.SøknadMapper.tilEndringssøknad;
import static no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.mapper.SøknadMapper.tilSøknad;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_PDF;
import static org.springframework.http.MediaType.MULTIPART_MIXED;

import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import no.nav.foreldrepenger.common.domain.Søknad;
import no.nav.foreldrepenger.common.domain.felles.Ettersending;
import no.nav.foreldrepenger.selvbetjening.http.AbstractRestConnection;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.MottattTidspunkt;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.SøknadDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.VedleggDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.endringssøknad.EndringssøknadDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.engangsstønad.SøknadV2Dto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.ettersendelse.EttersendelseDto;

@Component
public class InnsendingConnection extends AbstractRestConnection {
    private static final Logger SECURE_LOGGER = LoggerFactory.getLogger("secureLogger");
    private static final String BODY_PART_NAME = "body";
    private static final String VEDLEGG_PART_NAME = "vedlegg";
    private static final String VEDLEGG_REFERANSE_HEADER = "vedleggsreferanse";

    private final ObjectMapper mapper;
    private final Environment env;
    private final InnsendingConfig config;
    private final VedleggsHåndteringTjeneste vedleggshåndtering;

    public InnsendingConnection(RestOperations operations, ObjectMapper mapper, Environment env, InnsendingConfig config, VedleggsHåndteringTjeneste vedleggshåndtering) {
        super(operations);
        this.mapper = mapper;
        this.env = env;
        this.config = config;
        this.vedleggshåndtering = vedleggshåndtering;
    }

    public Kvittering sendInn(SøknadDto søknad) {
        SECURE_LOGGER.info("{} mottatt fra frontend med følende innhold: {}", søknad.type(), tilJson(søknad));
        vedleggshåndtering.fjernDupliserteVedleggFraSøknad(søknad);
        return postForEntity(config.innsendingURI(), body(søknad), Kvittering.class);
    }

    public Kvittering sendInn(SøknadV2Dto søknad) {
        SECURE_LOGGER.info("Engangsstønad mottatt fra frontend med følende innhold: {}", tilJson(søknad));
        vedleggshåndtering.fjernDupliserteVedleggFraSøknad(søknad);
        return postForEntity(config.innsendingURI(), body(søknad), Kvittering.class);
    }

    public Kvittering endre(EndringssøknadDto endringssøknad) {
        SECURE_LOGGER.info("{} mottatt fra frontend med følende innhold: {}", endringssøknad.type(), tilJson(endringssøknad));
        vedleggshåndtering.fjernDupliserteVedleggFraSøknad(endringssøknad);
        return postForEntity(config.endringURI(), body(endringssøknad), Kvittering.class);
    }

    public Kvittering ettersend(EttersendelseDto ettersending) {
        vedleggshåndtering.fjernDupliserteVedleggFraEttersending(ettersending);
        return postForEntity(config.ettersendingURI(), body(ettersending), Kvittering.class);
    }

    private HttpEntity<MultiValueMap<String, HttpEntity<?>>> body(SøknadDto søknad) {
        return body(tilJson(søknad), søknad.vedlegg());
    }

    private HttpEntity<MultiValueMap<String, HttpEntity<?>>> body(SøknadV2Dto søknad) {
        return body(tilJson(søknad), søknad.vedlegg());
    }

    private HttpEntity<MultiValueMap<String, HttpEntity<?>>> body(EndringssøknadDto endringssøknadDto) {
        return body(tilJson(endringssøknadDto), endringssøknadDto.vedlegg());
    }

    private HttpEntity<MultiValueMap<String, HttpEntity<?>>> body(EttersendelseDto ettersendelseDto) {
        return body(tilJson(ettersendelseDto), ettersendelseDto.vedlegg());
    }

    private String tilJson(SøknadDto søknad) {
        return tilJson(tilSøknad(søknad, mottattDato(søknad)));
    }

    private String tilJson(SøknadV2Dto søknad) {
        return tilJson(tilSøknad(søknad, mottattDato(søknad)));
    }

    private String tilJson(EndringssøknadDto endringssøknad) {
        return tilJson(tilEndringssøknad(endringssøknad, mottattDato(endringssøknad)));
    }

    private String tilJson(EttersendelseDto ettersendelse) {
        return tilJson(tilEttersending(ettersendelse));
    }

    private String tilJson(Søknad søknad) {
        try {
            return mapper.writeValueAsString(søknad);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Klarte ikke å oversette søknad til JSON!", e);
        }
    }

    private String tilJson(Ettersending ettersending) {
        try {
            return mapper.writeValueAsString(ettersending);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Klarte ikke å oversette ettersending til JSON!", e);
        }
    }

    private static HttpEntity<MultiValueMap<String, HttpEntity<?>>> body(String jsonBody, List<VedleggDto> vedlegg) {
        var builder = new MultipartBodyBuilder();
        builder.part(BODY_PART_NAME, jsonBody, APPLICATION_JSON);
        safeStream(vedlegg)
            .filter(v -> v.getInnsendingsType() == null || LASTET_OPP.name().equals(v.getInnsendingsType()))
            .filter(v -> v.getContent() != null)
            .forEach(v -> builder.part(VEDLEGG_PART_NAME, v.getContent(), APPLICATION_PDF)
                    .headers(headers -> headers.set(VEDLEGG_REFERANSE_HEADER, v.getId().referanse())));
        return new HttpEntity<>(builder.build(), headers());
    }

    private static HttpHeaders headers() {
        var headers = new HttpHeaders();
        headers.setContentType(MULTIPART_MIXED);
        return headers;
    }

    private LocalDate mottattDato(MottattTidspunkt m) {
        if (isGcp(env) || m.mottattdato() == null) {
            return LocalDate.now();
        } else {
            return m.mottattdato(); // Brukes av autotest for å spesifisere mottatttidspunkt annet enn dagens dato
        }
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [config=" + config + "]";
    }

}
