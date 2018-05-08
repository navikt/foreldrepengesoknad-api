package no.nav.foreldrepenger.selvbetjening.felles.config;

import static com.fasterxml.jackson.annotation.JsonCreator.Mode.PROPERTIES;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;

@Configuration
public class JacksonModulesConfiguration {

    @Bean
    public Module javaTimeModule() {
        return new JavaTimeModule();
    }

    @Bean
    public Module jdk8Module() {
        return new Jdk8Module();
    }

    @Bean
    public Module parameterNamesModule() {
        return new ParameterNamesModule(PROPERTIES);
    }
}
