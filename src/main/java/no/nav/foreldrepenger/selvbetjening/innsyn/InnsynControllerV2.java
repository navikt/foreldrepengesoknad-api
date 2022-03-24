package no.nav.foreldrepenger.selvbetjening.innsyn;

import static no.nav.foreldrepenger.selvbetjening.innsyn.InnsynControllerV2.INNSYN;

import javax.inject.Inject;

import org.springframework.web.bind.annotation.GetMapping;

import no.nav.foreldrepenger.boot.conditionals.ConditionalOnNotProd;
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
}

