package no.nav.foreldrepenger.selvbetjening.innsending;

import static java.time.LocalDateTime.now;
import static org.slf4j.LoggerFactory.getLogger;

import javax.inject.Inject;
import javax.ws.rs.BadRequestException;

import org.slf4j.Logger;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import no.nav.foreldrepenger.selvbetjening.innsending.domain.Engangsstønad;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.Ettersending;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.Foreldrepengesøknad;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.Kvittering;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.Svangerskapspengesøknad;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.Søknad;
import no.nav.foreldrepenger.selvbetjening.innsending.dto.EngangsstønadDto;
import no.nav.foreldrepenger.selvbetjening.innsending.dto.EttersendingDto;
import no.nav.foreldrepenger.selvbetjening.innsending.dto.ForeldrepengesøknadDto;
import no.nav.foreldrepenger.selvbetjening.innsending.dto.SvangerskapspengesøknadDto;
import no.nav.foreldrepenger.selvbetjening.innsending.dto.SøknadDto;

@Service
@ConditionalOnProperty(name = "stub.mottak", havingValue = "true")
public class InnsendingTjenesteStub implements Innsending {

    public static final Kvittering STUB = new Kvittering(now(), "12345", new byte[0], new byte[0]);

    private static final Logger LOG = getLogger(InnsendingTjenesteStub.class);

    @Inject
    private ObjectMapper mapper;

    @Override
    public Kvittering sendInn(Søknad søknad) {
        søknad.setOpprettet(now());
        return postStub(søknad);
    }

    @Override
    public Kvittering ettersend(Ettersending ettersending) {
        return postStub(ettersending);
    }

    @Override
    public Kvittering endre(Søknad søknad) {
        return postStub(søknad);
    }

    private Kvittering postStub(Søknad søknad) {
        SøknadDto dto = switch (søknad) {
            case Engangsstønad engangsstønad -> new EngangsstønadDto(engangsstønad);
            case Foreldrepengesøknad foreldrepengesøknad -> new ForeldrepengesøknadDto(foreldrepengesøknad);
            case Svangerskapspengesøknad svangerskapspengesøknad -> new SvangerskapspengesøknadDto(svangerskapspengesøknad);
            case null, default -> throw new BadRequestException("Unknown application type");
        };

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
        return STUB;
    }

    private Kvittering postStub(Ettersending ettersending) {
        EttersendingDto dto = new EttersendingDto(ettersending);
        ettersending.getVedlegg().forEach(v -> v.setContent(new byte[] {}));

        try {
            LOG.info("Posting JSON (stub): {}", mapper.writeValueAsString(dto));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return STUB;
    }

    @Override
    public String ping() {
        return "Hello earthlings";
    }

}
