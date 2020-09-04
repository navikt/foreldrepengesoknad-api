package no.nav.foreldrepenger.selvbetjening.historikk;

import static no.nav.foreldrepenger.selvbetjening.historikk.HistorikkController.HISTORIKK;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import no.nav.foreldrepenger.selvbetjening.http.UnprotectedRestController;
import no.nav.foreldrepenger.selvbetjening.oppslag.domain.Fødselsnummer;

@UnprotectedRestController(HistorikkDevController.DEVPATH)
public class HistorikkDevController {

    static final String DEVPATH = HISTORIKK + "/dev";

    private final Historikk historikk;

    @Autowired
    private DiscoveryClient discoveryClient;

    public HistorikkDevController(Historikk historikk) {
        this.historikk = historikk;
    }

    @GetMapping
    public List<HistorikkInnslag> historikk(@RequestParam("fnr") Fødselsnummer fnr) {
        return historikk.historikkFor(fnr);
    }

    @GetMapping(path = "/discovery")
    public List<String> discovery() {
        return discoveryClient.getServices();
    }

    @GetMapping(path = "/vedlegg")
    public List<String> vedlegg(@RequestParam("fnr") Fødselsnummer fnr, @RequestParam("saksnummer") String saksnummer) {
        return historikk.manglendeVedleggFor(fnr, saksnummer);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[historikk=" + historikk + "]";
    }
}
