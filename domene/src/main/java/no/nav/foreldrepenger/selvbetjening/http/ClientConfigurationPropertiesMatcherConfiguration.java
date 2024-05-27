package no.nav.foreldrepenger.selvbetjening.http;

import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import no.nav.security.token.support.client.core.ClientProperties;
import no.nav.security.token.support.client.spring.ClientConfigurationProperties;
import no.nav.security.token.support.client.spring.oauth2.ClientConfigurationPropertiesMatcher;

@Configuration
public class ClientConfigurationPropertiesMatcherConfiguration implements ClientConfigurationPropertiesMatcher {
    private static final Logger LOG = LoggerFactory.getLogger(ClientConfigurationPropertiesMatcherConfiguration.class);

    @Override
    public ClientProperties findProperties(ClientConfigurationProperties properties, URI uri) {
        LOG.trace("Oppslag token X konfig for {}", uri.getHost());
        var cfg = properties.getRegistration().get(uri.getHost().split("\\.")[0]);
        if (cfg != null) {
            LOG.trace("Oppslag token X konfig for {} OK", uri.getHost());
        } else {
            LOG.trace("Oppslag token X konfig for {} fant ingenting", uri.getHost());
        }
        return cfg;
    }

    @Override
    public ClientProperties findProperties(ClientConfigurationProperties properties, String uri) {
        return findProperties(properties, URI.create(uri));
    }
}
