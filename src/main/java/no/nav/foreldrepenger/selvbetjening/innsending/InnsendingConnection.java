package no.nav.foreldrepenger.selvbetjening.innsending;

import static java.time.LocalDateTime.now;
import static no.nav.foreldrepenger.selvbetjening.innsending.mapper.EttersendingMapper.tilEttersending;
import static no.nav.foreldrepenger.selvbetjening.innsending.mapper.SøknadMapper.tilSøknad;

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
import no.nav.foreldrepenger.selvbetjening.innsending.mapper.EttersendingMapper;
import no.nav.foreldrepenger.selvbetjening.innsending.mapper.SøknadMapper;
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

    public SøknadDto body(Søknad søknad) {
        var dtoGammel = tilSøknadDto(søknad);
        sammenlignNySøknadMedGammel(søknad);
        return dtoGammel;
    }

    private void sammenlignNySøknadMedGammel(Søknad søknad) {
        try {
            var dtoGammel = tilSøknadDto(søknad); // Nytt instans her for å forsikre oss om at vi ikke endrer dto før innsending
            var søknadGammel = mapper.readValue(serialize(dtoGammel), no.nav.foreldrepenger.common.domain.Søknad.class);

            var dtoNY = tilSøknadDtoNY(søknad);
            var søknadNY = mapper.readValue(serialize(dtoNY), no.nav.foreldrepenger.common.domain.Søknad.class);

            if (søknadGammel.equals(søknadNY)) {
                LOG.trace("Søknad er lik ");
            } else {
                LOG.trace("FEIL: Ulikheter mellom ny og gammel mappet søknadsobjet under innsending: \nNY: {}\nGAMMEL: {}",
                    søknadNY, søknadGammel);
                LOG.trace("FEIL: Opprinnelig innsendt søknadsobjekt: \n{}", søknad);
            }
        } catch (Exception e) {
            LOG.trace("Uventet feil: Noe gikk feil under seralisering eller deseralisering av en av DTOene", e);
        }
    }


    public SøknadDto tilSøknadDto(Søknad søknad) {
        SøknadDto dto = ytelse(søknad);
        logJSON(dto);
        dto.mottattdato = LocalDate.now();
        dto.tilleggsopplysninger = søknad.getTilleggsopplysninger();
        LOG.trace("{} vedlegg {}", søknad.getVedlegg().size(), søknad.getVedlegg());
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

    public no.nav.foreldrepenger.common.domain.Søknad tilSøknadDtoNY(Søknad søknad) {
        var dto = tilSøknad(søknad);
        logJSON(dto);
        dto.setMottattdato(LocalDate.now());
        dto.setTilleggsopplysninger(søknad.getTilleggsopplysninger());
        LOG.trace("{} vedlegg {}", søknad.getVedlegg().size(), søknad.getVedlegg());
        søknad.getVedlegg().forEach(v -> SøknadMapper.leggTilVedlegg(dto, convert(v)));
        return dto;
    }

    // TODO: Implementer etter søknad
    private no.nav.foreldrepenger.common.domain.felles.Ettersending tilEttersendingNY(Ettersending ettersending) {
        var dto = tilEttersending(ettersending);
        ettersending.getVedlegg().forEach(v -> EttersendingMapper.leggTilVedlegg(dto, convert(v)));
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
            LOG.trace("GAMMEL JSON er {}", serialize(dto));
        } catch (JsonProcessingException e) {
            LOG.trace("Klarte ikke å seralisere gammel søknad for logging!");
        }
    }

    private void logJSON(no.nav.foreldrepenger.common.domain.Søknad søknad) {
        try {
            LOG.trace("NY JSON er {}", serialize(søknad));
        } catch (JsonProcessingException e) {
            LOG.trace("Klarte ikke å seralisere ny søknad ved logging!");
        }
    }

    private String serialize(Object obj) throws JsonProcessingException {
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
    }


    @Override
    public String toString() {
        return getClass().getSimpleName() + " [config=" + config + "]";
    }

}
