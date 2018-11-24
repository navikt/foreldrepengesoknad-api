package no.nav.foreldrepenger.selvbetjening.innsending.tjeneste;

import static java.time.LocalDateTime.now;
import static org.slf4j.LoggerFactory.getLogger;

import javax.inject.Inject;
import javax.ws.rs.BadRequestException;

import org.slf4j.Logger;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import no.nav.foreldrepenger.selvbetjening.innsending.json.Engangsstønad;
import no.nav.foreldrepenger.selvbetjening.innsending.json.Ettersending;
import no.nav.foreldrepenger.selvbetjening.innsending.json.Foreldrepengesøknad;
import no.nav.foreldrepenger.selvbetjening.innsending.json.Kvittering;
import no.nav.foreldrepenger.selvbetjening.innsending.json.Søknad;
import no.nav.foreldrepenger.selvbetjening.innsending.tjeneste.json.EngangsstønadDto;
import no.nav.foreldrepenger.selvbetjening.innsending.tjeneste.json.EttersendingDto;
import no.nav.foreldrepenger.selvbetjening.innsending.tjeneste.json.ForeldrepengesøknadDto;
import no.nav.foreldrepenger.selvbetjening.innsending.tjeneste.json.SøknadDto;

@Service
@ConditionalOnProperty(name = "stub.mottak", havingValue = "true")
public class InnsendingstjenesteStub implements Innsending {

    private static final Logger LOG = getLogger(InnsendingstjenesteStub.class);

    @Inject
    private ObjectMapper mapper;

    @Override
    public Kvittering sendInn(Søknad søknad) {
        søknad.opprettet = now();
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
        else {
            throw new BadRequestException("Unknown application type");
        }

        søknad.vedlegg.forEach(v -> {
            v.content = new byte[] {};
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
        ettersending.vedlegg.forEach(v -> v.content = new byte[] {});

        try {
            LOG.info("Posting JSON (stub): {}", mapper.writeValueAsString(dto));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return Kvittering.STUB;
    }
}