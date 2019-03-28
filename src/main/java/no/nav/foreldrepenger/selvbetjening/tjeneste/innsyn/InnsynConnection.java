package no.nav.foreldrepenger.selvbetjening.tjeneste.innsyn;

import no.nav.foreldrepenger.selvbetjening.tjeneste.AbstractRestConnection;
import no.nav.foreldrepenger.selvbetjening.tjeneste.innsyn.saker.Sak;
import no.nav.foreldrepenger.selvbetjening.tjeneste.innsyn.uttaksplan.Uttaksplan;
import no.nav.foreldrepenger.selvbetjening.tjeneste.innsyn.vedtak.Vedtak;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestOperations;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;

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
        List<Sak> saker = saker(innsynConfig.getFpsakURI(), "FPSAK");
        if (saker.isEmpty()) {
            saker.addAll(saker(innsynConfig.getSakURI(), "SAK"));
        }

        return saker;
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
