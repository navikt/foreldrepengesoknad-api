package no.nav.foreldrepenger.selvbetjening.innsending;

import static java.time.LocalDateTime.now;
import static no.nav.foreldrepenger.selvbetjening.innsending.mapper.CommonMapper.tilVedlegg;
import static no.nav.foreldrepenger.selvbetjening.innsending.mapper.EttersendingMapper.tilEttersending;
import static no.nav.foreldrepenger.selvbetjening.innsending.mapper.SøknadMapper.tilSøknad;
import static org.slf4j.LoggerFactory.getLogger;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import no.nav.foreldrepenger.common.domain.Kvittering;
import no.nav.foreldrepenger.common.domain.Saksnummer;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.EttersendingFrontend;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.SøknadFrontend;

@Service
@ConditionalOnProperty(name = "stub.mottak", havingValue = "true")
public class InnsendingTjenesteStub implements Innsending {

    public static final Kvittering STUB = new Kvittering(now(), Saksnummer.valueOf("12345"), new byte[0], new byte[0]);

    private static final Logger LOG = getLogger(InnsendingTjenesteStub.class);

    @Inject
    private ObjectMapper mapper;

    @Override
    public Kvittering sendInn(SøknadFrontend søknad) {
        søknad.setOpprettet(now());
        return postStub(søknad);
    }

    @Override
    public Kvittering ettersend(EttersendingFrontend ettersending) {
        return postStub(ettersending);
    }

    @Override
    public Kvittering endre(SøknadFrontend søknad) {
        return postStub(søknad);
    }

    // TODO: Fiks/fjern denne
    private Kvittering postStub(SøknadFrontend søknad) {
        var dto = tilSøknad(søknad);
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

    private Kvittering postStub(EttersendingFrontend ettersending) {
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
