package no.nav.foreldrepenger.selvbetjening.tjeneste.innsending;

import static java.time.LocalDateTime.now;

import java.net.URI;

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
import no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.domain.Søknad;
import no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.domain.Vedlegg;
import no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.dto.EngangsstønadDto;
import no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.dto.EttersendingDto;
import no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.dto.ForeldrepengesøknadDto;
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
    protected URI pingURI() {
        return config.getPingURI();
    }

    public Kvittering sendInn(Søknad søknad) {
        søknad.opprettet = now();
        return postForObject(config.getInnsendingURI(), body(søknad), Kvittering.class);
    }

    public Kvittering ettersend(Ettersending ettersending) {
        return postForObject(config.getEttersendingURI(), body(ettersending), Kvittering.class);
    }

    public Kvittering ettersendForEngangsstonad(Ettersending ettersending) {
        return postForObject(config.getEngangsstonadEttersendingURI(), body(ettersending), Kvittering.class);
    }

    public Kvittering endre(Søknad søknad) {
        return postForObject(config.getEndringURI(), body(søknad), Kvittering.class);
    }

    private SøknadDto body(Søknad søknad) {
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
        dto.tilleggsopplysninger = søknad.tilleggsopplysninger;
        søknad.vedlegg.forEach(v -> dto.addVedlegg(convert(v)));
        return dto;
    }

    private EttersendingDto body(Ettersending ettersending) {
        EttersendingDto dto = new EttersendingDto(ettersending);
        ettersending.vedlegg.forEach(v -> dto.addVedlegg(convert(v)));
        return dto;
    }

    private Vedlegg convert(Vedlegg v) {
        Vedlegg vedlegg = v.kopi();
        if (v.content != null && v.content.length > 0) {
            vedlegg.content = converter.convert(v.content);
        }
        return vedlegg;
    }

    private void logJSON(SøknadDto dto) {
        try {
            LOG.trace("JSON er {}", mapper.writerWithDefaultPrettyPrinter().writeValueAsString(dto));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [config=" + config + "]";
    }

}
