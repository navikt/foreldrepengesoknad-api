package no.nav.foreldrepenger.selvbetjening.config;

import static io.swagger.models.Scheme.HTTP;
import static io.swagger.models.Scheme.HTTPS;
import static java.util.stream.Collectors.toSet;
import static no.nav.foreldrepenger.selvbetjening.util.EnvUtil.DEV;
import static springfox.documentation.spi.DocumentationType.SWAGGER_2;

import java.util.Set;
import java.util.stream.Stream;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import io.swagger.models.Scheme;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@Profile(DEV)
@EnableSwagger2
public class SwaggerConfiguration {

    @Bean
    public Docket productApi() {
        return new Docket(SWAGGER_2)
                .protocols(allProtocols())
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
    }

    private static Set<String> allProtocols() {
        return Stream.of(new Scheme[] { HTTPS, HTTP })
                .map(Scheme::toValue)
                .collect(toSet());
    }
}
