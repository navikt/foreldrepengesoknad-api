package no.nav.foreldrepenger.selvbetjening.innsyn;

import static java.util.Collections.emptyList;
import static no.nav.foreldrepenger.common.util.StringUtil.flertall;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestOperations;

import no.nav.foreldrepenger.common.domain.Fødselsnummer;
import no.nav.foreldrepenger.common.domain.Saksnummer;
import no.nav.foreldrepenger.common.innsyn.uttaksplan.UttaksplanDto;
import no.nav.foreldrepenger.common.innsyn.v2.Saker;
import no.nav.foreldrepenger.common.innsyn.v2.VedtakPeriode;
import no.nav.foreldrepenger.selvbetjening.http.AbstractRestConnection;
import no.nav.foreldrepenger.selvbetjening.innsyn.saker.Sak;
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

    public UttaksplanDto hentUttaksplan(Saksnummer saksnummer) {
        return getIfEnabled(cfg.uttakURI(saksnummer), UttaksplanDto.class, false);
    }

    public UttaksplanDto hentUttaksplanAnnenPart(Fødselsnummer annenPart) {
        return getIfEnabled(cfg.uttakURIForAnnenPart(annenPart), UttaksplanDto.class, false);
    }

    private <T> T getIfEnabled(URI uri, Class<T> clazz, boolean shouldThrow) {
        if (isEnabled()) {
            return getForObject(uri, clazz, shouldThrow);
        }
        LOG.warn("Innsyn er ikke aktivert");
        return null;
    }

    public List<Sak> hentSaker() {
        var uri = cfg.fpsakURI();
        var saker = Optional.ofNullable(getForObject(uri, Sak[].class, false))
                .map(Arrays::asList)
                .orElse(emptyList());
        LOG.info("Hentet {} sak{} fra {}", saker.size(), flertall(saker.size()), uri);
        return saker;
    }

    public List<VedtakPeriode> annenPartsVedtaksperioder(AnnenPartVedtakIdentifikator annenPartVedtakIdentifikator) {
        LOG.info("Henter annen parts vedtaksperioder");

        var uri = cfg.vedtaksperioderURI();
        var perioder = Optional.ofNullable(postForObject(uri, annenPartVedtakIdentifikator, VedtakPeriode[].class))
            .map(Arrays::asList)
            .orElse(emptyList());

        LOG.info("Hentet annen parts vedtaksperioder. Antall perioder {}", perioder.size());
        return perioder;
    }

    public List<Arbeidsforhold> hentArbeidsForhold() {
        return Optional
                .ofNullable(getForObject(cfg.arbeidsforholdURI(), Arbeidsforhold[].class, false))
                .map(Arrays::asList)
                .orElse(emptyList());
    }

    public Saker hentSakerV2() {
        return getForObject(cfg.fpsakV2URI(), Saker.class);
    }
}
