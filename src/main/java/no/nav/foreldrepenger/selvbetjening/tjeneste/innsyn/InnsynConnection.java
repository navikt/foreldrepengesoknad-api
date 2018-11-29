package no.nav.foreldrepenger.selvbetjening.tjeneste.innsyn;

import static com.google.common.collect.Iterables.concat;
import static com.google.common.collect.Iterables.unmodifiableIterable;
import static com.google.common.collect.Lists.newArrayList;
import static java.util.Collections.emptyList;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestOperations;

import no.nav.foreldrepenger.selvbetjening.tjeneste.AbstractRestConnection;

@Component
public class InnsynConnection extends AbstractRestConnection {
    private static final Logger LOG = LoggerFactory.getLogger(InnsynConnection.class);

    private final InnsynConfig innsynConfig;

    public InnsynConnection(RestOperations operations, InnsynConfig innsynConfig) {
        super(operations);
        this.innsynConfig = innsynConfig;
    }

    @Override
    public boolean isEnabled() {
        return innsynConfig.isEnabled();
    }

    @Override
    protected URI pingURI() {
        return innsynConfig.getPingURI();
    }

    public List<UttaksPeriode> hentUttaksplan(String saksnummer) {
        return Optional.ofNullable(getForObject(innsynConfig.getUttakURI(saksnummer), UttaksPeriode[].class))
                .map(Arrays::asList)
                .orElse(emptyList());
    }

    public List<Sak> hentSaker() {
        List<Sak> sakSaker = saker(innsynConfig.getSakURI(), "SAK");
        List<Sak> fpsakSaker = saker(innsynConfig.getFpsakURI(), "FPSAK");
        if (isDevOrPreprod()) {
            visSaker(fpsakSaker);
        }
        return newArrayList(unmodifiableIterable(concat(sakSaker, fpsakSaker)));
    }

    private List<Sak> saker(URI uri, String fra) {
        List<Sak> saker = Optional.ofNullable(getForObject(uri, Sak[].class))
                .map(Arrays::asList)
                .orElse(emptyList());
        LOG.info("Hentet {} sak(er) fra {}", saker.size(), fra);
        return saker;

    }

    private void visSaker(List<Sak> saker) {
        try {
            saker.stream()
                    .map(Sak::getSaksnummer)
                    .forEach(this::planFor);
        } catch (Exception e) {
            LOG.trace("Dette gikk galt, men frykt ikke, dette er bare en test foreløpig", e);
        }
    }

    private void planFor(String saksnummer) {
        hentUttaksplan(saksnummer).stream()
                .forEach(s -> LOG.info("Uttaksplan for {} er {}", saksnummer, s));
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "pingURI=" + pingURI() + ", fpsakURI=" + innsynConfig.getFpsakURI()
                + ", sakURI=" + innsynConfig.getSakURI() + ", uttakURI=" + innsynConfig.getUttakURI("42") + "]";
    }
}
