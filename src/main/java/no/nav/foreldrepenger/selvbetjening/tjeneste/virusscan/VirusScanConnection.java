package no.nav.foreldrepenger.selvbetjening.tjeneste.virusscan;

import static no.nav.foreldrepenger.selvbetjening.tjeneste.virusscan.Result.OK;

import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestOperations;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Metrics;
import no.nav.foreldrepenger.selvbetjening.error.AttachmentVirusException;

@Component
class VirusScanConnection {

    private static final Logger LOG = LoggerFactory.getLogger(VirusScanConnection.class);
    private static final Counter INGENVIRUS_COUNTER = counter("virus", "OK");
    private static final Counter VIRUS_COUNTER = counter("virus", "FEIL");

    private final VirusScanConfig config;
    private final RestOperations operations;

    public VirusScanConnection(RestOperations operations, VirusScanConfig config) {
        this.operations = operations;
        this.config = config;
    }

    public boolean isEnabled() {
        return config.isEnabled();
    }

    public void scan(byte[] bytes, String name) {
        if (isEnabled()) {
            try {
                LOG.info("Scanner {}", name);
                ScanResult[] scanResults = putForObject(config.getUri(), bytes, ScanResult[].class);
                if (scanResults.length != 1) {
                    LOG.warn("Uventet respons med lengde {}, forventet lengde er 1", scanResults.length);
                    return;
                }
                ScanResult scanResult = scanResults[0];
                LOG.info("Fikk scan result {}", scanResult);
                if (OK.equals(scanResult.getResult())) {
                    LOG.info("Ingen virus i {}", name);
                    INGENVIRUS_COUNTER.increment();
                    return;
                }
                LOG.warn("Fant virus i {}, status {}", name, scanResult.getResult());
                VIRUS_COUNTER.increment();
                throw new AttachmentVirusException(name);
            } catch (Exception e) {
                LOG.warn("Kunne ikke scanne {}", name, e);
                return;
            }
        }
        LOG.info("Virusscanning er ikke aktivert");
    }

    private <T> T putForObject(URI uri, Object payload, Class<T> responseType) {
        return operations.exchange(RequestEntity.put(uri).body(payload), responseType).getBody();
    }

    private static Counter counter(String name, String type) {
        return Metrics.counter(name, "result", type);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [config=" + config + ", operations=" + operations + "]";
    }

}
