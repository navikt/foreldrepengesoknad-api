package no.nav.foreldrepenger.selvbetjening;

import static org.springframework.boot.SpringApplication.run;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

import io.prometheus.client.hotspot.DefaultExports;
import no.nav.security.spring.oidc.test.TokenGeneratorConfiguration;
import no.nav.security.spring.oidc.validation.api.EnableOIDCTokenValidation;

@SpringBootApplication
@Import(value = TokenGeneratorConfiguration.class)
@EnableOIDCTokenValidation(ignore = "org.springframework")
public class ApplicationLocal {

    public static void main(String[] args) {
        DefaultExports.initialize();
        run(ApplicationLocal.class, args);
    }
}
