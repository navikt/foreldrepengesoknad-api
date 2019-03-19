package no.nav.foreldrepenger.selvbetjening.tjeneste.innsyn;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.concat;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestOperations;

import no.nav.foreldrepenger.selvbetjening.tjeneste.AbstractRestConnection;
import no.nav.foreldrepenger.selvbetjening.tjeneste.innsyn.saker.Sak;
import no.nav.foreldrepenger.selvbetjening.tjeneste.innsyn.uttaksplan.Uttaksplan;
import no.nav.foreldrepenger.selvbetjening.tjeneste.innsyn.vedtak.Vedtak;

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

    public Uttaksplan hentUttaksplan(String saksnummer) {
        return getForObject(innsynConfig.getUttakURI(saksnummer), Uttaksplan.class, false);
    }

    public Vedtak hentVedtak(String saksnummer) {
        return getForObject(innsynConfig.getVedtakURI(saksnummer), Vedtak.class, false);
    }

    public List<Sak> hentSaker() {
        List<Sak> sak = saker(innsynConfig.getSakURI(), "SAK");
        List<Sak> fpsak = saker(innsynConfig.getFpsakURI(), "FPSAK");

        return concat(sak.stream(), fpsak.stream()).collect(toList());
    }

    private List<Sak> saker(URI uri, String fra) {
        List<Sak> saker = Optional.ofNullable(getForObject(uri, Sak[].class, false))
                .map(Arrays::asList)
                .orElse(emptyList());

        saker.forEach(sak -> sak.setType(fra));

        LOG.info("Hentet {} sak(er) fra {}", saker.size(), fra);
        return saker;

    }

}
