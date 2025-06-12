package no.nav.foreldrepenger.selvbetjening.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import no.nav.boot.conditionals.ConditionalOnNotProd;

@Configuration
@ConditionalOnNotProd
public class SwaggerConfiguration {

    static {
        io.swagger.v3.core.jackson.ModelResolver.enumsAsRef = true;
    }

    @Bean
    public OpenAPI swaggerOpenAPI() {
        return new OpenAPI().info(new Info().title("Foreldrepengesoknad-api")
            .description(
                "Mottar søknader om svangerskapspenger, foreldrepenger og engangsstønad fra frontend og sender dem videre inn i NAV for behandling")
            .version("v0.0.1")
            .license(new License().name("MIT").url("http://nav.no")));
    }
}
