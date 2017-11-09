package no.nav.foreldrepenger.selvbetjening.engangstonad;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatusController {

  @RequestMapping(method = {RequestMethod.GET}, value = "/rest/internal/isAlive")
  public ResponseEntity<String> isAlive() {
    return new ResponseEntity<String>("OK", HttpStatus.OK);
  }

}
