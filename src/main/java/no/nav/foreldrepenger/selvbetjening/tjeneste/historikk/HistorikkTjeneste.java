package no.nav.foreldrepenger.selvbetjening.tjeneste.historikk;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class HistorikkTjeneste implements Historikk {
    private final HistorikkConnection connection;

    public HistorikkTjeneste(HistorikkConnection connection) {
        this.connection = connection;
    }

    @Override
    public List<Melding> hentMeldinger() {
        return connection.hentMeldinger();
    }

}
