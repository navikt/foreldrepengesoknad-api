package no.nav.foreldrepenger.selvbetjening.tjeneste.minidialog;

import static java.util.Collections.emptyList;

import java.net.URI;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestOperations;

import no.nav.foreldrepenger.selvbetjening.tjeneste.AbstractRestConnection;
import no.nav.foreldrepenger.selvbetjening.tjeneste.historikk.MinidialogInnslag;
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
    public URI pingURI() {
        return config.pingURI();
    }

    public List<MinidialogInnslag> hentMinidialoger(Fødselsnummer fnr, boolean kunAktive) {
        return getIfEnabled(config.minidialogPreprodURI(fnr.getFnr(), kunAktive));
    }

    public List<MinidialogInnslag> hentAktiveSpørsmål() {
        return getIfEnabled(config.aktiveSpmURI());
    }

    public List<MinidialogInnslag> hentAktiveSpørsmål(Fødselsnummer fnr) {
        return getIfEnabled(config.aktiveSpmURI(fnr.getFnr()));
    }

    private List<MinidialogInnslag> getIfEnabled(URI uri) {
        if (isEnabled()) {
            LOG.info("Henter minidialoger fra {}", uri);
            List<MinidialogInnslag> dialoger = Optional
                    .ofNullable(getForObject(uri, MinidialogInnslag[].class))
                    .map(Arrays::asList)
                    .orElse(emptyList());
            LOG.trace("Hentet minidialoger {} fra {}", dialoger, uri);
            return dialoger;
        }
        LOG.warn("Henting av  minidialoger er deaktivert");
        return Collections.emptyList();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [config=" + config + "]";
    }

}
