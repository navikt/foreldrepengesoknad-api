package no.nav.foreldrepenger.selvbetjening.tjeneste.innsyn;

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
import no.nav.foreldrepenger.selvbetjening.tjeneste.innsyn.saker.Sak;
import no.nav.foreldrepenger.selvbetjening.tjeneste.innsyn.uttaksplan.Uttaksplan;
import no.nav.foreldrepenger.selvbetjening.tjeneste.innsyn.vedtak.Vedtak;

@Component
public class InnsynConnection extends AbstractRestConnection {
    private static final Logger LOG = LoggerFactory.getLogger(InnsynConnection.class);

    private final InnsynConfig cfg;

    public InnsynConnection(RestOperations operations, InnsynConfig cfg) {
        super(operations);
        this.cfg = cfg;
    }

    @Override
    public boolean isEnabled() {
        return cfg.isEnabled();
    }

    @Override
    public URI pingURI() {
        return cfg.pingURI();
    }

    public Uttaksplan hentUttaksplan(String saksnummer) {
        return getIfEnabled(cfg.uttakURI(saksnummer), Uttaksplan.class, false);
    }

    public Uttaksplan hentUttaksplanAnnenPart(String annenPart) {
        return getIfEnabled(cfg.uttakURIForAnnenPart(annenPart), Uttaksplan.class, false);
    }

    public Vedtak hentVedtak(String saksnummer) {
        return getIfEnabled(cfg.vedtakURI(saksnummer), Vedtak.class, false);
    }

    private <T> T getIfEnabled(URI uri, Class<T> clazz, boolean shouldThrow) {
        if (isEnabled()) {
            return getForObject(uri, clazz, shouldThrow);
        }
        LOG.warn("Innsyn er ikke aktivert");
        return null;
    }

    public List<Sak> hentSaker() {
        List<Sak> saker = saker(cfg.fpsakURI(), "FPSAK");
        if (saker.isEmpty()) {
            saker = saker(cfg.sakURI(), "SAK");
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
