package no.nav.foreldrepenger.selvbetjening.minidialog;

import java.util.List;

import javax.inject.Inject;

import org.springframework.web.bind.annotation.GetMapping;

import no.nav.foreldrepenger.selvbetjening.historikk.MinidialogInnslag;
import no.nav.foreldrepenger.selvbetjening.http.ProtectedRestController;

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
