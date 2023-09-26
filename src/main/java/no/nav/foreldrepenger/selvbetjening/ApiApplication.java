package no.nav.foreldrepenger.selvbetjening;

import static no.nav.boot.conditionals.Cluster.profiler;

import java.util.TimeZone;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.retry.annotation.EnableRetry;

import jakarta.annotation.PostConstruct;
import no.nav.security.token.support.client.spring.oauth2.EnableOAuth2Client;
import no.nav.security.token.support.spring.api.EnableJwtTokenValidation;

@SpringBootApplication
@EnableCaching
@EnableRetry
@EnableOAuth2Client(cacheEnabled = true)
@ConfigurationPropertiesScan("no.nav.foreldrepenger.selvbetjening")
@EnableJwtTokenValidation(ignore = { "org.springframework", "org.springdoc" })
public class ApiApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(ApiApplication.class)
                .profiles(profiler())
                .main(ApiApplication.class)
                .run(args);
    }

    @PostConstruct
    public void init(){
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/Oslo"));
    }
}
