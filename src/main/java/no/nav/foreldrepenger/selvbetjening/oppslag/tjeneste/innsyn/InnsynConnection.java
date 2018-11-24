package no.nav.foreldrepenger.selvbetjening.oppslag.tjeneste.innsyn;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static no.nav.foreldrepenger.selvbetjening.oppslag.tjeneste.innsyn.InnsynConfig.SAKSNUMMER;
import static no.nav.foreldrepenger.selvbetjening.oppslag.tjeneste.innsyn.InnsynConfig.UTTAKSPLAN;
import static org.springframework.web.util.UriComponentsBuilder.fromUri;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import no.nav.foreldrepenger.selvbetjening.felles.util.Enabled;
import no.nav.foreldrepenger.selvbetjening.felles.util.TokenHandler;
import no.nav.foreldrepenger.selvbetjening.oppslag.json.Sak;

@Component
public class InnsynConnection extends AbstractRestConnection {
    private static final Logger LOG = LoggerFactory.getLogger(InnsynConnection.class);

    private final InnsynConfig config;

    public InnsynConnection(RestTemplate template, TokenHandler tokenHandler, InnsynConfig config) {
        super(template, tokenHandler);
        this.config = config;
    }

    @Override
    public boolean isEnabled() {
        return config.isEnabled();
    }

    @Override
    public String ping() {
        return ping(pingEndpoint());
    }

    public URI pingEndpoint() {
        return uri(config.getMottakURI(), config.getPingPath());
    }

    public List<UttaksPeriode> hentUttaksplan(String saksnummer) {
        LOG.trace("Henter uttaksplan");
        return Optional.ofNullable(getForObject(uri(config.getMottakURI(), UTTAKSPLAN,
                queryParams(SAKSNUMMER, saksnummer)), UttaksPeriode[].class))
                .map(Arrays::asList)
                .orElse(emptyList());
    }

    public List<Sak> hentSaker() {
        List<Sak> saker = new ArrayList<>();

        URI sakUri = fromUri(config.getOppslagURI()).path("/sak").build().toUri();
        List<Sak> sakSaker = asList(
                Optional.ofNullable(template.getForObject(sakUri, Sak[].class)).orElse(new Sak[] {}));
        saker.addAll(sakSaker);

        if (Enabled.FPSAKSAKER) {
            URI fpsakUri = fromUri(config.getMottakURI()).path("/mottak/saker").build().toUri();
            List<Sak> fpsakSaker = asList(
                    Optional.ofNullable(template.getForObject(fpsakUri, Sak[].class)).orElse(new Sak[] {}));
            saker.addAll(fpsakSaker);
            LOG.info("Henter {} sak(er) fra fpsak og {} sak(er) fra Sak", fpsakSaker.size(), sakSaker.size());
        }
        else {
            LOG.info("Henter {} sak(er) fra Sak", sakSaker.size());
        }
        if (isDevOrPreprod()) {
            try {
                for (Sak sak : saker) {
                    List<UttaksPeriode> plan = hentUttaksplan(sak.getSaksnummer());
                    plan.stream().forEach(s -> LOG.info("Uttaksplan for {} er {}", sak.getSaksnummer(), s));
                }
            } catch (Exception e) {
                LOG.trace("Dette gikk galt, men no worries, testing testing", e);
            }
        }
        return saker;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [config=" + config + "]";
    }

    @Override
    public String name() {
        return "innsyn";
    }

}
