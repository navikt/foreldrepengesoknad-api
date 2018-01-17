package no.nav.foreldrepenger.selvbetjening.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatusController {

    @GetMapping("/internal/isAlive")
    public ResponseEntity<String> isAlive() {
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

}
