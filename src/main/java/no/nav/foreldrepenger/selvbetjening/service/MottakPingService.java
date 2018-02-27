package no.nav.foreldrepenger.selvbetjening.service;

import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class MottakPingService extends AbstractPingService {

    public MottakPingService(RestTemplate template, @Value("${FPSOKNAD_MOTTAK_API_URL}") URI mottakUri) {
        super(template, mottakUri);
    }

    @Override
    protected URI pingURI(String message) {
        return UriComponentsBuilder
                .fromUri(baseUri)
                .path("/mottak/dokmot/ping")
                .queryParam("navn", message).build().toUri();
    }
}
