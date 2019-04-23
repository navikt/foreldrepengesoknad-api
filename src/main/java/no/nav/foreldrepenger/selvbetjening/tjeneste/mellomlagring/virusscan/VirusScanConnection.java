package no.nav.foreldrepenger.selvbetjening.tjeneste.mellomlagring.virusscan;

import java.net.URI;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestOperations;

import no.nav.foreldrepenger.selvbetjening.tjeneste.AbstractRestConnection;
import no.nav.foreldrepenger.selvbetjening.tjeneste.mellomlagring.Attachment;

@Component
public class VirusScanConnection extends AbstractRestConnection {

    private final VirusScanConfig config;

    public VirusScanConnection(RestOperations operations, VirusScanConfig config) {
        super(operations);
        this.config = config;
    }

    @Override
    public boolean isEnabled() {
        return config.isEnabled();
    }

    public boolean scan(Attachment attachment) {
        try {
            return Result.OK.equals(postForObject(config.getUri(), attachment.bytes, ScanResult.class).getResult());
        } catch (Exception e) {
            LOG.warn("Kunne ikke scanne {}", attachment.uuid, e);
            return true;
        }
    }

    @Override
    protected URI pingURI() {
        return config.getPingURI();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [config=" + config + "]";
    }
}
