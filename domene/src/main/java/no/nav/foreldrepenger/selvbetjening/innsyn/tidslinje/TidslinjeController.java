package no.nav.foreldrepenger.selvbetjening.innsyn.tidslinje;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import no.nav.foreldrepenger.common.domain.Saksnummer;
import no.nav.foreldrepenger.selvbetjening.http.ProtectedRestController;
import no.nav.foreldrepenger.selvbetjening.innsyn.Innsyn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@ProtectedRestController("/rest/innsyn/tidslinje")
public class TidslinjeController {
    private final Innsyn innsyn;

    @Autowired
    public TidslinjeController(Innsyn innsyn) {
        this.innsyn = innsyn;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<TidslinjeHendelseDto> hentTidslinje(@RequestParam @Valid @NotNull Saksnummer saksnummer) {
        return innsyn.tidslinje(saksnummer);
    }
}
