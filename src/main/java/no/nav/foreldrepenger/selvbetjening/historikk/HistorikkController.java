package no.nav.foreldrepenger.selvbetjening.historikk;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import no.nav.foreldrepenger.common.domain.Saksnummer;
import no.nav.foreldrepenger.selvbetjening.http.ProtectedRestController;

@ProtectedRestController(value = HistorikkController.HISTORIKK)
public class HistorikkController {

    static final String HISTORIKK = "/rest/historikk";

    private final Historikk historikk;

    public HistorikkController(Historikk historikk) {
        this.historikk = historikk;
    }

    @GetMapping
    public List<HistorikkInnslag> historikk() {
        return historikk.historikk();
    }

    @GetMapping(path = "/vedlegg")
    public List<String> vedlegg(@Valid @RequestParam("saksnummer") Saksnummer saksnummer) {
        return historikk.manglendeVedlegg(saksnummer);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [historikk=" + historikk + "]";
    }
}
