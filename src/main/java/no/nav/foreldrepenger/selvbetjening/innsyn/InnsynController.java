package no.nav.foreldrepenger.selvbetjening.innsyn;

import java.util.List;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import no.nav.foreldrepenger.selvbetjening.http.ProtectedRestController;
import no.nav.foreldrepenger.selvbetjening.innsyn.saker.Sak;
import no.nav.foreldrepenger.selvbetjening.innsyn.uttaksplan.Uttaksplan;

@ProtectedRestController(InnsynController.INNSYN)
public class InnsynController {

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

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [innsyn=" + innsynTjeneste + "]";
    }
}
