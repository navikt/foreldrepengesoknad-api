package no.nav.foreldrepenger.selvbetjening.minidialog;

import static no.nav.foreldrepenger.common.util.StreamUtil.safeStream;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;

import no.nav.foreldrepenger.selvbetjening.historikk.HistorikkInnslag;
import no.nav.foreldrepenger.selvbetjening.historikk.MinidialogInnslag;
import no.nav.foreldrepenger.selvbetjening.http.ProtectedRestController;
import no.nav.foreldrepenger.selvbetjening.innsyn.Innsyn;
import no.nav.foreldrepenger.selvbetjening.innsyn.TilbakekrevingsInnslag;

@ProtectedRestController(MinidialogController.MINIDIALOG_PATH)
public class MinidialogController {

    private static final Logger LOG = LoggerFactory.getLogger(MinidialogController.class);
    static final String MINIDIALOG_PATH = "/rest/minidialog";

    private final MinidialogTjeneste minidialog;
    private final Innsyn innsyn;

    @Autowired
    public MinidialogController(MinidialogTjeneste minidialog, Innsyn innsyn) {
        this.minidialog = minidialog;
        this.innsyn = innsyn;
    }

    @GetMapping
    public List<MinidialogInnslag> aktive() {
        var aktiveMinidialoger = minidialog.aktive();
        sammenlign(aktiveMinidialoger);
        return aktiveMinidialoger;
    }


    private void sammenlign(List<MinidialogInnslag> fraHistorikk) {
        try {
            var saksnummreFraHistorikk = safeStream(fraHistorikk).map(HistorikkInnslag::getSaksnr).toList();
            var saksnummreFraOversikt = safeStream(innsyn.hentUttalelserOmTilbakekreving()).map(TilbakekrevingsInnslag::saksnummer).toList();

            if (saksnummreFraHistorikk.size() == saksnummreFraOversikt.size() && saksnummreFraHistorikk.containsAll(saksnummreFraOversikt) && saksnummreFraOversikt.containsAll(saksnummreFraHistorikk)) {
                LOG.info("Ingen avvik i tilbakekrevingsuttalelser mottatt fra fpoversikt sammenlignet med fpinfo-historikk");
            } else {
                LOG.info("AVVIK [tilbakekrevingsuttalelser]: Fpinfo-historikk returnerte {}, mens fpoversikt returnerte {}", saksnummreFraHistorikk, saksnummreFraOversikt);
            }
        } catch (Exception e) {
            LOG.info("Noe gikk galt med sammenligning av tilbakekrevingsuttalelser", e);
        }
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [minidialog=" + minidialog + "]";
    }
}
