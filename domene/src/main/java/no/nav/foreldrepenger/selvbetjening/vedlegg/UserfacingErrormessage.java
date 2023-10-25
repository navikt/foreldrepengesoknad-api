package no.nav.foreldrepenger.selvbetjening.vedlegg;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

public interface UserfacingErrormessage {

    String getUserfacingErrorMessage(MessageSource messageSource);

    default String getMessage(MessageSource messageSource, String code, Object... arguments) {
        var locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(code, arguments, locale);
    }

}
