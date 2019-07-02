package no.nav.foreldrepenger.selvbetjening.tjeneste.minidialog;

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
public class MinidialogConnection extends AbstractRestConnection {

    private final MinidialogConfig config;

    public MinidialogConnection(RestOperations operations, MinidialogConfig config) {
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

    public List<MinidialogInnslag> hentMinidialoger() {
        return hentFra(config.minidialogURI());
    }

    public List<MinidialogInnslag> hentMinidialoger(Fødselsnummer fnr) {
        return hentFra(config.minidialogPreprodURI(fnr.getFnr()));
    }

    private List<MinidialogInnslag> hentFra(URI uri) {
        LOG.trace("Henter  minidialoger fra {}", uri);
        List<MinidialogInnslag> dialoger = Optional
                .ofNullable(getForObject(uri, MinidialogInnslag[].class, false))
                .map(Arrays::asList)
                .orElse(emptyList());
        LOG.trace("Hentet minidialoger {}", dialoger);
        return dialoger;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [config=" + config + "]";
    }
}
