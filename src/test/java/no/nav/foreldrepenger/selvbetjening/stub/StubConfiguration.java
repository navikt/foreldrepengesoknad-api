package no.nav.foreldrepenger.selvbetjening.stub;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import no.nav.foreldrepenger.selvbetjening.felles.storage.S3Storage;
import no.nav.foreldrepenger.selvbetjening.felles.storage.Storage;
import no.nav.foreldrepenger.selvbetjening.felles.storage.StorageCrypto;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import static org.testcontainers.containers.localstack.LocalStackContainer.Service.S3;

@Configuration
@Profile("dev")
public class StubConfiguration {

    @Bean
    @Profile("!localstack")
    public Storage storageStub() {
        return new StorageStub();
    }

    @Bean
    @Profile("localstack")
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
    @Profile("localstack")
    public StubbedLocalStackContainer stubbedLocalStackContainer() {
        return new StubbedLocalStackContainer().withServices(S3);
    }

    @Bean
    public StorageCrypto storageCrypto() {
        return new StorageCrypto("whatever");
    }
}
