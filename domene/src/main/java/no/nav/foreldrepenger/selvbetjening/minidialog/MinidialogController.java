package no.nav.foreldrepenger.selvbetjening.minidialog;

import java.time.LocalDate;
import java.util.List;

import jakarta.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;

import no.nav.foreldrepenger.selvbetjening.http.ProtectedRestController;
import no.nav.foreldrepenger.selvbetjening.innsyn.Innsyn;
import no.nav.foreldrepenger.selvbetjening.innsyn.TilbakekrevingsInnslag;

@ProtectedRestController(MinidialogController.MINIDIALOG_PATH)
public class MinidialogController {

    static final String MINIDIALOG_PATH = "/rest/minidialog";
    private final Innsyn innsyn;

    @Autowired
    public MinidialogController(Innsyn innsyn) {
        this.innsyn = innsyn;
    }

    @GetMapping
    public List<MinidialogInnslag> aktive() {
        return innsyn.hentUttalelserOmTilbakekreving().stream().map(MinidialogController::map).toList();
    }

    private static MinidialogInnslag map(TilbakekrevingsInnslag tilbakekrevingsInnslag) {
        return new MinidialogInnslag(tilbakekrevingsInnslag.saksnummer().value(), tilbakekrevingsInnslag.opprettet(),
            tilbakekrevingsInnslag.saksnummer().value(), tilbakekrevingsInnslag.frist());
    }

    public record MinidialogInnslag(@NotNull String saksnr, @NotNull LocalDate opprettet, @NotNull String dialogId, LocalDate frist) {
    }
}
