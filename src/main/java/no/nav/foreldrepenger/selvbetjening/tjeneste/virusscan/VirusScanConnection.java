package no.nav.foreldrepenger.selvbetjening.tjeneste.virusscan;

import static org.springframework.http.MediaType.APPLICATION_PDF;
import static no.nav.foreldrepenger.selvbetjening.tjeneste.virusscan.Result.OK;

import java.net.URI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestOperations;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Metrics;

import no.nav.foreldrepenger.selvbetjening.tjeneste.mellomlagring.Attachment;

@Component
public class VirusScanConnection {

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

    public boolean scan(Attachment attachment) {
        try {
            if (!APPLICATION_PDF.equals(attachment.contentType)) {
                LOG.info("Scanner ikke vedlegg av tyoe {}", attachment.contentType);
                return true;
            }
            LOG.info("Scanner {}", attachment);
            ScanResult[] scanResults = putForObject(config.getUri(), attachment.bytes, ScanResult[].class);
            if (scanResults.length != 1) {
                LOG.warn("Uventet respons med lengde {}, forventet lengde er 1", scanResults.length);
                return true;
            }
            ScanResult scanResult = scanResults[0];
            LOG.info("Fikk scan result {}", scanResult);
            if (OK.equals(scanResult.getResult())) {
                LOG.info("Ingen virus i {}", attachment.uri());
                INGENVIRUS_COUNTER.increment();
                return true;
            }
            LOG.warn("Fant virus i {}", attachment.uri());
            VIRUS_COUNTER.increment();
            return false;
        } catch (Exception e) {
            LOG.warn("Kunne ikke scanne {}", attachment, e);
            return true;
        }
    }

    protected <T> T putForObject(URI uri, Object payload, Class<T> responseType) {
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
