package no.nav.foreldrepenger.selvbetjening.virusscan;

import static no.nav.foreldrepenger.selvbetjening.virusscan.Result.OK;

import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestOperations;

import no.nav.foreldrepenger.selvbetjening.error.AttachmentVirusException;
import no.nav.foreldrepenger.selvbetjening.http.AbstractRestConnection;

@Component
class VirusScanConnection extends AbstractRestConnection {

    private static final Logger LOG = LoggerFactory.getLogger(VirusScanConnection.class);

    private final VirusScanConfig config;

    public VirusScanConnection(RestOperations operations, VirusScanConfig config) {
        super(operations);
        this.config = config;
    }

    @Override
    public URI pingURI() {
        return config.pingURI();
    }

    @Override
    public boolean isEnabled() {
        return config.isEnabled();
    }

    @Override
    public String ping() {
        scan(new byte[0], "ping");
        return "OK";
    }

    public void scan(byte[] bytes, String name) {
        if (isEnabled()) {
            if (bytes != null) {
                try {
                    LOG.info("Scanner {}", name);
                    var scanResults = putForObject(config.getUri(), bytes, ScanResult[].class);
                    if (scanResults.length != 1) {
                        LOG.warn("Uventet respons med lengde {}, forventet lengde er 1", scanResults.length);
                        return;
                    }
                    var scanResult = scanResults[0];
                    LOG.info("Fikk scan result {}", scanResult);
                    if (OK.equals(scanResult.getResult())) {
                        LOG.info("Ingen virus i {}", name);
                        return;
                    }
                    LOG.warn("Fant virus i {}, status {}", name, scanResult.getResult());
                    throw new AttachmentVirusException(name);
                } catch (Exception e) {
                    LOG.warn("Kunne ikke scanne {}", name, e);
                    return;
                }
            }
            LOG.info("Ingen scanning av null bytes", bytes);
            return;
        }
        LOG.warn("Scanning av {} er deaktivert", name);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [config=" + config + "]";
    }

}
