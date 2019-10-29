package no.nav.foreldrepenger.selvbetjening.stub;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import no.nav.foreldrepenger.selvbetjening.tjeneste.mellomlagring.Storage;
import no.nav.foreldrepenger.selvbetjening.util.ConditionalOnLocal;

@Configuration
@ConditionalOnLocal
public class StubConfiguration {

    @Bean
    public Storage storageStub() {
        return new StorageStub();
    }

}
