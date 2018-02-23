package no.nav.foreldrepenger.selvbetjening.config;

import static io.swagger.models.Scheme.HTTP;
import static io.swagger.models.Scheme.HTTPS;
import static java.util.stream.Collectors.toSet;
import static springfox.documentation.builders.PathSelectors.regex;
import static springfox.documentation.spi.DocumentationType.SWAGGER_2;

import java.util.Set;
import java.util.stream.Stream;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.models.Scheme;
import no.nav.foreldrepenger.selvbetjening.rest.PingController;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

    @Bean
    public Docket productApi() {
        return new Docket(SWAGGER_2)
                .protocols(protocols(HTTPS, HTTP))
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(regex(PingController.PING + ".*"))
                .build();
    }

    private static Set<String> protocols(Scheme... schemes) {
        return Stream.of(schemes).map(s -> s.toValue()).collect(toSet());
    }
}
