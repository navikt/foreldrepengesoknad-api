package no.nav.foreldrepenger.selvbetjening.tjeneste.oppslag;

import static no.nav.foreldrepenger.selvbetjening.util.Constants.ISSUER;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import no.nav.foreldrepenger.selvbetjening.tjeneste.historikk.Historikk;
import no.nav.foreldrepenger.selvbetjening.tjeneste.historikk.HistorikkInnslag;
import no.nav.foreldrepenger.selvbetjening.tjeneste.minidialog.Minidialog;
import no.nav.foreldrepenger.selvbetjening.tjeneste.minidialog.MinidialogInnslag;
import no.nav.foreldrepenger.selvbetjening.tjeneste.oppslag.domain.Person;
import no.nav.foreldrepenger.selvbetjening.tjeneste.oppslag.domain.Søkerinfo;
import no.nav.foreldrepenger.selvbetjening.util.EnvUtil;
import no.nav.security.oidc.api.ProtectedWithClaims;

@RestController
@RequestMapping(path = OppslagController.OPPSLAG, produces = APPLICATION_JSON_VALUE)
@ProtectedWithClaims(issuer = ISSUER, claimMap = { "acr=Level4" })
public class OppslagController implements EnvironmentAware {

    private static final Logger LOG = LoggerFactory.getLogger(OppslagController.class);
    public static final String OPPSLAG = "/rest";

    private final Oppslag oppslag;
    private final Historikk historikk;
    private final Minidialog minidialog;

    private Environment env;

    @Inject
    public OppslagController(Oppslag oppslag, Historikk historikk, Minidialog minidialog) {
        this.oppslag = oppslag;
        this.historikk = historikk;
        this.minidialog = minidialog;
    }

    @GetMapping("/personinfo")
    public Person personinfo() {
        return oppslag.hentPerson();
    }

    @GetMapping("/sokerinfo")
    public Søkerinfo søkerinfo() {
        Søkerinfo info = oppslag.hentSøkerinfo();
        if (EnvUtil.isPreprod(env)) {
            hentHistorikk();
            hentMinidialoger();
        }
        return info;
    }

    private void hentHistorikk() {
        try {
            LOG.info("Henter noen historikkinnslag");
            List<HistorikkInnslag> historikkinnslag = historikk.hentHistorikk();
            LOG.info("Hentet historikkinnslag {}", historikkinnslag);
        } catch (Exception e) {
            LOG.info("Kunne ikke hente historikkinnslag", e);
        }
    }

    private void hentMinidialoger() {
        try {
            LOG.info("Henter noen minidialoger");
            List<MinidialogInnslag> dialoger = minidialog.hentMinidialoger();
            LOG.info("Hentet minidialoger {}", dialoger);
        } catch (Exception e) {
            LOG.info("Kunne ikke hente dialoger", e);
        }
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [oppslag=" + oppslag + "]";
    }

    @Override
    public void setEnvironment(Environment env) {
        this.env = env;

    }

}
