package no.nav.foreldrepenger.selvbetjening;

import io.prometheus.client.hotspot.DefaultExports;
import no.nav.security.spring.oidc.validation.api.EnableOIDCTokenValidation;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import static org.springframework.boot.SpringApplication.run;

@SpringBootApplication
@ComponentScan
@EnableOIDCTokenValidation(ignore = "org.springframework")
public class ApplicationLocal {

    public static void main(String[] args) {
        DefaultExports.initialize();
        run(ApplicationLocal.class, args);
    }
}
