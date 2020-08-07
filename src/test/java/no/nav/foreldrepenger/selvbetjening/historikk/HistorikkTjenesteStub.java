package no.nav.foreldrepenger.selvbetjening.historikk;

import java.util.Collections;
import java.util.List;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import no.nav.foreldrepenger.selvbetjening.oppslag.domain.Fødselsnummer;

@Service
@ConditionalOnProperty(name = "stub.historikk", havingValue = "true")
public class HistorikkTjenesteStub implements Historikk {

    @Override
    public String ping() {
        return "Hello earthlings";
    }

    @Override
    public List<HistorikkInnslag> historikk() {
        return Collections.emptyList();
    }

    @Override
    public List<HistorikkInnslag> historikkFor(Fødselsnummer fnr) {
        return Collections.emptyList();
    }

    @Override
    public List<String> manglendeVedlegg(String saksnr) {
        return Collections.emptyList();
    }

    @Override
    public List<String> manglendeVedleggFor(Fødselsnummer fnr, String saksnr) {
        return Collections.emptyList();
    }

}