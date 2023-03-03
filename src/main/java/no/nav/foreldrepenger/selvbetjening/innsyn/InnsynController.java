package no.nav.foreldrepenger.selvbetjening.innsyn;

import no.nav.foreldrepenger.common.innsyn.AnnenPartVedtak;
import no.nav.foreldrepenger.common.innsyn.Saker;
import no.nav.foreldrepenger.selvbetjening.http.ProtectedRestController;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.inject.Inject;
import javax.validation.Valid;

import static no.nav.foreldrepenger.selvbetjening.innsyn.InnsynController.INNSYN;

@ProtectedRestController(INNSYN)
public class InnsynController {

    public static final String INNSYN = "/rest/innsyn/v2";

    private final Innsyn innsynTjeneste;

    @Inject
    public InnsynController(Innsyn innsyn) {
        this.innsynTjeneste = innsyn;
    }

    @GetMapping("/saker")
    public Saker saker() {
        return innsynTjeneste.hentSaker();
    }

    @PostMapping(path = "/annenPartVedtak", consumes = MediaType.APPLICATION_JSON_VALUE)
    public AnnenPartVedtak annenPartVedtak(@Valid @RequestBody AnnenPartVedtakIdentifikator annenPartVedtakIdentifikator) {
        return innsynTjeneste.annenPartVedtak(annenPartVedtakIdentifikator).orElse(null);
    }
}

