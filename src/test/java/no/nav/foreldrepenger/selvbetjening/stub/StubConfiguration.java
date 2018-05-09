package no.nav.foreldrepenger.selvbetjening.stub;

import no.nav.foreldrepenger.selvbetjening.felles.storage.Storage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("dev")
public class StubConfiguration {

    @Bean
    @Primary
    public Storage storageStub() {
        return new StorageStub();
    }

}
