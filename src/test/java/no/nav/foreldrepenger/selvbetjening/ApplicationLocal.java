package no.nav.foreldrepenger.selvbetjening;

import static org.springframework.context.annotation.FilterType.ASSIGNABLE_TYPE;

import no.nav.security.oidc.test.support.spring.TokenGeneratorConfiguration;
import no.nav.security.spring.oidc.api.EnableOIDCTokenValidation;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Import;

import io.prometheus.client.hotspot.DefaultExports;
import no.nav.foreldrepenger.selvbetjening.felles.util.EnvUtil;


@SpringBootApplication
@EnableOIDCTokenValidation(ignore = { "org.springframework", "springfox.documentation" })
@Import(value = TokenGeneratorConfiguration.class)
@ComponentScan(excludeFilters = { @Filter(type = ASSIGNABLE_TYPE, value = ApiApplication.class) })
public class ApplicationLocal {

    public static void main(String[] args) {
        DefaultExports.initialize();
        SpringApplication app = new SpringApplication(ApplicationLocal.class);
        app.setAdditionalProfiles(EnvUtil.DEV);
        app.run(args);
    }
}
