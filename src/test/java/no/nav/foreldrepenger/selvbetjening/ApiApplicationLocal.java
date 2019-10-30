package no.nav.foreldrepenger.selvbetjening;

import static no.nav.foreldrepenger.selvbetjening.config.ClusterAwareSpringProfileResolver.profiles;
import static no.nav.foreldrepenger.selvbetjening.util.Constants.NAIS_CLUSTER_NAME;
import static no.nav.foreldrepenger.selvbetjening.util.EnvUtil.LOCAL;
import static org.springframework.context.annotation.FilterType.ASSIGNABLE_TYPE;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Import;
import org.springframework.retry.annotation.EnableRetry;

import com.google.common.base.Joiner;

import no.nav.foreldrepenger.selvbetjening.stub.StorageStub;
import no.nav.foreldrepenger.selvbetjening.tjeneste.mellomlagring.Storage;
import no.nav.foreldrepenger.selvbetjening.util.Cluster;
import no.nav.foreldrepenger.selvbetjening.util.ConditionalOnClusters;
import no.nav.security.token.support.spring.api.EnableJwtTokenValidation;
import no.nav.security.token.support.test.spring.TokenGeneratorConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableConfigurationProperties
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
    @ConditionalOnClusters(clusters = { Cluster.LOCAL })
    public Storage storageStub() {
        return new StorageStub();
    }
}
