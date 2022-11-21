package no.nav.foreldrepenger.selvbetjening.innsyn.tidslinje;

import no.nav.boot.conditionals.ConditionalOnNotProd;
import no.nav.foreldrepenger.selvbetjening.http.ProtectedRestController;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@ConditionalOnNotProd
@ProtectedRestController("/rest/innsyn/tidslinje")
public class TidslinjeDevController {

    private TidslinjeTjeneste tidslinjeTjeneste;

    public TidslinjeDevController(TidslinjeTjeneste tidslinjeTjeneste) {
        this.tidslinjeTjeneste = tidslinjeTjeneste;
    }

    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> hentTidslinje(@RequestParam String saksnummer) {
        return ResponseEntity.ok().body(tidslinjeTjeneste.hentTidslinje(saksnummer));
    }

}
