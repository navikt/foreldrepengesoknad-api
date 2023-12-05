package no.nav.foreldrepenger.selvbetjening.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import java.util.Locale;

@Configuration
public class MessageSourceConfig {

    public static final Locale BOKMÅL_LOCALE = Locale.forLanguageTag("nb-NO");

    @Bean
    public LocaleResolver localeResolver() {
        AcceptHeaderLocaleResolver resolver = new AcceptHeaderLocaleResolver();
        resolver.setDefaultLocale(BOKMÅL_LOCALE);
        return resolver;
    }

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:visningsvennlig-feilmelding");
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setDefaultLocale(BOKMÅL_LOCALE);
        return messageSource;
    }
}
