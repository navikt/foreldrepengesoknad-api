package no.nav.foreldrepenger.selvbetjening.historikk;

import static no.nav.foreldrepenger.common.domain.validation.InputValideringRegex.FRITEKST;

import java.util.List;

import javax.validation.constraints.Pattern;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import no.nav.foreldrepenger.selvbetjening.http.ProtectedRestController;

@Validated
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
    public List<String> vedlegg(@RequestParam("saksnummer") @Pattern(regexp = FRITEKST) String saksnummer) {
        return historikk.manglendeVedlegg(saksnummer);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [historikk=" + historikk + "]";
    }
}
