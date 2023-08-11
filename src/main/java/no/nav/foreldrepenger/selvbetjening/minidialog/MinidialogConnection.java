package no.nav.foreldrepenger.selvbetjening.minidialog;

import no.nav.foreldrepenger.selvbetjening.historikk.MinidialogInnslag;
import no.nav.foreldrepenger.selvbetjening.http.AbstractRestConnection;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestOperations;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;

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

    public List<MinidialogInnslag> hentAktiveSpørsmål() {
        return getIfEnabled(config.aktiveSpmURI());
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
        return emptyList();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [config=" + config + "]";
    }

}
