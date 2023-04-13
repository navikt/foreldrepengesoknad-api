package no.nav.foreldrepenger.selvbetjening.innsyn.tidslinje;

import no.nav.foreldrepenger.common.domain.Saksnummer;
import no.nav.foreldrepenger.selvbetjening.http.ProtectedRestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.validation.Valid;


@ProtectedRestController("/rest/innsyn/tidslinje")
public class TidslinjeController {

    private final TidslinjeTjeneste tidslinjeTjeneste;

    @Autowired
    public TidslinjeController(TidslinjeTjeneste tidslinjeTjeneste) {
        this.tidslinjeTjeneste = tidslinjeTjeneste;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> hentTidslinje(@RequestParam @Valid Saksnummer saksnummer) {
        return ResponseEntity.ok().body(tidslinjeTjeneste.hentTidslinje(saksnummer));
    }

}
