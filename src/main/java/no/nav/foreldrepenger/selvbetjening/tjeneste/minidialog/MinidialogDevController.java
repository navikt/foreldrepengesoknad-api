package no.nav.foreldrepenger.selvbetjening.tjeneste.minidialog;

import static no.nav.foreldrepenger.selvbetjening.tjeneste.minidialog.MinidialogController.MINIDIALOG;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import no.nav.foreldrepenger.selvbetjening.tjeneste.historikk.MinidialogInnslag;
import no.nav.foreldrepenger.selvbetjening.tjeneste.oppslag.domain.Fødselsnummer;
import no.nav.foreldrepenger.selvbetjening.util.ConditionalOnDev;
import no.nav.security.token.support.core.api.Unprotected;

@ConditionalOnDev
@RestController
@RequestMapping(path = MinidialogDevController.DEVPATH, produces = APPLICATION_JSON_VALUE)
@Unprotected
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
