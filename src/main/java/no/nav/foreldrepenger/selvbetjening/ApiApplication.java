package no.nav.foreldrepenger.selvbetjening;

import no.nav.security.spring.oidc.api.EnableOIDCTokenValidation;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import io.prometheus.client.hotspot.DefaultExports;

@SpringBootApplication
@EnableOIDCTokenValidation(ignore = { "org.springframework", "springfox.documentation" })
public class ApiApplication {

    public static void main(String[] args) {
        DefaultExports.initialize();
        SpringApplication.run(ApiApplication.class, args);
    }
}
