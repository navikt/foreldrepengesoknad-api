package no.nav.foreldrepenger.selvbetjening.oppslag.tjeneste;

import no.nav.foreldrepenger.selvbetjening.oppslag.json.Sak;
import no.nav.foreldrepenger.selvbetjening.oppslag.tjeneste.json.PersonDto;
import no.nav.foreldrepenger.selvbetjening.oppslag.tjeneste.json.SakDeserializer;
import no.nav.foreldrepenger.selvbetjening.oppslag.tjeneste.json.SøkerinfoDto;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.inject.Inject;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.Arrays.asList;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.web.util.UriComponentsBuilder.fromUri;

@Service
@ConditionalOnProperty(name = "stub.oppslag", havingValue = "false", matchIfMissing = true)
public class Oppslagstjeneste implements Oppslag {

    private static final Logger LOG = getLogger(Oppslagstjeneste.class);

    private final RestTemplate template;
    private final URI oppslagServiceUrl;
    private final URI mottakServiceUrl;
    private SakDeserializer sakDeserializer;

    @Inject
    public Oppslagstjeneste(@Value("${FPSOKNAD_OPPSLAG_API_URL}") URI oppslagUrl,
                            @Value("${FPSOKNAD_MOTTAK_API_URL}") URI mottakUrl,
                            RestTemplate template,
                            SakDeserializer sakDeserializer) {
        this.oppslagServiceUrl = oppslagUrl;
        this.mottakServiceUrl = mottakUrl;
        this.template = template;
        this.sakDeserializer = sakDeserializer;
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

        URI sakUri = fromUri(oppslagServiceUrl).path("/sak").build().toUri();
        String sakerJson = template.getForObject(sakUri, String.class);
        List<Sak> sakSaker = sakDeserializer.from(sakerJson);
        saker.addAll(sakSaker);

        LOG.info("Henter {} saker fra fpsak og {} saker fra Sak", fpsakSaker.size(), sakSaker.size());

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
