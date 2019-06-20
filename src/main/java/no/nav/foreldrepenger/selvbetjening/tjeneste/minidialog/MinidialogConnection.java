package no.nav.foreldrepenger.selvbetjening.tjeneste.minidialog;

import static java.util.Collections.emptyList;
import static no.nav.foreldrepenger.selvbetjening.util.EnvUtil.CONFIDENTIAL;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestOperations;

import no.nav.foreldrepenger.selvbetjening.tjeneste.AbstractRestConnection;

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
        List<MinidialogInnslag> dialoger = Optional
                .ofNullable(getForObject(config.minidialogURI(), MinidialogInnslag[].class, false))
                .map(Arrays::asList)
                .orElse(emptyList());
        LOG.trace(CONFIDENTIAL, "Fikk minidialoger {}", dialoger);
        return dialoger;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [config=" + config + "]";
    }
}
