package no.nav.foreldrepenger.selvbetjening.historikk;

import java.util.List;

import jakarta.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.validation.Valid;
import no.nav.foreldrepenger.common.domain.Saksnummer;
import no.nav.foreldrepenger.selvbetjening.http.ProtectedRestController;
import no.nav.foreldrepenger.selvbetjening.innsyn.Innsyn;

@ProtectedRestController(value = HistorikkController.HISTORIKK)
public class HistorikkController {

    private static final Logger LOG = LoggerFactory.getLogger(HistorikkController.class);
    static final String HISTORIKK = "/rest/historikk";

    private final Innsyn innsyn;

    public HistorikkController(Innsyn innsyn) {
        this.innsyn = innsyn;
    }

    @GetMapping(path = "/vedlegg")
    public List<String> vedlegg(@RequestParam("saksnummer") @Valid @NotNull Saksnummer saksnummer) {
        LOG.info("Henter manglende vedlegg for sak {}", saksnummer.value());
        var manglendeVedlegg = innsyn.hentManglendeVedlegg(saksnummer);
        LOG.info("Sak {} mangler følgende vedlegg: {}", saksnummer.value(), manglendeVedlegg);
        return manglendeVedlegg;
    }
}
