package no.nav.foreldrepenger.selvbetjening;

import io.prometheus.client.hotspot.DefaultExports;
import no.nav.security.spring.oidc.validation.api.EnableOIDCTokenValidation;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan
@EnableOIDCTokenValidation(ignore = "org.springframework")
public class ApiApplication {

    public static void main(String[] args) {
        DefaultExports.initialize();
        SpringApplication.run(ApiApplication.class, args);
    }
}
