package no.nav.foreldrepenger.selvbetjening.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import no.nav.foreldrepenger.selvbetjening.tjeneste.mellomlagring.GCPCloudStorage;
import no.nav.foreldrepenger.selvbetjening.tjeneste.mellomlagring.Storage;
import no.nav.foreldrepenger.selvbetjening.tjeneste.mellomlagring.StorageCrypto;
import no.nav.foreldrepenger.selvbetjening.util.ConditionalOnGCP;

@Configuration
@ConditionalOnGCP
public class S3GCPStorageConfiguration {

    @Bean
    public Storage voidStorage() {
        return new GCPCloudStorage();
    }

    @Bean
    public StorageCrypto storageCrypto(@Value("${storage.passphrase}") String encryptionPassphrase) {
        return new StorageCrypto(encryptionPassphrase);
    }

}
