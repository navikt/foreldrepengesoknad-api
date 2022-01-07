package no.nav.foreldrepenger.selvbetjening.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import no.nav.foreldrepenger.common.mapper.DefaultJsonMapper;

@Configuration
public class JacksonConfiguration {

    @Bean
    @Primary
    public ObjectMapper customObjectmapper() {
        return DefaultJsonMapper.MAPPER
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES); // TODO: Fjern denne
    }
}
