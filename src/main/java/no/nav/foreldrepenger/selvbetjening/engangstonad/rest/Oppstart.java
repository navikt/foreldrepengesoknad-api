package no.nav.foreldrepenger.selvbetjening.engangstonad.rest;

import no.nav.foreldrepenger.selvbetjening.engangstonad.rest.json.Oppstartsinfo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class Oppstart {

    @RequestMapping(method = {RequestMethod.GET}, value = "/rest/oppstartsinfo")
    public Oppstartsinfo oppstartsinfo() {
        RestTemplate restTemplate = new RestTemplate();
        Oppstartsinfo oppstartsinfo = restTemplate.getForObject("https://foreldrepenger-selvbetjening-oppslag.nais.preprod.local/startup/?fnr=03016536325", Oppstartsinfo.class);
        return oppstartsinfo;
    }
}
