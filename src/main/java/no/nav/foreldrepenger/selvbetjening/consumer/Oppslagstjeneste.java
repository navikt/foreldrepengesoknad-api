package no.nav.foreldrepenger.selvbetjening.consumer;

import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.web.util.UriComponentsBuilder.fromUri;

import java.net.URI;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import no.nav.foreldrepenger.selvbetjening.consumer.json.PersonDto;

@Service
@ConditionalOnProperty(name = "stub.oppslag", havingValue = "false", matchIfMissing = true)
public class Oppslagstjeneste implements Oppslag {

    private static final Logger LOG = getLogger(Oppslagstjeneste.class);

    private final RestTemplate template;
    private final URI oppslagServiceUrl;

    @Inject
    public Oppslagstjeneste(@Value("${FPSOKNAD_OPPSLAG_API_URL}") URI uri, RestTemplate template) {
        this.oppslagServiceUrl = uri;
        this.template = template;
    }

    @Override
    public PersonDto hentPerson() {
        URI url = fromUri(oppslagServiceUrl).path("/person").build().toUri();
        LOG.info("Oppslag URL: {}", url);
        return template.getForObject(url, PersonDto.class);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [template=" + template + ", oppslagServiceUrl=" + oppslagServiceUrl + "]";
    }
}
