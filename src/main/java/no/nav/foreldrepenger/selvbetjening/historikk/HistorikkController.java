package no.nav.foreldrepenger.selvbetjening.historikk;

import static no.nav.boot.conditionals.EnvUtil.isDevOrLocal;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
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

    private final HistorikkTjeneste historikk;
    private final Innsyn innsyn;
    private final Environment env;

    public HistorikkController(HistorikkTjeneste historikk, Innsyn innsyn, Environment env) {
        this.historikk = historikk;
        this.innsyn = innsyn;
        this.env = env;
    }

    @GetMapping
    public List<HistorikkInnslag> historikk() {
        return historikk.historikk();
    }

    @GetMapping(path = "/vedlegg")
    public List<String> vedlegg(@Valid @RequestParam("saksnummer") Saksnummer saksnummer) {
        var manglendeVedleggFraFpinfoHistorikk = historikk.manglendeVedlegg(saksnummer);
        if (isDevOrLocal(env)) {
            sammenlign(saksnummer, manglendeVedleggFraFpinfoHistorikk);
        }
        return manglendeVedleggFraFpinfoHistorikk;
    }

    private void sammenlign(Saksnummer saksnummer, List<String> fraHistorikk) {
        try {
            var fraFpoversikt = innsyn.hentManglendeVedlegg(saksnummer);
            if (fraHistorikk.size() == fraFpoversikt.size() && fraHistorikk.containsAll(fraFpoversikt) && fraFpoversikt.containsAll(fraHistorikk)) {
                LOG.info("Ingen avvik i manglende vedlegg mottatt fra fpoversikt sammenlignet med fpinfo-historikk");
            } else {
                LOG.info("AVVIK[manglende vedlegg]: Fpinfo-historikk returnerte {}, mens fpoversikt returnerte {} på saksnummer {}", fraHistorikk, fraFpoversikt, saksnummer.value());
            }
        } catch (Exception e) {
            LOG.info("Noe gikk galt med sammenligning av manglende vedlegg", e);
        }
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [historikk=" + historikk + "]";
    }
}
