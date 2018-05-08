package no.nav.foreldrepenger.selvbetjening.felles.config;

import no.nav.foreldrepenger.selvbetjening.felles.storage.RedisSentinelStorage;
import no.nav.foreldrepenger.selvbetjening.felles.storage.Storage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
public class StorageConfiguration {

    @Bean
    @Lazy
    public Storage storage() {
        return new RedisSentinelStorage();
    }

}
