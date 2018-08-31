package no.nav.foreldrepenger.selvbetjening.oppslag.tjeneste;

import static java.util.Arrays.asList;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.web.util.UriComponentsBuilder.fromUri;

import java.net.URI;
import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import no.nav.foreldrepenger.selvbetjening.oppslag.tjeneste.json.Fagsak;
import no.nav.foreldrepenger.selvbetjening.oppslag.tjeneste.json.PersonDto;
import no.nav.foreldrepenger.selvbetjening.oppslag.tjeneste.json.SøkerinfoDto;

@Service
@ConditionalOnProperty(name = "stub.oppslag", havingValue = "false", matchIfMissing = true)
public class Oppslagstjeneste implements Oppslag {

    private static final Logger LOG = getLogger(Oppslagstjeneste.class);

    private final RestTemplate template;
    private final URI oppslagServiceUrl;
    private final URI søknadURI;

    @Inject
    public Oppslagstjeneste(@Value("${FPSOKNAD_OPPSLAG_API_URL}") URI oppslagURI,
            @Value("${FPSOKNAD_MOTTAK_API_URL}") URI søknadURI, RestTemplate template) {
        this.oppslagServiceUrl = oppslagURI;
        this.søknadURI = søknadURI;
        this.template = template;
    }

    @Override
    public PersonDto hentPerson() {
        URI uri = fromUri(oppslagServiceUrl).path("/person").build().toUri();
        LOG.info("Person URI: {}", uri);
        return template.getForObject(uri, PersonDto.class);
    }

    @Override
    public SøkerinfoDto hentSøkerinfo() {
        URI uri = fromUri(oppslagServiceUrl).path("/oppslag").build().toUri();
        LOG.info("Oppslag URI: {}", uri);
        return template.getForObject(uri, SøkerinfoDto.class);
    }

    @Override
    public List<Fagsak> hentFagsaker() {
        URI uri = fromUri(oppslagServiceUrl).path("/oppslag/saker").build().toUri();
        LOG.info("Fagsak URI: {}", uri);
        return asList(template.getForObject(uri, Fagsak[].class));
    }

    @Override
    public String hentSøknad(String behandlingId) {
        URI uri = fromUri(søknadURI).path("/mottak/soknad").build().toUri();
        LOG.info("Søknad URI: {}", uri);
        return template.getForObject(uri, String.class);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [template=" + template + ", oppslagServiceUrl=" + oppslagServiceUrl + "]";
    }

}
