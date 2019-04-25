package no.nav.foreldrepenger.selvbetjening.tjeneste.virusscan;

import java.net.URI;
import static org.springframework.http.MediaType.APPLICATION_PDF;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestOperations;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Metrics;

import static no.nav.foreldrepenger.selvbetjening.tjeneste.virusscan.Result.*;
import no.nav.foreldrepenger.selvbetjening.tjeneste.AbstractRestConnection;
import no.nav.foreldrepenger.selvbetjening.tjeneste.mellomlagring.Attachment;

@Component
public class VirusScanConnection extends AbstractRestConnection {

    private final VirusScanConfig config;

    private static final Counter INGENVIRUS_COUNTER = counter("virus", "OK");
    private static final Counter VIRUS_COUNTER = counter("virus", "FEIL");

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

    @Override
    protected URI pingURI() {
        return config.getPingURI();
    }

    private static Counter counter(String name, String type) {
        return Metrics.counter(name, "result", type);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [config=" + config + "]";
    }
}
