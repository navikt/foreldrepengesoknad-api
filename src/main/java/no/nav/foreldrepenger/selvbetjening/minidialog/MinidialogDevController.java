package no.nav.foreldrepenger.selvbetjening.minidialog;

import static no.nav.foreldrepenger.common.util.Constants.FNR;
import static no.nav.foreldrepenger.selvbetjening.minidialog.MinidialogController.MINIDIALOG_PATH;

import java.util.List;

import javax.validation.Valid;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import no.nav.foreldrepenger.common.domain.Fødselsnummer;
import no.nav.foreldrepenger.selvbetjening.historikk.MinidialogInnslag;
import no.nav.foreldrepenger.selvbetjening.http.UnprotectedRestController;

@Validated
@UnprotectedRestController(MinidialogDevController.DEVPATH)
public class MinidialogDevController {

    static final String DEVPATH = MINIDIALOG_PATH + "/dev";

    private final Minidialog minidialog;

    public MinidialogDevController(Minidialog minidialog) {
        this.minidialog = minidialog;
    }

    @GetMapping
    public List<MinidialogInnslag> minidialoger(@Valid @RequestParam(FNR) Fødselsnummer fnr,
                                                @RequestParam(defaultValue = "true") boolean activeOnly) {
        return minidialog.hentMinidialoger(fnr, activeOnly);
    }

    @GetMapping("/aktive")
    public List<MinidialogInnslag> aktiveSpørsmål(@RequestParam(FNR) Fødselsnummer fnr) {
        return minidialog.hentAktiveMinidialogSpørsmål(fnr);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[minidialog=" + minidialog + "]";
    }
}
