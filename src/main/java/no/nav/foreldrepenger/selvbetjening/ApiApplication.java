package no.nav.foreldrepenger.selvbetjening;

import static no.nav.foreldrepenger.boot.conditionals.Cluster.profiler;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.retry.annotation.EnableRetry;

import no.nav.security.token.support.spring.api.EnableJwtTokenValidation;
import springfox.documentation.oas.annotations.EnableOpenApi;

@SpringBootApplication
@EnableDiscoveryClient
@EnableOpenApi
@EnableCaching
@EnableRetry
@ConfigurationPropertiesScan("no.nav.foreldrepenger.selvbetjening")
@EnableJwtTokenValidation(ignore = { "org.springframework", "springfox.documentation" })
public class ApiApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(ApiApplication.class)
                .profiles(profiler())
                .main(ApiApplication.class)
                .run(args);
    }
}
