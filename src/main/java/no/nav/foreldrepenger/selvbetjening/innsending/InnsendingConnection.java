package no.nav.foreldrepenger.selvbetjening.innsending;

import static java.time.LocalDateTime.now;

import java.net.URI;
import java.time.LocalDate;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestOperations;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import no.nav.foreldrepenger.selvbetjening.error.UnexpectedInputException;
import no.nav.foreldrepenger.selvbetjening.http.AbstractRestConnection;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.Engangsstønad;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.Ettersending;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.Foreldrepengesøknad;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.Kvittering;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.Svangerskapspengesøknad;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.Søknad;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.Vedlegg;
import no.nav.foreldrepenger.selvbetjening.innsending.dto.EngangsstønadDto;
import no.nav.foreldrepenger.selvbetjening.innsending.dto.EttersendingDto;
import no.nav.foreldrepenger.selvbetjening.innsending.dto.ForeldrepengesøknadDto;
import no.nav.foreldrepenger.selvbetjening.innsending.dto.SvangerskapspengesøknadDto;
import no.nav.foreldrepenger.selvbetjening.innsending.dto.SøknadDto;
import no.nav.foreldrepenger.selvbetjening.vedlegg.Image2PDFConverter;

@Component
public class InnsendingConnection extends AbstractRestConnection {

    private static final Logger LOG = LoggerFactory.getLogger(InnsendingConnection.class);

    private final InnsendingConfig config;
    private final Image2PDFConverter converter;

    @Inject
    private ObjectMapper mapper;

    public InnsendingConnection(RestOperations operations, InnsendingConfig config, Image2PDFConverter converter) {
        super(operations);
        this.config = config;
        this.converter = converter;
    }

    @Override
    public boolean isEnabled() {
        return config.isEnabled();
    }

    @Override
    public URI pingURI() {
        return config.pingURI();
    }

    public Kvittering sendInn(Søknad søknad) {
        søknad.setOpprettet(now());
        return postIfEnabled(config.innsendingURI(), body(søknad), Kvittering.class);
    }

    public Kvittering ettersend(Ettersending ettersending) {
        return postIfEnabled(config.ettersendingURI(), body(ettersending), Kvittering.class);
    }

    public Kvittering endre(Søknad søknad) {
        return postIfEnabled(config.endringURI(), body(søknad), Kvittering.class);
    }

    private <T> T postIfEnabled(URI uri, Object body, Class<T> clazz) {
        if (isEnabled()) {
            return postForObject(uri, body, clazz);
        }
        LOG.info("Innsending er ikke aktivert");
        return null;
    }

    private SøknadDto body(Søknad søknad) {
        SøknadDto dto = ytelse(søknad);
        logJSON(dto);
        dto.mottattdato = LocalDate.now();
        dto.tilleggsopplysninger = søknad.getTilleggsopplysninger();
        søknad.getVedlegg().forEach(v -> dto.addVedlegg(convert(v)));
        return dto;
    }

    private static SøknadDto ytelse(Søknad søknad) {
        if (søknad instanceof Engangsstønad e) {
            return new EngangsstønadDto(e);
        }
        if (søknad instanceof Foreldrepengesøknad f) {
            return new ForeldrepengesøknadDto(f);
        }
        if (søknad instanceof Svangerskapspengesøknad s) {
            return new SvangerskapspengesøknadDto(s);
        }
        LOG.warn("Mottok en søknad av ukjent type {}", søknad.getClass().getSimpleName());
        throw new UnexpectedInputException("Unknown application type " + søknad.getClass().getSimpleName());
    }

    private EttersendingDto body(Ettersending ettersending) {
        EttersendingDto dto = new EttersendingDto(ettersending);
        ettersending.getVedlegg().forEach(v -> dto.addVedlegg(convert(v)));
        return dto;
    }

    private Vedlegg convert(Vedlegg v) {
        Vedlegg vedlegg = v.kopi();
        if ((v.getContent() != null) && (v.getContent().length > 0)) {
            vedlegg.setContent(converter.convert(v.getContent()));
        }
        return vedlegg;
    }

    private void logJSON(SøknadDto dto) {
        try {
            LOG.trace("JSON er {}", mapper.writerWithDefaultPrettyPrinter().writeValueAsString(dto));
        } catch (JsonProcessingException e) {

        }
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [config=" + config + "]";
    }

}
