package no.nav.foreldrepenger.selvbetjening.rest;

import no.nav.foreldrepenger.selvbetjening.rest.json.Engangsstonad;
import org.slf4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

import static java.time.LocalDate.now;
import static org.slf4j.LoggerFactory.getLogger;

@CrossOrigin
@RestController
@RequestMapping("/engangsstonad")
public class EngangsstonadController {

    private static final Logger LOG = getLogger(EngangsstonadController.class);

    @GetMapping("/{id}")
    public Engangsstonad hentEngangsstonad(@PathVariable String id) {
        Engangsstonad engangsstonad = Engangsstonad.stub();
        engangsstonad.id = id;
        return engangsstonad;
    }

    @PostMapping
    public ResponseEntity<Engangsstonad> opprettEngangsstonad(@RequestBody Engangsstonad engangsstonad, @RequestParam(name = "stub", defaultValue = "false", required = false) Boolean stub) {
        if (stub) {
            engangsstonad.id = "69";
            engangsstonad.opprettet = now();
        }

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(engangsstonad.id).toUri();
        return ResponseEntity.created(location).body(engangsstonad);
    }

}
