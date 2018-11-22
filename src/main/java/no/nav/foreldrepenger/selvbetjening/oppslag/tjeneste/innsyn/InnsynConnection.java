package no.nav.foreldrepenger.selvbetjening.oppslag.tjeneste.innsyn;

import static java.util.Collections.emptyList;
import static no.nav.foreldrepenger.selvbetjening.oppslag.tjeneste.innsyn.InnsynConfig.SAKSNUMMER;
import static no.nav.foreldrepenger.selvbetjening.oppslag.tjeneste.innsyn.InnsynConfig.UTTAKSPLAN;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import no.nav.foreldrepenger.selvbetjening.felles.util.TokenHandler;

@Component
public class InnsynConnection extends AbstractRestConnection implements Pingable {
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
        return uri(config.getUri(), config.getPingPath());
    }

    public List<UttaksPeriode> hentUttaksplan(String saksnummer) {
        LOG.trace("Henter uttaksplan");
        return Optional.ofNullable(getForObject(uri(config.getUri(), UTTAKSPLAN,
                queryParams(SAKSNUMMER, saksnummer)), UttaksPeriode[].class))
                .map(Arrays::asList)
                .orElse(emptyList());
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
