package no.nav.foreldrepenger.selvbetjening.engangstonad.rest;

import no.nav.foreldrepenger.selvbetjening.engangstonad.rest.json.Oppstartsinfo;
import org.slf4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import static org.slf4j.LoggerFactory.getLogger;

@RestController
public class Oppstart {

    private static final Logger LOG = getLogger(Oppstart.class);

    @RequestMapping(method = {RequestMethod.GET}, value = "/rest/oppstartsinfo")
    public Oppstartsinfo oppstartsinfo() {
        LOG.info("Henter info om bruker");
        RestTemplate restTemplate = new RestTemplate();
        Oppstartsinfo oppstartsinfo = restTemplate.getForObject("https://foreldrepenger-selvbetjening-oppslag.nais.preprod.local/startup/?fnr=03016536325", Oppstartsinfo.class);
        return oppstartsinfo;
    }
}
