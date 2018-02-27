package no.nav.foreldrepenger.selvbetjening.service;

import static org.slf4j.LoggerFactory.getLogger;

import java.net.URI;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class PingService {

    private static final Logger LOG = getLogger(PingService.class);

    private final RestTemplate template;
    private final URI mottakUri;

    public PingService(RestTemplate template, @Value("${FPSOKNAD_MOTTAK_API_URL}") URI mottakUri) {
        this.template = template;
        this.mottakUri = mottakUri;
    }

    public String ping(String message) {
        URI uri = UriComponentsBuilder
                .fromUri(mottakUri)
                .path("/mottak/dokmot/ping")
                .queryParam("navn", message).build().toUri();
        LOG.info("Pinging mottak {}", uri);
        return template.getForObject(uri, String.class);
    }

    public URI remote() {
        return mottakUri;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [template=" + template + ", mottakUri=" + mottakUri + "]";
    }
}
