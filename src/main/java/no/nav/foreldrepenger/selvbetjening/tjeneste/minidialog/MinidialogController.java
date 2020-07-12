package no.nav.foreldrepenger.selvbetjening.tjeneste.minidialog;

import java.util.List;

import javax.inject.Inject;

import org.springframework.web.bind.annotation.GetMapping;

import no.nav.foreldrepenger.selvbetjening.http.ProtectedRestController;
import no.nav.foreldrepenger.selvbetjening.tjeneste.historikk.MinidialogInnslag;

@ProtectedRestController(MinidialogController.MINIDIALOG)
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
