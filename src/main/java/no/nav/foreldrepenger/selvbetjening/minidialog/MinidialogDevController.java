package no.nav.foreldrepenger.selvbetjening.minidialog;

import static no.nav.foreldrepenger.selvbetjening.minidialog.MinidialogController.MINIDIALOG;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import no.nav.foreldrepenger.selvbetjening.historikk.MinidialogInnslag;
import no.nav.foreldrepenger.selvbetjening.http.UnprotectedRestController;
import no.nav.foreldrepenger.selvbetjening.oppslag.domain.Fødselsnummer;

@UnprotectedRestController
@RequestMapping(MinidialogDevController.DEVPATH)
public class MinidialogDevController {

    static final String DEVPATH = MINIDIALOG + "/dev";

    private final Minidialog minidialog;

    public MinidialogDevController(Minidialog minidialog) {
        this.minidialog = minidialog;
    }

    @GetMapping
    public List<MinidialogInnslag> minidialoger(@RequestParam("fnr") Fødselsnummer fnr,
            @RequestParam(defaultValue = "true") boolean activeOnly) {
        return minidialog.hentMinidialoger(fnr, activeOnly);
    }

    @GetMapping("/aktive")
    public List<MinidialogInnslag> aktiveSpørsmål(@RequestParam("fnr") Fødselsnummer fnr) {
        return minidialog.hentAktiveMinidialogSpørsmål(fnr);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[minidialog=" + minidialog + "]";
    }
}
