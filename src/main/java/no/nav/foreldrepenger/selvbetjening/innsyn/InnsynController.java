package no.nav.foreldrepenger.selvbetjening.innsyn;

import static no.nav.foreldrepenger.common.domain.validation.InputValideringRegex.BARE_TALL;

import java.util.List;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import no.nav.foreldrepenger.common.domain.Fødselsnummer;
import no.nav.foreldrepenger.common.innsyn.uttaksplan.UttaksplanDto;
import no.nav.foreldrepenger.selvbetjening.http.ProtectedRestController;
import no.nav.foreldrepenger.selvbetjening.innsyn.saker.Sak;

@Validated
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
    public UttaksplanDto uttaksplan(@RequestParam(name = "saksnummer") @Pattern(regexp = BARE_TALL) @NotNull String saksnummer) {
        return innsynTjeneste.hentUttaksplan(saksnummer);
    }

    @GetMapping("/uttaksplanannen")
    public UttaksplanDto uttaksplanAnnenPart(@Valid @RequestParam(name = "annenPart") @NotNull Fødselsnummer annenPart) {
        return innsynTjeneste.hentUttaksplanAnnenPart(annenPart);
    }

    @PostMapping("/uttaksplanannen1")
    public UttaksplanDto uttaksplanAnnenPart1(@Valid @RequestBody @NotNull Fødselsnummer annenPart) {
        return innsynTjeneste.hentUttaksplanAnnenPart(annenPart);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [innsyn=" + innsynTjeneste + "]";
    }
}
