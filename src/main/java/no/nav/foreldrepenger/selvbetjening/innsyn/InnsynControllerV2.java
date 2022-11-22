package no.nav.foreldrepenger.selvbetjening.innsyn;

import static no.nav.foreldrepenger.selvbetjening.innsyn.InnsynControllerV2.INNSYN;

import javax.inject.Inject;
import javax.validation.Valid;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import no.nav.boot.conditionals.ConditionalOnNotProd;
import no.nav.foreldrepenger.common.innsyn.v2.AnnenPartVedtak;
import no.nav.foreldrepenger.common.innsyn.v2.Saker;
import no.nav.foreldrepenger.selvbetjening.http.ProtectedRestController;

@ConditionalOnNotProd
@ProtectedRestController(INNSYN)
public class InnsynControllerV2 {

    public static final String INNSYN = "/rest/innsyn/v2";

    private final Innsyn innsynTjeneste;

    @Inject
    public InnsynControllerV2(Innsyn innsyn) {
        this.innsynTjeneste = innsyn;
    }

    @GetMapping("/saker")
    public Saker sakerV2() {
        return innsynTjeneste.hentSakerV2();
    }

    @PostMapping(path = "/annenPartVedtak", consumes = MediaType.APPLICATION_JSON_VALUE)
    public AnnenPartVedtak annenPartVedtak(@Valid @RequestBody AnnenPartVedtakIdentifikator annenPartVedtakIdentifikator) {
        return innsynTjeneste.annenPartVedtak(annenPartVedtakIdentifikator).orElse(null);
    }
}

