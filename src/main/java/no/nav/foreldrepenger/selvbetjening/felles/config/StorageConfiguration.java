package no.nav.foreldrepenger.selvbetjening.felles.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import no.nav.foreldrepenger.selvbetjening.felles.storage.S3Storage;
import no.nav.foreldrepenger.selvbetjening.felles.storage.Storage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
public class StorageConfiguration {

    @Value("${FORELDREPENGESOKNAD_API_ACCESSKEY_PASSWORD}")
    private String accessKey;

    @Value("${FORELDREPENGESOKNAD_API_SECRETKEY_PASSWORD}")
    private String secretKey;

    @Value("${FORELDREPENGESOKNAD_API_S3_ENDPOINT}")
    private String s3Endpoint;

    private final static String REGION_TO_USE_FOR_S3_TO_WORK_ONPREM = "us-east-1";

    @Bean
    @Lazy
    public Storage storage() {
        return new S3Storage(s3());
    }

    private AmazonS3 s3() {
        AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
        return AmazonS3ClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(s3Endpoint, REGION_TO_USE_FOR_S3_TO_WORK_ONPREM))
                .enablePathStyleAccess()
                .withCredentials(new AWSStaticCredentialsProvider(credentials)).build();
    }

}
