package no.nav.foreldrepenger.selvbetjening.oppslag.tjeneste;

import static java.util.Arrays.asList;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.web.util.UriComponentsBuilder.fromUri;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import no.nav.foreldrepenger.selvbetjening.oppslag.json.Sak;
import no.nav.foreldrepenger.selvbetjening.oppslag.tjeneste.json.GSakDeserializer;
import no.nav.foreldrepenger.selvbetjening.oppslag.tjeneste.json.PersonDto;
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
        List<Sak> saker = new ArrayList<>();

        URI fpsakUri = fromUri(mottakServiceUrl).path("/mottak/saker").build().toUri();
        List<Sak> fpsakSaker = asList(
                Optional.ofNullable(template.getForObject(fpsakUri, Sak[].class)).orElse(new Sak[] {}));
        saker.addAll(fpsakSaker);
        List<Sak> gsakSaker = gsakSaker();
        saker.addAll(gsakSaker);
        LOG.info("Henter {} saker fra fpsak og {} saker fra gsak", fpsakSaker.size(), gsakSaker.size());
        return saker;
    }

    private List<Sak> gsakSaker() {
        try {
            URI gsakUri = fromUri(oppslagServiceUrl).path("/gsak").build().toUri();
            String gsakerJson = template.getForObject(gsakUri, String.class);
            return gSakDeserializer.from(gsakerJson);
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    @Override
    public String hentSøknad(String behandlingId) {
        URI uri = fromUri(mottakServiceUrl).path("/mottak/soknad").queryParam("behandlingId", behandlingId).build()
                .toUri();
        LOG.info("Søknad URI: {}", uri);
        return template.getForObject(uri, String.class);
    }

}
