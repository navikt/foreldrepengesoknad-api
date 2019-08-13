package no.nav.foreldrepenger.selvbetjening.config;

import static no.nav.foreldrepenger.selvbetjening.util.EnvUtil.NOTLOCAL;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

import no.nav.foreldrepenger.selvbetjening.tjeneste.mellomlagring.S3Storage;
import no.nav.foreldrepenger.selvbetjening.tjeneste.mellomlagring.Storage;
import no.nav.foreldrepenger.selvbetjening.tjeneste.mellomlagring.StorageCrypto;

@Configuration
@Profile(NOTLOCAL)
public class StorageConfiguration {

    @Bean
    @Lazy
    public Storage storage(@Value("${s3.username}") String accessKey, @Value("${s3.password}") String secretKey,
            @Value("${s3.region:us-east-1}") String region,
            @Value("${s3.url:http://s3.nais-rook.svc.nais.local/}") String url) {
        return new S3Storage(AmazonS3ClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(url, region))
                .enablePathStyleAccess()
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)))
                .build());
    }

    @Bean
    public StorageCrypto storageCrypto(@Value("${storage.passphrase}") String encryptionPassphrase) {
        return new StorageCrypto(encryptionPassphrase);
    }

}
