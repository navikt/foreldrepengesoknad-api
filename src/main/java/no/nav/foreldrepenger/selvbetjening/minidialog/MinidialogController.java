package no.nav.foreldrepenger.selvbetjening.minidialog;

import no.nav.foreldrepenger.selvbetjening.historikk.MinidialogInnslag;
import no.nav.foreldrepenger.selvbetjening.http.ProtectedRestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@ProtectedRestController(MinidialogController.MINIDIALOG_PATH)
public class MinidialogController {

    static final String MINIDIALOG_PATH = "/rest/minidialog";

    private final Minidialog minidialog;

    @Autowired
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
