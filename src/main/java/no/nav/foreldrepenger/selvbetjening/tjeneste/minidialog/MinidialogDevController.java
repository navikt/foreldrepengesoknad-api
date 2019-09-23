package no.nav.foreldrepenger.selvbetjening.tjeneste.minidialog;

import static no.nav.foreldrepenger.selvbetjening.util.EnvUtil.DEV;
import static no.nav.foreldrepenger.selvbetjening.util.EnvUtil.DEV_GCP;
import static no.nav.foreldrepenger.selvbetjening.util.EnvUtil.LOCAL;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.List;

import javax.inject.Inject;

import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import no.nav.foreldrepenger.selvbetjening.tjeneste.historikk.MinidialogInnslag;
import no.nav.foreldrepenger.selvbetjening.tjeneste.oppslag.domain.Fødselsnummer;
import no.nav.security.oidc.api.Unprotected;

@Profile({ LOCAL, DEV, DEV_GCP })
@RestController
@RequestMapping(produces = APPLICATION_JSON_VALUE)
@Unprotected
public class MinidialogDevController {

    private static final String MINIDIALOGPATH = "/rest/minidialog" + "/" + DEV;

    private final Minidialog minidialog;

    @Inject
    public MinidialogDevController(Minidialog minidialog) {
        this.minidialog = minidialog;
    }

    @GetMapping(MINIDIALOGPATH)
    public List<MinidialogInnslag> minidialoger(@RequestParam("fnr") Fødselsnummer fnr,
            @RequestParam(defaultValue = "true") boolean activeOnly) {
        return minidialog.hentMinidialoger(fnr, activeOnly);
    }

    @GetMapping(MINIDIALOGPATH + "/aktive")
    public List<MinidialogInnslag> aktiveSpørsmål(@RequestParam("fnr") Fødselsnummer fnr) {
        return minidialog.hentAktiveMinidialogSpørsmål(fnr);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[minidialog=" + minidialog + "]";
    }
}
