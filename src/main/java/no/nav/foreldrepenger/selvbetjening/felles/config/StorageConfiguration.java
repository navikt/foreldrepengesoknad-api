package no.nav.foreldrepenger.selvbetjening.felles.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

import no.nav.foreldrepenger.selvbetjening.felles.storage.S3Storage;
import no.nav.foreldrepenger.selvbetjening.felles.storage.Storage;
import no.nav.foreldrepenger.selvbetjening.felles.storage.StorageCrypto;

@Configuration
@Profile("!dev")
public class StorageConfiguration {

    @Value("${FORELDREPENGESOKNAD_API_S3_CREDS_USERNAME}")
    private String accessKey;

    @Value("${FORELDREPENGESOKNAD_API_S3_CREDS_PASSWORD}")
    private String secretKey;

    @Value("${FORELDREPENGESOKNAD_API_S3_ENDPOINT_URL}")
    private String s3Endpoint;

    @Value("${FORELDREPENGESOKNAD_API_S3_REGION:us-east-1}")
    private String s3Region;

    @Value("${FORELDREPENGESOKNAD_API_STORAGE_PASSWORD}")
    private String encryptionPassphrase;

    @Bean
    @Lazy
    public Storage storage() {
        return new S3Storage(s3());
    }

    @Bean
    public StorageCrypto storageCrypto() {
        return new StorageCrypto(encryptionPassphrase);
    }

    private AmazonS3 s3() {
        AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
        return AmazonS3ClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(s3Endpoint, s3Region))
                .enablePathStyleAccess()
                .withCredentials(new AWSStaticCredentialsProvider(credentials)).build();
    }

}
