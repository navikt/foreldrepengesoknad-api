package no.nav.foreldrepenger.selvbetjening.tjeneste.historikk;

import static java.util.Collections.emptyList;
import static no.nav.foreldrepenger.selvbetjening.util.EnvUtil.CONFIDENTIAL;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestOperations;

import no.nav.foreldrepenger.selvbetjening.tjeneste.AbstractRestConnection;
import no.nav.foreldrepenger.selvbetjening.tjeneste.oppslag.domain.Fødselsnummer;

@Component
public class HistorikkConnection extends AbstractRestConnection {

    private final HistorikkConfig config;

    public HistorikkConnection(RestOperations operations, HistorikkConfig config) {
        super(operations);
        this.config = config;
    }

    @Override
    public boolean isEnabled() {
        return config.isEnabled();
    }

    @Override
    protected URI pingURI() {
        return config.pingURI();
    }

    public List<SøknadInnslag> hentHistorikk() {
        List<SøknadInnslag> historikk = Optional
                .ofNullable(getForObject(config.historikkURI(), SøknadInnslag[].class, false))
                .map(Arrays::asList)
                .orElse(emptyList());
        LOG.trace(CONFIDENTIAL, "Fikk historikk {}", historikk);
        return historikk;
    }

    public List<SøknadInnslag> hentHistorikk(Fødselsnummer fnr) {
        List<SøknadInnslag> historikk = Optional
                .ofNullable(getForObject(config.historikkPreprodURI(fnr.getFnr()), SøknadInnslag[].class, false))
                .map(Arrays::asList)
                .orElse(emptyList());
        LOG.trace(CONFIDENTIAL, "Fikk historikk {}", historikk);
        return historikk;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [config=" + config + "]";
    }

}
