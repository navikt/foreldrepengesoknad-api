package no.nav.foreldrepenger.selvbetjening.config;

import static io.swagger.models.Scheme.HTTP;
import static io.swagger.models.Scheme.HTTPS;
import static java.util.stream.Collectors.toSet;
import static springfox.documentation.builders.RequestHandlerSelectors.basePackage;
import static springfox.documentation.spi.DocumentationType.SWAGGER_2;

import java.util.Set;
import java.util.stream.Stream;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.models.Scheme;
import no.nav.foreldrepenger.boot.conditionals.ConditionalOnNotProd;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
@ConditionalOnNotProd
@EnableOpenApi
public class SwaggerConfiguration {

    @Bean
    public Docket productApi() {
        return new Docket(SWAGGER_2)
                .protocols(allProtocols())
                .select()
                .apis(basePackage("no.nav.foreldrepenger.selvbetjening"))
                .paths(PathSelectors.any())
                .build();
    }

    private static Set<String> allProtocols() {
        return Stream.of(HTTPS, HTTP)
                .map(Scheme::toValue)
                .collect(toSet());
    }
}
