package no.nav.foreldrepenger.selvbetjening.tjeneste.historikk;

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

import no.nav.foreldrepenger.selvbetjening.tjeneste.minidialog.Minidialog;
import no.nav.foreldrepenger.selvbetjening.tjeneste.oppslag.domain.Fødselsnummer;
import no.nav.security.oidc.api.Unprotected;

@Profile({ LOCAL, DEV, DEV_GCP })
@RestController
@RequestMapping(produces = APPLICATION_JSON_VALUE)
@Unprotected
public class HistorikkDevController {

    private static final String DEVPART = "/" + DEV;

    private static final String HISTORIKKPATH = "historikk" + DEVPART;
    private static final String MINIDIALOGPATH = "minidialog" + DEVPART;

    private final Minidialog minidialog;
    private final Historikk historikk;

    @Inject
    public HistorikkDevController(Historikk historikk, Minidialog minidialog) {
        this.historikk = historikk;
        this.minidialog = minidialog;
    }

    @GetMapping(HISTORIKKPATH + "/hent")
    public List<SøknadInnslag> hentHistorikk(@RequestParam("fnr") Fødselsnummer fnr) {
        return historikk.hentHistorikkFor(fnr);
    }

    @GetMapping(MINIDIALOGPATH + "/hent")
    public List<MinidialogInnslag> hentMinidialoger(@RequestParam("fnr") Fødselsnummer fnr) {
        return minidialog.hentMinidialoger(fnr);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[minidialog=" + minidialog + ", historikk=" + historikk + "]";
    }
}
