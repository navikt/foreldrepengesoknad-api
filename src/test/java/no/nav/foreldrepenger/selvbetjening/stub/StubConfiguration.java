package no.nav.foreldrepenger.selvbetjening.stub;

import static no.nav.foreldrepenger.selvbetjening.util.EnvUtil.LOCAL;
import static no.nav.foreldrepenger.selvbetjening.util.EnvUtil.LOCALSTACK;
import static no.nav.foreldrepenger.selvbetjening.util.EnvUtil.NOTLOCALSTACK;
import static no.nav.foreldrepenger.selvbetjening.util.EnvUtil.TEST;
import static org.testcontainers.containers.localstack.LocalStackContainer.Service.S3;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

import no.nav.foreldrepenger.selvbetjening.tjeneste.mellomlagring.S3Storage;
import no.nav.foreldrepenger.selvbetjening.tjeneste.mellomlagring.Storage;

@Configuration
@Profile({ TEST, LOCAL })
public class StubConfiguration {

    @Bean
    @Profile(NOTLOCALSTACK)
    public Storage storageStub() {
        return new StorageStub();
    }

    @Bean
    @Profile(LOCALSTACK)
    public Storage containerStub(StubbedLocalStackContainer localstack) {
        AmazonS3 s3 = AmazonS3ClientBuilder
                .standard()
                .withEndpointConfiguration(localstack.getEndpointConfiguration(S3))
                .withCredentials(localstack.getDefaultCredentialsProvider())
                .enablePathStyleAccess()
                .build();
        return new S3Storage(s3);
    }

    @Bean(name = "stubbedLocalStackContainer")
    @Profile(LOCALSTACK)
    public StubbedLocalStackContainer stubbedLocalStackContainer() {
        return new StubbedLocalStackContainer().withServices(S3);
    }

}
