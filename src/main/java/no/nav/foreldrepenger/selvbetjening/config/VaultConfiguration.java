package no.nav.foreldrepenger.selvbetjening.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.vault.config.SecretBackendConfigurer;

import org.springframework.cloud.vault.config.VaultConfigurer;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("dev-sbs")
@Configuration
public class VaultConfiguration implements VaultConfigurer {
    private static final Logger log = LoggerFactory.getLogger(VaultConfiguration.class);


    @Override
    public void addSecretBackends(SecretBackendConfigurer configurer) {
        log.info("Oppretter custom VaultConfiguration");

        configurer.add("apigw/preprod/fppdfgen/foreldrepengesoknad-api_q1");
        configurer.add("kv/preprod/sbs");

        configurer.registerDefaultGenericSecretBackends(false);
        configurer.registerDefaultDiscoveredSecretBackends(true);
    }
}
