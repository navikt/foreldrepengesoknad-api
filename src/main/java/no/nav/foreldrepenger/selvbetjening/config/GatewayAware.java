package no.nav.foreldrepenger.selvbetjening.config;

import java.net.URI;

public interface GatewayAware {
    String getApiKey();

    URI getUri();
}
