package no.nav.foreldrepenger.selvbetjening.innsending;

import static no.nav.boot.conditionals.EnvUtil.isGcp;
import static no.nav.foreldrepenger.common.domain.felles.InnsendingsType.LASTET_OPP;
import static no.nav.foreldrepenger.common.util.StreamUtil.safeStream;
import static no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.mapper.EttersendingMapper.tilEttersending;
import static no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.mapper.SøknadMapper.tilEndringssøknad;
import static no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.mapper.SøknadMapper.tilSøknad;
import static no.nav.foreldrepenger.selvbetjening.util.StringUtils.escapeHtml;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_PDF;
import static org.springframework.http.MediaType.MULTIPART_MIXED;

import java.net.URI;
import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestOperations;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import no.nav.foreldrepenger.common.domain.Kvittering;
import no.nav.foreldrepenger.common.domain.Søknad;
import no.nav.foreldrepenger.common.domain.felles.Ettersending;
import no.nav.foreldrepenger.selvbetjening.http.AbstractRestConnection;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.MottattTidspunkt;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.SøknadDto;
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

    public Kvittering sendInnViaMultipart(SøknadDto søknad) throws JsonProcessingException {
        LOG.info("Sender inn søknad via multipart body {}", søknad.type());
        vedleggshåndtering.fjernDupliserteVedleggFraSøknad(søknad);

        var mulitpartBuilder = new MultipartBodyBuilder();
        mulitpartBuilder.part(BODY_PART_NAME, mapper.writeValueAsString(tilSøknad(søknad, mottattDato(søknad))), APPLICATION_JSON);
        safeStream(søknad.vedlegg())
            .filter(s -> LASTET_OPP.name().equals(s.getInnsendingsType()))
            .forEach(v -> mulitpartBuilder.part(VEDLEGG_PART_NAME, v.getContent(), APPLICATION_PDF)
                    .headers(headers -> headers.set(VEDLEGG_REFERANSE_HEADER, v.getId().referanse())));
        return postForEntity(config.innsendingV2URI(),  new HttpEntity<>(mulitpartBuilder.build(), headers()), Kvittering.class);
    }

    private static HttpHeaders headers() {
        var headers = new HttpHeaders();
        headers.setContentType(MULTIPART_MIXED);
        return headers;
    }


    public Kvittering sendInn(SøknadV2Dto søknad) {
        return post(config.innsendingURI(), body(søknad));
    }

    public Kvittering ettersend(EttersendelseDto ettersending) {
        return post(config.ettersendingURI(), body(ettersending));
    }

    public Kvittering endre(EndringssøknadDto endringssøknad) {
        return post(config.endringURI(), body(endringssøknad));
    }

    private Kvittering post(URI uri, Object body) {
        return postForObject(uri, body, Kvittering.class);
    }

    public Søknad body(SøknadV2Dto søknad) {
        SECURE_LOGGER.info("Engangsstønad mottatt fra frontend med følende innhold: {}", escapeHtml(søknad));
        vedleggshåndtering.fjernDupliserteVedleggFraSøknad(søknad);
        return tilSøknad(søknad, mottattDato(søknad));
    }

    public Søknad body(EndringssøknadDto endringssøknad) {
        SECURE_LOGGER.info("{} mottatt fra frontend med følende innhold: {}", endringssøknad.type(), escapeHtml(endringssøknad));
        vedleggshåndtering.fjernDupliserteVedleggFraSøknad(endringssøknad);
        return tilEndringssøknad(endringssøknad, mottattDato(endringssøknad));
    }

    public Ettersending body(EttersendelseDto ettersending) {
        vedleggshåndtering.fjernDupliserteVedleggFraEttersending(ettersending);
        return tilEttersending(ettersending);
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
