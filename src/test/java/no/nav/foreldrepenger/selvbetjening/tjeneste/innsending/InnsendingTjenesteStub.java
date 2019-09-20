package no.nav.foreldrepenger.selvbetjening.tjeneste.innsending;

import static java.time.LocalDateTime.now;
import static org.slf4j.LoggerFactory.getLogger;

import java.net.URI;

import javax.inject.Inject;
import javax.ws.rs.BadRequestException;

import no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.domain.*;
import no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.dto.*;
import org.slf4j.Logger;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
@ConditionalOnProperty(name = "stub.mottak", havingValue = "true")
public class InnsendingTjenesteStub implements Innsending {

    private static final Logger LOG = getLogger(InnsendingTjenesteStub.class);

    @Inject
    private ObjectMapper mapper;

    @Override
    public Kvittering sendInn(Søknad søknad) {
        søknad.setOpprettet(now());
        return postStub(søknad);
    }

    @Override
    public Kvittering sendInn(Ettersending ettersending) {
        return postStub(ettersending);
    }

    @Override
    public Kvittering endre(Søknad søknad) {
        return postStub(søknad);
    }

    private Kvittering postStub(Søknad søknad) {
        SøknadDto dto;
        if (søknad instanceof Engangsstønad) {
            dto = new EngangsstønadDto((Engangsstønad) søknad);
        }
        else if (søknad instanceof Foreldrepengesøknad) {
            dto = new ForeldrepengesøknadDto((Foreldrepengesøknad) søknad);
        }
        else if (søknad instanceof Svangerskapspengesøknad) {
            dto = new SvangerskapspengesøknadDto((Svangerskapspengesøknad) søknad);
        }
        else {
            throw new BadRequestException("Unknown application type");
        }

        dto.tilleggsopplysninger = søknad.getTilleggsopplysninger();
        søknad.getVedlegg().forEach(v -> {
            v.setContent(new byte[] {});
            dto.addVedlegg(v);
        });

        try {
            LOG.info("Posting JSON (stub): {}", mapper.writeValueAsString(dto));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return Kvittering.STUB;
    }

    private Kvittering postStub(Ettersending ettersending) {
        EttersendingDto dto = new EttersendingDto(ettersending);
        ettersending.getVedlegg().forEach(v -> v.setContent(new byte[] {}));

        try {
            LOG.info("Posting JSON (stub): {}", mapper.writeValueAsString(dto));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return Kvittering.STUB;
    }

    @Override
    public String ping() {
        return "Hello earthlings";
    }

    @Override
    public URI pingURI() {
        return URI.create("http://www.vg.no");
    }
}