package no.nav.foreldrepenger.selvbetjening.oppslag.tjeneste;

import static java.util.Arrays.asList;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.web.util.UriComponentsBuilder.fromUri;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import no.nav.foreldrepenger.selvbetjening.oppslag.json.Sak;
import no.nav.foreldrepenger.selvbetjening.oppslag.tjeneste.json.PersonDto;
import no.nav.foreldrepenger.selvbetjening.oppslag.tjeneste.json.SøkerinfoDto;

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
        LOG.trace("Person URI: {}", uri);
        return template.getForObject(uri, PersonDto.class);
    }

    @Override
    public SøkerinfoDto hentSøkerinfo() {
        URI uri = fromUri(oppslagServiceUrl).path("/oppslag").build().toUri();
        LOG.trace("Oppslag URI: {}", uri);
        return template.getForObject(uri, SøkerinfoDto.class);
    }

    @Override
    public List<Sak> hentSaker() {
        List<Sak> saker = new ArrayList<>();

        // TODO ta inn igjen for engangsstønad/foreldrepenger i november
        // URI fpsakUri =
        // fromUri(mottakServiceUrl).path("/mottak/saker").build().toUri();
        // List<Sak> fpsakSaker =
        // asList(Optional.ofNullable(template.getForObject(fpsakUri,
        // Sak[].class)).orElse(new Sak[] {}));
        // saker.addAll(fpsakSaker);

        URI sakUri = fromUri(oppslagServiceUrl).path("/sak").build().toUri();
        List<Sak> sakSaker = asList(
                Optional.ofNullable(template.getForObject(sakUri, Sak[].class)).orElse(new Sak[] {}));
        saker.addAll(sakSaker);

        // LOG.info("Henter {} saker fra fpsak og {} saker fra Sak", fpsakSaker.size(),
        // sakSaker.size());
        LOG.info("Henter {} saker fra Sak", sakSaker.size());

        return saker;
    }

    @Override
    @Cacheable(cacheNames = "aktoer")
    public String hentAktørId(String fnr) {
        LOG.info("Henter aktørId");
        URI uri = fromUri(oppslagServiceUrl)
                .path("/aktorfnr")
                .queryParams(queryParams("fnr", fnr))
                .build()
                .toUri();
        return template.getForObject(uri, String.class);
    }

    protected static HttpHeaders queryParams(String key, String value) {
        HttpHeaders queryParams = new HttpHeaders();
        queryParams.add(key, value);
        return queryParams;
    }

}
