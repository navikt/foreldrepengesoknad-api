package no.nav.foreldrepenger.selvbetjening.tjeneste.historikk;

import java.util.Collections;
import java.util.List;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import no.nav.foreldrepenger.selvbetjening.tjeneste.oppslag.domain.Fødselsnummer;

@Service
@ConditionalOnProperty(name = "stub.historikk", havingValue = "true")
public class HistorikkTjenesteStub implements Historikk {

    @Override
    public String ping() {
        return "Hello earthlings";
    }

    @Override
    public List<HistorikkInnslag> hentHistorikk() {
        return Collections.emptyList();
    }

    @Override
    public List<HistorikkInnslag> hentHistorikkFor(Fødselsnummer fnr) {
        return Collections.emptyList();
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}