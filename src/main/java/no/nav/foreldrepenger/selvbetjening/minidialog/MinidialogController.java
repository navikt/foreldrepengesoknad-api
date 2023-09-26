package no.nav.foreldrepenger.selvbetjening.minidialog;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;

import no.nav.foreldrepenger.selvbetjening.http.ProtectedRestController;
import no.nav.foreldrepenger.selvbetjening.innsyn.Innsyn;
import no.nav.foreldrepenger.selvbetjening.innsyn.TilbakekrevingsInnslag;

@ProtectedRestController(MinidialogController.MINIDIALOG_PATH)
public class MinidialogController {

    static final String MINIDIALOG_PATH = "/rest/minidialog";
    private static final Period TILBAKEKREVING_FRIST = Period.ofWeeks(2); //TODO skal flyttes til fptilbake
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
        var opprettet = tilbakekrevingsInnslag.opprettet().atTime(7, 0); //Viser bare dato i frontend
        return new MinidialogInnslag(tilbakekrevingsInnslag.saksnummer().value(), opprettet, LocalDate.now().plusWeeks(1),
            tilbakekrevingsInnslag.saksnummer().value(), opprettet.plus(TILBAKEKREVING_FRIST).toLocalDate());
    }

    public record MinidialogInnslag(String saksnr, LocalDateTime opprettet, LocalDate gyldigTil, String dialogId, LocalDate frist) {
    }
}
