package no.nav.foreldrepenger.selvbetjening;

import static no.nav.foreldrepenger.selvbetjening.util.EnvUtil.LOCAL;
import static org.springframework.context.annotation.FilterType.ASSIGNABLE_TYPE;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.vault.config.VaultBootstrapConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Import;

import no.nav.security.oidc.test.support.spring.TokenGeneratorConfiguration;
import no.nav.security.spring.oidc.api.EnableOIDCTokenValidation;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableCaching
@EnableSwagger2
@EnableAutoConfiguration(exclude = { VaultBootstrapConfiguration.class })
@EnableOIDCTokenValidation(ignore = { "org.springframework", "springfox.documentation" })
@Import(value = TokenGeneratorConfiguration.class)
@ComponentScan(excludeFilters = { @Filter(type = ASSIGNABLE_TYPE, value = ApiApplication.class) })
public class ApiApplicationLocal {

    public static void main(String[] args) {
        new SpringApplicationBuilder(ApiApplicationLocal.class)
                .profiles(LOCAL)
                .main(ApiApplicationLocal.class)
                .run(args);
    }
}
