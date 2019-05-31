package no.nav.foreldrepenger.selvbetjening.tjeneste.historikk;

import java.net.URI;
import java.util.Collections;
import java.util.List;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(name = "stub.historikk", havingValue = "true")
public class HistorikkTjenesteStub implements Historikk {

    @Override
    public String ping() {
        return "Hello earthlings";
    }

    @Override
    public URI pingURI() {
        return URI.create("http://www.db.no");
    }

    @Override
    public List<Melding> hentMeldinger() {
        return Collections.emptyList();
    }
}