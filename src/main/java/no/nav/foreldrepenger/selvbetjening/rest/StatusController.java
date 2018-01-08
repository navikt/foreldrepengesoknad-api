package no.nav.foreldrepenger.selvbetjening.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
public class StatusController {

    @RequestMapping(method = {GET}, value = "/rest/internal/isAlive")
    public ResponseEntity<String> isAlive() {
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

}
