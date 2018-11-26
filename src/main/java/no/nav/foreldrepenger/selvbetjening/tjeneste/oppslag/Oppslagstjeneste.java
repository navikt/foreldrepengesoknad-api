package no.nav.foreldrepenger.selvbetjening.tjeneste.oppslag;

import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.web.util.UriComponentsBuilder.fromUri;

import java.net.URI;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import no.nav.foreldrepenger.selvbetjening.tjeneste.innsyn.InnsynTjeneste;
import no.nav.foreldrepenger.selvbetjening.tjeneste.oppslag.domain.AktørId;
import no.nav.foreldrepenger.selvbetjening.tjeneste.oppslag.dto.PersonDto;
import no.nav.foreldrepenger.selvbetjening.tjeneste.oppslag.dto.SøkerinfoDto;

@Service
@ConditionalOnProperty(name = "stub.oppslag", havingValue = "false", matchIfMissing = true)
public class Oppslagstjeneste implements Oppslag, EnvironmentAware {

    private static final Logger LOG = getLogger(Oppslagstjeneste.class);
    private final RestTemplate template;
    private final URI oppslagServiceUrl;
    private final URI mottakServiceUrl;
    private final InnsynTjeneste innsyn;
    private Environment env;

    @Inject
    public Oppslagstjeneste(@Value("${FPSOKNAD_OPPSLAG_API_URL}") URI oppslagUrl,
            @Value("${FPSOKNAD_MOTTAK_API_URL}") URI mottakUrl,
            RestTemplate template, InnsynTjeneste innsyn) {
        this.oppslagServiceUrl = oppslagUrl;
        this.mottakServiceUrl = mottakUrl;
        this.template = template;
        this.innsyn = innsyn;
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
    @Cacheable(cacheNames = "aktoer")
    public AktørId hentAktørId(String fnr) {
        LOG.trace("Henter aktørId");
        URI uri = fromUri(oppslagServiceUrl)
                .path("/oppslag/aktorfnr")
                .queryParams(fnr("fnr", fnr))
                .build().toUri();
        AktørId aktørId = template.getForObject(uri, AktørId.class);
        LOG.trace("Fikk aktørid " + aktørId);
        return aktørId;
    }

    protected static HttpHeaders fnr(String key, String value) {
        HttpHeaders queryParams = new HttpHeaders();
        queryParams.add(key, value);
        return queryParams;
    }

    @Override
    public void setEnvironment(Environment env) {
        this.env = env;
    }
}
