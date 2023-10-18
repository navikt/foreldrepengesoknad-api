package no.nav.foreldrepenger.selvbetjening.innsending.pdf;

import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestOperations;

import no.nav.foreldrepenger.selvbetjening.http.AbstractRestConnection;

@Component
public class PdfGeneratorConnection extends AbstractRestConnection {

    private static final Logger LOG = LoggerFactory.getLogger(PdfGeneratorConnection.class);
    private final PdfGeneratorConfig config;

    public PdfGeneratorConnection(RestOperations operations, PdfGeneratorConfig config) {
        super(operations);
        this.config = config;
    }

    public byte[] genererPdf(TilbakebetalingUttalelseDto dto) {
        LOG.trace("Uttalelse for tilbakebetaling er {}", dto);
        return postIfEnabled(config.tilbakebetalingURI(), dto);
    }

    private byte[] postIfEnabled(URI uri, Object body) {
        if (config.isEnabled()) {
            return postForObject(uri, body, byte[].class);
        }
        LOG.info("PdfGenerator er ikke aktivert");
        return new byte[0];
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[config=" + config + "]";
    }
}
