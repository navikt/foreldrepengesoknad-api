package no.nav.foreldrepenger.selvbetjening.tjeneste.innsending;

import static java.time.LocalDateTime.now;

import java.net.URI;
import java.time.LocalDate;

import javax.inject.Inject;
import javax.ws.rs.BadRequestException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestOperations;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import no.nav.foreldrepenger.selvbetjening.tjeneste.AbstractRestConnection;
import no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.domain.Engangsstønad;
import no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.domain.Ettersending;
import no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.domain.Foreldrepengesøknad;
import no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.domain.Kvittering;
import no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.domain.Svangerskapspengesøknad;
import no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.domain.Søknad;
import no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.domain.Vedlegg;
import no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.dto.EngangsstønadDto;
import no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.dto.EttersendingDto;
import no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.dto.ForeldrepengesøknadDto;
import no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.dto.SvangerskapspengesøknadDto;
import no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.dto.SøknadDto;
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
        SøknadDto dto;
        if (søknad instanceof Engangsstønad) {
            dto = new EngangsstønadDto((Engangsstønad) søknad);
            logJSON(dto);
        } else if (søknad instanceof Foreldrepengesøknad) {
            dto = new ForeldrepengesøknadDto((Foreldrepengesøknad) søknad);
            logJSON(dto);
        } else if (søknad instanceof Svangerskapspengesøknad) {
            dto = new SvangerskapspengesøknadDto((Svangerskapspengesøknad) søknad);
            logJSON(dto);
        } else {
            LOG.warn("Mottok en søknad av ukjent type {}", søknad.getClass().getSimpleName());
            throw new BadRequestException("Unknown application type " + søknad.getClass().getSimpleName());
        }
        dto.mottattdato = LocalDate.now();
        dto.tilleggsopplysninger = søknad.getTilleggsopplysninger();
        søknad.getVedlegg().forEach(v -> dto.addVedlegg(convert(v)));
        return dto;
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
