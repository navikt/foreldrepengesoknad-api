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

import no.nav.foreldrepenger.selvbetjening.oppslag.tjeneste.json.GSakDeserializer;
import no.nav.foreldrepenger.selvbetjening.oppslag.tjeneste.json.PersonDto;
import no.nav.foreldrepenger.selvbetjening.oppslag.tjeneste.json.Sak;
import no.nav.foreldrepenger.selvbetjening.oppslag.tjeneste.json.SøkerinfoDto;

@Service
@ConditionalOnProperty(name = "stub.oppslag", havingValue = "false", matchIfMissing = true)
public class Oppslagstjeneste implements Oppslag {

    private static final Logger LOG = getLogger(Oppslagstjeneste.class);

    private final RestTemplate template;
    private final URI oppslagServiceUrl;
    private final URI mottakServiceUrl;
    private GSakDeserializer gSakDeserializer;

    @Inject
    public Oppslagstjeneste(@Value("${FPSOKNAD_OPPSLAG_API_URL}") URI oppslagUrl,
            @Value("${FPSOKNAD_MOTTAK_API_URL}") URI mottakUrl,
            RestTemplate template,
            GSakDeserializer gSakDeserializer) {
        this.oppslagServiceUrl = oppslagUrl;
        this.mottakServiceUrl = mottakUrl;
        this.template = template;
        this.gSakDeserializer = gSakDeserializer;
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
        URI fpsakUri = fromUri(mottakServiceUrl).path("/mottak/saker").build().toUri();
        LOG.info("Fpsak URI: {}", fpsakUri);
        List<Sak> saker = asList(template.getForObject(fpsakUri, Sak[].class));
        URI gsakUri = fromUri(oppslagServiceUrl).path("/oppslag/gsak").build().toUri();
        LOG.info("Gsak URI: {}", gsakUri);
        String gsakerJson = template.getForObject(gsakUri, String.class);
        saker.addAll(gSakDeserializer.from(gsakerJson));
        return saker;
    }

    @Override
    public String hentSøknad(String behandlingId) {
        URI uri = fromUri(mottakServiceUrl).path("/mottak/soknad").queryParam("behandlingId", behandlingId).build()
                .toUri();
        LOG.info("Søknad URI: {}", uri);
        return template.getForObject(uri, String.class);
    }

}
