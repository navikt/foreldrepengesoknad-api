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
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import no.nav.foreldrepenger.selvbetjening.felles.util.Enabled;
import no.nav.foreldrepenger.selvbetjening.felles.util.EnvUtil;
import no.nav.foreldrepenger.selvbetjening.oppslag.json.AktørId;
import no.nav.foreldrepenger.selvbetjening.oppslag.json.Sak;
import no.nav.foreldrepenger.selvbetjening.oppslag.tjeneste.innsyn.InnsynTjeneste;
import no.nav.foreldrepenger.selvbetjening.oppslag.tjeneste.innsyn.UttaksPeriode;
import no.nav.foreldrepenger.selvbetjening.oppslag.tjeneste.json.PersonDto;
import no.nav.foreldrepenger.selvbetjening.oppslag.tjeneste.json.SøkerinfoDto;

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
    public List<Sak> hentSaker() {
        List<Sak> saker = new ArrayList<>();

        URI sakUri = fromUri(oppslagServiceUrl).path("/sak").build().toUri();
        List<Sak> sakSaker = asList(
                Optional.ofNullable(template.getForObject(sakUri, Sak[].class)).orElse(new Sak[] {}));
        saker.addAll(sakSaker);

        if (Enabled.FPSAKSAKER) {
            URI fpsakUri = fromUri(mottakServiceUrl).path("/mottak/saker").build().toUri();
            List<Sak> fpsakSaker = asList(
                    Optional.ofNullable(template.getForObject(fpsakUri, Sak[].class)).orElse(new Sak[] {}));
            saker.addAll(fpsakSaker);

            LOG.info("Henter {} sak(er) fra fpsak og {} sak(er) fra Sak", fpsakSaker.size(), sakSaker.size());
        }
        else {
            LOG.info("Henter {} sak(er) fra Sak", sakSaker.size());
        }
        if (EnvUtil.isDevOrPreprod(env)) {
            try {
                for (Sak sak : saker) {
                    List<UttaksPeriode> plan = innsyn.hentUttaksplan(sak.getSaksnummer());
                    plan.stream().forEach(s -> LOG.info("Uttaksplan for {} er {}", sak.getSaksnummer(), s));
                }
            } catch (Exception e) {
                LOG.trace("Dette gikk galt, men no worries, testing testing", e);
            }
        }

        return saker;
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
