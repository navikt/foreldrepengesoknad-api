package no.nav.foreldrepenger.selvbetjening;

import static no.nav.foreldrepenger.boot.conditionals.EnvUtil.LOCAL;
import static no.nav.foreldrepenger.selvbetjening.config.ClusterAwareSpringProfileResolver.profiles;
import static no.nav.foreldrepenger.selvbetjening.tjeneste.mellomlagring.Bøtte.SØKNAD;
import static no.nav.foreldrepenger.selvbetjening.tjeneste.mellomlagring.Bøtte.TMP;
import static no.nav.foreldrepenger.selvbetjening.util.Constants.NAIS_CLUSTER_NAME;
import static org.springframework.context.annotation.FilterType.ASSIGNABLE_TYPE;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Import;
import org.springframework.retry.annotation.EnableRetry;

import com.google.common.base.Joiner;

import no.nav.foreldrepenger.boot.conditionals.ConditionalOnLocal;
import no.nav.foreldrepenger.selvbetjening.stub.InMemoryMellomlagring;
import no.nav.foreldrepenger.selvbetjening.tjeneste.mellomlagring.Bøtte;
import no.nav.foreldrepenger.selvbetjening.tjeneste.mellomlagring.Mellomlagring;
import no.nav.security.token.support.spring.api.EnableJwtTokenValidation;
import no.nav.security.token.support.test.spring.TokenGeneratorConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@ConfigurationPropertiesScan("no.nav.foreldrepenger.selvbetjening")
@EnableCaching
@EnableRetry
@EnableSwagger2
@EnableJwtTokenValidation(ignore = { "org.springframework", "springfox.documentation" })
@Import(value = TokenGeneratorConfiguration.class)
@ComponentScan(excludeFilters = { @Filter(type = ASSIGNABLE_TYPE, value = ApiApplication.class) })
public class ApiApplicationLocal {

    public static void main(String[] args) {
        new SpringApplicationBuilder(ApiApplicationLocal.class)
                .profiles(profiles())
                .properties(localCluster())
                .main(ApiApplicationLocal.class)
                .run(args);
    }

    private static String localCluster() {
        return Joiner.on(':').join(NAIS_CLUSTER_NAME, LOCAL);
    }

    @Bean
    @ConditionalOnLocal
    public Mellomlagring inMemoryMellomlager(@Qualifier(SØKNAD) Bøtte b1, @Qualifier(TMP) Bøtte b2) {
        return new InMemoryMellomlagring(b1, b2);
    }
}
