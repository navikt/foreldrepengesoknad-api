package no.nav.foreldrepenger.selvbetjening.minidialog;

import static java.util.Collections.emptyList;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestOperations;

import no.nav.foreldrepenger.selvbetjening.historikk.MinidialogInnslag;
import no.nav.foreldrepenger.selvbetjening.http.AbstractRestConnection;

@Component
public class MinidialogConnection extends AbstractRestConnection {

    private final MinidialogConfig config;

    public MinidialogConnection(RestOperations operations, MinidialogConfig config) {
        super(operations);
        this.config = config;
    }

    public List<MinidialogInnslag> hentAktiveSpørsmål() {
        return get(config.aktiveSpmURI());
    }

    private List<MinidialogInnslag> get(URI uri) {
        LOG.info("Henter minidialoger fra {}", uri);
        var dialoger = Optional.ofNullable(getForObject(uri, MinidialogInnslag[].class)).map(Arrays::asList).orElse(emptyList());
        LOG.trace("Hentet minidialoger {} fra {}", dialoger, uri);
        return dialoger;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [config=" + config + "]";
    }

}
