package no.nav.foreldrepenger.selvbetjening.consumer;

import no.nav.foreldrepenger.selvbetjening.consumer.json.PersonDto;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.inject.Inject;
import java.net.URI;

import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.web.util.UriComponentsBuilder.fromUri;

@Service
public class Oppslagstjeneste {

    private static final Logger LOG = getLogger(Oppslagstjeneste.class);

    private final RestTemplate template;
    private final URI oppslagServiceUrl;

    @Inject
    public Oppslagstjeneste(@Value("${FPSOKNAD_OPPSLAG_API_URL}") URI uri, RestTemplate template) {
        this.oppslagServiceUrl = uri;
        this.template = template;
    }

    public PersonDto hentPerson(String fnr) {
        URI url = fromUri(oppslagServiceUrl).path("/person").queryParam("fnr", fnr).build().toUri();
        LOG.info("Oppslag URL: {}", url);

        return template.getForObject(url, PersonDto.class);
    }
}
