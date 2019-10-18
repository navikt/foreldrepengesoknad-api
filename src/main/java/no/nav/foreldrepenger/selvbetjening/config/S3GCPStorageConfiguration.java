package no.nav.foreldrepenger.selvbetjening.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import no.nav.foreldrepenger.selvbetjening.tjeneste.mellomlagring.Storage;
import no.nav.foreldrepenger.selvbetjening.tjeneste.mellomlagring.StorageCrypto;
import no.nav.foreldrepenger.selvbetjening.util.ConditionalOnGCP;

@Configuration
@ConditionalOnGCP
public class S3GCPStorageConfiguration {

    public Storage voidStorage() {
        return new VoidStorage();
    }

    /*
     * @Bean public Storage s3GCPStorage(AmazonS3 s3) { return new S3Storage(s3); }
     * 
     * @Bean public AmazonS3 s3(AWSCredentialsProvider credentialsProvider,
     * EndpointConfiguration endpointConfiguration) { return
     * AmazonS3ClientBuilder.standard()
     * .withEndpointConfiguration(endpointConfiguration)
     * .withCredentials(credentialsProvider) .build(); }
     * 
     * @Bean public EndpointConfiguration endpointConfiguration(
     * 
     * @Value("${gcp.endpoint:https://storage.googleapis.com}") String
     * serviceEndpoint) { return new
     * AwsClientBuilder.EndpointConfiguration(serviceEndpoint, "auto"); }
     * 
     * @Bean AWSCredentialsProvider credentialsProvider(AWSCredentials
     * s3Credentials) { return new AWSStaticCredentialsProvider(s3Credentials); }
     * 
     * @Bean public AWSCredentials s3Credentials(@Value("${gcp.accesskey}") String
     * accessKey,
     * 
     * @Value("${gcp.secretkey}") String secretKey) { return new
     * BasicAWSCredentials(accessKey, secretKey); }
     */
    @Bean
    public StorageCrypto storageCrypto(@Value("${storage.passphrase}") String encryptionPassphrase) {
        return new StorageCrypto(encryptionPassphrase);
    }

}
