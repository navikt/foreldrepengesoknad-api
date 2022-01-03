package no.nav.foreldrepenger.selvbetjening.innsending;

import static java.time.LocalDateTime.now;
import static no.nav.foreldrepenger.selvbetjening.innsending.mapper.EttersendingMapper.tilEttersending;
import static no.nav.foreldrepenger.selvbetjening.innsending.mapper.SøknadMapper.tilSøknad;
import static no.nav.foreldrepenger.selvbetjening.innsending.mapper.SøknadMapper.tilVedlegg;
import static org.slf4j.LoggerFactory.getLogger;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import no.nav.foreldrepenger.selvbetjening.innsending.domain.Ettersending;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.Kvittering;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.Søknad;

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
        var dto = tilSøknad(søknad);
        dto.setTilleggsopplysninger(søknad.getTilleggsopplysninger());
        søknad.getVedlegg().forEach(v -> {
            v.setContent(new byte[] {});
            dto.getVedlegg().add(tilVedlegg(v));
        });

        try {
            LOG.info("Posting JSON (stub): {}", mapper.writeValueAsString(dto));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return STUB;
    }

    private Kvittering postStub(Ettersending ettersending) {
        var dto = tilEttersending(ettersending);

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
