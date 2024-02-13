package no.nav.foreldrepenger.selvbetjening.http;

import no.nav.security.token.support.core.api.Unprotected;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;

@RestController
public class PreStopHookController {
    private static final Logger LOG = LoggerFactory.getLogger(PreStopHookController.class);

    @Unprotected
    @GetMapping("/internal/preStop")
    public ResponseEntity<String> preStop() throws InterruptedException {
        LOG.info("Mottok kall på /internal/preStop, sover 6 sekunder");
        Thread.sleep(Duration.ofSeconds(6));
        return ResponseEntity.ok().build();
    }
}
