package no.nav.foreldrepenger.selvbetjening.tjeneste.historikk;

import static java.util.Collections.emptyList;

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
    public URI pingURI() {
        return config.pingURI();
    }

    public List<HistorikkInnslag> hentHistorikk() {
        return hentHistorikk(config.historikkURI());
    }

    public List<HistorikkInnslag> hentHistorikk(Fødselsnummer fnr) {
        return hentHistorikk(config.historikkPreprodURI(fnr.getFnr()));
    }

    private List<HistorikkInnslag> hentHistorikk(URI uri) {
        LOG.trace("Henter historikk fra {}", uri);
        List<HistorikkInnslag> historikk = Optional
                .ofNullable(getForObject(uri, HistorikkInnslag[].class))
                .map(Arrays::asList)
                .orElse(emptyList());
        LOG.trace("Hentet historikk {} fra {}", historikk, uri);
        return historikk;
    }

    public List<String> manglendeVedlegg(Fødselsnummer fnr, String saksnr) {
        return hentManglendeVedlegg(config.vedleggPreprodURI(fnr, saksnr));
    }

    public List<String> manglendeVedlegg(String saksnr) {
        return hentManglendeVedlegg(config.vedleggURI(saksnr));
    }

    private List<String> hentManglendeVedlegg(URI uri) {
        LOG.trace("Henter manglende vedlegg fra {}", uri);
        List<String> vedleggIds = Optional
                .ofNullable(getForObject(uri, String[].class))
                .map(Arrays::asList)
                .orElse(emptyList());
        LOG.trace("Hentet manglende vedlegg {} fra {}", vedleggIds, uri);
        return vedleggIds;

    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [config=" + config + "]";
    }

}
