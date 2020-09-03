package no.nav.foreldrepenger.selvbetjening.innsending.pdf;

import java.net.URI;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestOperations;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import no.nav.foreldrepenger.selvbetjening.http.AbstractRestConnection;

@Component
public class PdfGeneratorConnection extends AbstractRestConnection {

    private static final Logger LOG = LoggerFactory.getLogger(PdfGeneratorConnection.class);
    private final PdfGeneratorConfig config;

    @Inject
    private ObjectMapper mapper;

    public PdfGeneratorConnection(RestOperations operations, PdfGeneratorConfig config) {
        super(operations);
        this.config = config;
    }

    @Override
    public URI pingURI() {
        return config.pingURI();
    }

    public byte[] genererPdf(TilbakebetalingUttalelseDto dto) {
        logJSON(dto);
        return postIfEnabled(config.tilbakebetalingURI(), dto);
    }

    @Override
    public boolean isEnabled() {
        return config.isEnabled();
    }

    private byte[] postIfEnabled(URI uri, Object body) {
        if (isEnabled()) {
            return postForObject(uri, body, byte[].class);
        }
        LOG.info("PdfGenerator er ikke aktivert");
        return null;
    }

    private void logJSON(TilbakebetalingUttalelseDto dto) {
        try {
            LOG.trace("JSON er {}", mapper.writerWithDefaultPrettyPrinter().writeValueAsString(dto));
        } catch (JsonProcessingException e) {

        }
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[config=" + config + "]";
    }
}
