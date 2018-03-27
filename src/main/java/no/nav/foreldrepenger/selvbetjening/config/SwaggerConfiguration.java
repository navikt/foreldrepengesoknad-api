package no.nav.foreldrepenger.selvbetjening.config;

import static com.google.common.base.Predicates.or;
import static io.swagger.models.Scheme.HTTP;
import static io.swagger.models.Scheme.HTTPS;
import static java.util.stream.Collectors.toSet;
import static no.nav.foreldrepenger.selvbetjening.rest.MottakController.REST_ENGANGSSTONAD;
import static no.nav.foreldrepenger.selvbetjening.rest.OppslagController.REST_OPPSLAG;
import static springfox.documentation.builders.PathSelectors.regex;
import static springfox.documentation.spi.DocumentationType.SWAGGER_2;

import java.util.Set;
import java.util.stream.Stream;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.models.Scheme;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

    @SuppressWarnings("unchecked")
    @Bean
    public Docket productApi() {
        return new Docket(SWAGGER_2)
                .protocols(allProtocols())
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(or(
                        regex(REST_OPPSLAG + ".*"),
                        regex(REST_ENGANGSSTONAD + ".*")))
                .build();
    }

    private static Set<String> allProtocols() {
        return Stream.of(new Scheme[] { HTTPS, HTTP }).map(Scheme::toValue).collect(toSet());
    }
}
