package no.nav.foreldrepenger.selvbetjening.tjeneste.virusscan;

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
            LOG.info("Scanner");
            ScanResult[] scanResult = putForObject(config.getUri(), attachment.bytes, ScanResult[].class);
            LOG.info("Fikk scan result {}", scanResult[0]);
            return Result.OK.equals(scanResult[0].getResult());
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
