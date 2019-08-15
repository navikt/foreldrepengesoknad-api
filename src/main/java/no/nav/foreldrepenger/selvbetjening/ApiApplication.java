package no.nav.foreldrepenger.selvbetjening;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cache.annotation.EnableCaching;

import no.nav.foreldrepenger.selvbetjening.config.ClusterAwareSpringProfileResolver;
import no.nav.security.spring.oidc.api.EnableOIDCTokenValidation;

@SpringBootApplication
@EnableCaching
@EnableOIDCTokenValidation(ignore = { "org.springframework", "springfox.documentation" })
public class ApiApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(ApiApplication.class)
                .profiles(new ClusterAwareSpringProfileResolver().getProfile())
                .main(ApiApplication.class)
                .run(args);
    }
}
