package no.nav.foreldrepenger.selvbetjening.config;

import static springfox.documentation.builders.RequestHandlerSelectors.basePackage;
import static springfox.documentation.spi.DocumentationType.OAS_30;

import java.util.Set;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
        return new Docket(OAS_30)
                .protocols(Set.of("http", "https"))
                .select()
                .apis(basePackage("no.nav.foreldrepenger.selvbetjening"))
                .paths(PathSelectors.any())
                .build();
    }
}
