package no.nav.foreldrepenger.selvbetjening.vedlegg.virusscan;

import static no.nav.foreldrepenger.selvbetjening.http.RestClientConfiguration.LONG_TIMEOUT;
import static no.nav.foreldrepenger.selvbetjening.vedlegg.virusscan.Result.OK;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestOperations;

import no.nav.foreldrepenger.selvbetjening.http.AbstractRestConnection;
import no.nav.foreldrepenger.selvbetjening.vedlegg.AttachmentVirusException;

@Component
public class VirusScanConnection extends AbstractRestConnection {

    private static final Logger LOG = LoggerFactory.getLogger(VirusScanConnection.class);

    private final VirusScanConfig config;

    public VirusScanConnection(@Qualifier(LONG_TIMEOUT) RestOperations operations, VirusScanConfig config) {
        super(operations);
        this.config = config;
    }

    public void scan(byte[] bytes, String uuid) {
        if (!config.isEnabled()) {
            LOG.warn("Scanning er deaktivert");
        } else {
            if (bytes != null) {
                LOG.trace("Scanner {}", uuid);
                var scanResults = putForObject(config.getBaseUri(), bytes, ScanResult[].class);
                if (scanResults.length != 1) {
                    LOG.warn("Uventet respons med lengde {}, forventet lengde er 1", scanResults.length);
                    throw new AttachmentVirusException(uuid);
                }
                var scanResult = scanResults[0];
                LOG.trace("Fikk scan result {}", scanResult);
                if (OK.equals(scanResult.getResult())) {
                    LOG.trace("Ingen virus i {}", uuid);
                    return;
                }
                LOG.warn("Fant virus!, status {}", scanResult.getResult());
                throw new AttachmentVirusException("Virus påvist i dokument med id " + uuid);
            }
            LOG.info("Ingen scanning av null bytes", bytes);
        }
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [config=" + config + "]";
    }

}
