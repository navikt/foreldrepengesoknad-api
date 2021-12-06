package no.nav.foreldrepenger.selvbetjening;

import no.nav.security.token.support.spring.api.EnableJwtTokenValidation;
import no.nav.security.token.support.spring.test.EnableMockOAuth2Server;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.retry.annotation.EnableRetry;
import springfox.documentation.oas.annotations.EnableOpenApi;

import static no.nav.foreldrepenger.boot.conditionals.Cluster.profiler;
import static org.springframework.context.annotation.FilterType.ASSIGNABLE_TYPE;

@EnableMockOAuth2Server
@SpringBootApplication
@ConfigurationPropertiesScan("no.nav.foreldrepenger.selvbetjening")
@EnableCaching
@EnableRetry
@EnableOpenApi
@EnableJwtTokenValidation(ignore = { "org.springframework", "springfox.documentation" })
@ComponentScan(excludeFilters = { @Filter(type = ASSIGNABLE_TYPE, value = ApiApplication.class) })
public class ApiApplicationLocal {

    public static void main(String[] args) {
        new SpringApplicationBuilder(ApiApplicationLocal.class)
                .profiles(profiler())
                .main(ApiApplicationLocal.class)
                .run(args);
    }
}
