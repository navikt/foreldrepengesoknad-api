package no.nav.foreldrepenger.selvbetjening.minidialog;

import java.util.List;

import javax.inject.Inject;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import no.nav.foreldrepenger.selvbetjening.historikk.MinidialogInnslag;
import no.nav.security.token.support.core.api.ProtectedWithClaims;

@RestController
@ProtectedWithClaims(issuer = "selvbetjening", claimMap = { "acr=Level4" })
@RequestMapping(MinidialogController.MINIDIALOG)
public class MinidialogController {

    static final String MINIDIALOG = "/rest/minidialog";

    private final Minidialog minidialog;

    @Inject
    public MinidialogController(Minidialog minidialog) {
        this.minidialog = minidialog;
    }

    @GetMapping
    public List<MinidialogInnslag> aktive() {
        return minidialog.aktive();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [minidialog=" + minidialog + "]";
    }
}
