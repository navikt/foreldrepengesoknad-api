package no.nav.foreldrepenger.selvbetjening.innsyn;

import java.util.List;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import no.nav.foreldrepenger.selvbetjening.http.ProtectedRestController;
import no.nav.foreldrepenger.selvbetjening.innsyn.saker.Sak;
import no.nav.foreldrepenger.selvbetjening.innsyn.sakerv2.Saker;
import no.nav.foreldrepenger.selvbetjening.innsyn.uttaksplan.Uttaksplan;
import no.nav.foreldrepenger.selvbetjening.innsyn.vedtak.Vedtak;

@ProtectedRestController(InnsynController.INNSYN)
public class InnsynController {
    private static final Logger LOG = LoggerFactory.getLogger(InnsynController.class);

    public static final String INNSYN = "/rest/innsyn";

    private final Innsyn innsynTjeneste;

    @Inject
    public InnsynController(Innsyn innsyn) {
        this.innsynTjeneste = innsyn;
    }

    @GetMapping("/saker")
    public List<Sak> saker() {
        return innsynTjeneste.hentSaker();
    }

    @GetMapping("/v2/saker")
    public Saker sakerV2() {
        return innsynTjeneste.hentSakerV2();
    }


    @GetMapping("/uttaksplan")
    public Uttaksplan uttaksplan(@RequestParam(name = "saksnummer") String saksnummer) {
        return innsynTjeneste.hentUttaksplan(saksnummer);
    }

    @GetMapping("/uttaksplanannen")
    public Uttaksplan uttaksplanAnnenPart(@RequestParam(name = "annenPart") @NotNull String annenPart) {
        return innsynTjeneste.hentUttaksplanAnnenPart(annenPart);
    }

    @PostMapping("/uttaksplanannen1")
    public Uttaksplan uttaksplanAnnenPart1(@RequestBody @NotNull String annenPart) {
        return innsynTjeneste.hentUttaksplanAnnenPart(annenPart);
    }

    @GetMapping("/vedtak")
    public Vedtak vedtak(@RequestParam(name = "saksnummer") String saksnummer) {
        return innsynTjeneste.hentVedtak(saksnummer);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [innsyn=" + innsynTjeneste + "]";
    }
}
