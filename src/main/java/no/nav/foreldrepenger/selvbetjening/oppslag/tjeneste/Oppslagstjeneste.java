package no.nav.foreldrepenger.selvbetjening.oppslag.tjeneste;

import no.nav.foreldrepenger.selvbetjening.oppslag.tjeneste.json.Sak;
import no.nav.foreldrepenger.selvbetjening.oppslag.tjeneste.json.PersonDto;
import no.nav.foreldrepenger.selvbetjening.oppslag.tjeneste.json.SøkerinfoDto;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.inject.Inject;
import java.net.URI;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.web.util.UriComponentsBuilder.fromUri;

@Service
@ConditionalOnProperty(name = "stub.oppslag", havingValue = "false", matchIfMissing = true)
public class Oppslagstjeneste implements Oppslag {

    private static final Logger LOG = getLogger(Oppslagstjeneste.class);

    private final RestTemplate template;
    private final URI oppslagServiceUrl;
    private final URI mottakServiceUrl;

    @Inject
    public Oppslagstjeneste(@Value("${FPSOKNAD_OPPSLAG_API_URL}") URI oppslagUrl,
                            @Value("${FPSOKNAD_MOTTAK_API_URL}") URI mottakUrl,
                            RestTemplate template) {
        this.oppslagServiceUrl = oppslagUrl;
        this.mottakServiceUrl = mottakUrl;
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
    public List<Sak> hentSaker() {
        URI uri = fromUri(mottakServiceUrl).path("/mottak/saker").build().toUri();
        LOG.info("Fagsak URI: {}", uri);
        Sak[] saker = template.getForObject(uri, Sak[].class);
        return saker != null ? asList(saker) : emptyList();
    }

    @Override
    public String hentSøknad(String behandlingId) {
        URI uri = fromUri(mottakServiceUrl).path("/mottak/soknad").queryParam("behandlingId", behandlingId).build().toUri();
        LOG.info("Søknad URI: {}", uri);
        return template.getForObject(uri, String.class);
    }

}
