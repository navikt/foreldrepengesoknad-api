package no.nav.foreldrepenger.selvbetjening.config;

import org.springframework.cloud.vault.config.SecretBackendConfigurer;

import org.springframework.cloud.vault.config.VaultConfigurer;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("dev-sbs")
@Configuration
public class VaultConfiguration implements VaultConfigurer {

    @Override
    public void addSecretBackends(SecretBackendConfigurer configurer) {
        configurer.add("kv/preprod/sbs");
        configurer.add("apigw/preprod/fppdfgen/foreldrepengesoknad-api_q1");
    }
}
