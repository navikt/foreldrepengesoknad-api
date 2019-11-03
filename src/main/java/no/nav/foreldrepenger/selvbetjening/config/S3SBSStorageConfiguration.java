package no.nav.foreldrepenger.selvbetjening.config;

import static no.nav.foreldrepenger.selvbetjening.util.Cluster.DEV_SBS;
import static no.nav.foreldrepenger.selvbetjening.util.Cluster.PROD_SBS;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.retry.PredefinedRetryPolicies;
import com.amazonaws.retry.RetryPolicy;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

import no.nav.foreldrepenger.selvbetjening.tjeneste.mellomlagring.Mellomlagring;
import no.nav.foreldrepenger.selvbetjening.tjeneste.mellomlagring.S3Mellomlagring;
import no.nav.foreldrepenger.selvbetjening.util.ConditionalOnClusters;

@Configuration
@ConditionalOnClusters(clusters = { DEV_SBS, PROD_SBS })
public class S3SBSStorageConfiguration {

    @Bean
    public Mellomlagring S3Mellomlagring(AmazonS3 s3,
            @Value("${mellomlagring.søknad:foreldrepengesoknad}") String søknadBøtte,
            @Value("${mellomlagring.mellomlagring:mellomlagring}") String mellomlagringBøtte,
            @Value("${mellomlagring.enabled:true}") boolean enabled) {
        return new S3Mellomlagring(s3, søknadBøtte, mellomlagringBøtte, enabled);
    }

    @Bean
    public AmazonS3 s3(AWSCredentials s3Credentials, EndpointConfiguration endpointConfig,
            ClientConfiguration clientConfig) {
        return AmazonS3ClientBuilder.standard()
                .withEndpointConfiguration(endpointConfig)
                .enablePathStyleAccess()
                .withClientConfiguration(clientConfig)
                .withCredentials(new AWSStaticCredentialsProvider(s3Credentials))
                .build();
    }

    @Bean
    public ClientConfiguration s3ClientConfig(RetryPolicy retryPolicy, @Value("${s3.timeout:3000}") int requestTimeout,
            @Value("${s3.retries:3}") int retries) {
        return new ClientConfiguration()
                .withRetryPolicy(retryPolicy)
                .withRequestTimeout(requestTimeout)
                .withMaxErrorRetry(retries);
    }

    @Bean
    public RetryPolicy retryPolicy(@Value("${s3.retries:3}") int retries) {
        return PredefinedRetryPolicies.getDefaultRetryPolicyWithCustomMaxRetries(retries);
    }

    @Bean
    public EndpointConfiguration s3EndpointConfig(@Value("${s3.region:us-east-1}") String region,
            @Value("${s3.url:http://s3.nais-rook.svc.nais.local/}") String url) {
        return new EndpointConfiguration(url, region);
    }

    @Bean
    public AWSCredentials s3Credentials(@Value("${s3.username}") String accessKey,
            @Value("${s3.password}") String secretKey) {
        return new BasicAWSCredentials(accessKey, secretKey);
    }
}
