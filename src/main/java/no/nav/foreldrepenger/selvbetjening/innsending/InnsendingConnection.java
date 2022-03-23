package no.nav.foreldrepenger.selvbetjening.innsending;

import static java.time.LocalDateTime.now;
import static no.nav.foreldrepenger.selvbetjening.innsending.mapper.EttersendingMapper.tilEttersending;
import static no.nav.foreldrepenger.selvbetjening.innsending.mapper.EttersendingMapper.tilVedlegg;
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

import no.nav.foreldrepenger.common.domain.Kvittering;
import no.nav.foreldrepenger.common.domain.Søknad;
import no.nav.foreldrepenger.selvbetjening.http.AbstractRestConnection;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.EttersendingFrontend;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.SøknadFrontend;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.VedleggFrontend;
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

    public Kvittering sendInn(SøknadFrontend søknad) {
        søknad.setOpprettet(now());
        return postIfEnabled(config.innsendingURI(), body(søknad));
    }

    public Kvittering ettersend(EttersendingFrontend ettersending) {
        return postIfEnabled(config.ettersendingURI(), body(ettersending));
    }

    public Kvittering endre(SøknadFrontend søknad) {
        return postIfEnabled(config.endringURI(), body(søknad));
    }

    private Kvittering postIfEnabled(URI uri, Object body) {
        if (isEnabled()) {
            return postForObject(uri, body, Kvittering.class);
        }
        LOG.info("Innsending er ikke aktivert");
        return null;
    }

    public Søknad body(SøknadFrontend søknad) {
        var dto = tilSøknad(søknad);
        logJSON(dto);
        dto.setMottattdato(LocalDate.now());
        dto.setTilleggsopplysninger(søknad.getTilleggsopplysninger());
        LOG.trace("{} vedlegg", søknad.getVedlegg().size());
        søknad.getVedlegg().forEach(v -> dto.getVedlegg().add(SøknadMapper.tilVedlegg(convert(v))));
        return dto;
    }

    public no.nav.foreldrepenger.common.domain.felles.Ettersending body(EttersendingFrontend ettersending) {
        var dto = tilEttersending(ettersending);
        ettersending.vedlegg().forEach(v -> dto.getVedlegg().add(tilVedlegg(convert(v))));
        return dto;
    }

    private VedleggFrontend convert(VedleggFrontend v) {
        VedleggFrontend vedlegg = v.kopi();
        if ((v.getContent() != null) && (v.getContent().length > 0)) {
            vedlegg.setContent(converter.convert(v.getContent()));
        }
        return vedlegg;
    }

    private void logJSON(no.nav.foreldrepenger.common.domain.Søknad søknad) {
        try {
            var seralizedSøknad = serialize(søknad);
            LOG.trace("JSON er {}", seralizedSøknad);
        } catch (JsonProcessingException e) {
            LOG.trace("Klarte ikke å seralisere søknad! Vil feile ved innsending mot mottak!");
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
