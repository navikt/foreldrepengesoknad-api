package no.nav.foreldrepenger.selvbetjening.innsyn;

import no.nav.foreldrepenger.common.innsyn.AnnenPartVedtak;
import no.nav.foreldrepenger.common.innsyn.Saker;
import no.nav.foreldrepenger.selvbetjening.http.AbstractRestConnection;
import no.nav.foreldrepenger.selvbetjening.oppslag.domain.Arbeidsforhold;
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

    public Optional<AnnenPartVedtak> annenPartVedtak(AnnenPartVedtakIdentifikator annenPartVedtakIdentifikator) {
        LOG.info("Henter annen parts vedtak");

        var uri = cfg.annenPartVedtakURI();
        var vedtak = Optional.ofNullable(postForObject(uri, annenPartVedtakIdentifikator, AnnenPartVedtak.class));

        LOG.info("Hentet annen parts vedtak. Antall perioder {}", vedtak.map(v -> v.perioder().size()).orElse(0));
        return vedtak;
    }

    public List<Arbeidsforhold> hentArbeidsForhold() {
        return Optional
                .ofNullable(getForObject(cfg.arbeidsforholdURI(), Arbeidsforhold[].class, false))
                .map(Arrays::asList)
                .orElse(emptyList());
    }

    public Saker hentSaker() {
        return getForObject(cfg.sakerURI(), Saker.class);
    }
}
