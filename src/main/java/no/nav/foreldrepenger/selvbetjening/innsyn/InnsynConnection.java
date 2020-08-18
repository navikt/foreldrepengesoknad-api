package no.nav.foreldrepenger.selvbetjening.innsyn;

import static java.util.Collections.emptyList;
import static no.nav.foreldrepenger.selvbetjening.util.StringUtil.flertall;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestOperations;

import no.nav.foreldrepenger.selvbetjening.http.AbstractRestConnection;
import no.nav.foreldrepenger.selvbetjening.innsyn.saker.Sak;
import no.nav.foreldrepenger.selvbetjening.innsyn.uttaksplan.Uttaksplan;
import no.nav.foreldrepenger.selvbetjening.innsyn.vedtak.Vedtak;
import no.nav.foreldrepenger.selvbetjening.oppslag.domain.Arbeidsforhold;

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
            saker = saker(cfg.infotrygdSakerURI(), "SAK");
        }

        return saker;
    }

    private List<Sak> saker(URI uri, String fra) {
        List<Sak> saker = Optional.ofNullable(getForObject(uri, Sak[].class, false))
                .map(Arrays::asList)
                .orElse(emptyList());

        saker.forEach(sak -> sak.setType(fra));

        LOG.info("Hentet {} sak{} fra {} ({})", saker.size(), flertall(saker.size()), uri, saker);
        return saker;

    }

    public List<Arbeidsforhold> hentArbeidsForhold() {
        return Optional
                .ofNullable(getForObject(cfg.arbeidsforholdURI(), Arbeidsforhold[].class, false))
                .map(Arrays::asList)
                .orElse(emptyList());
    }
}
