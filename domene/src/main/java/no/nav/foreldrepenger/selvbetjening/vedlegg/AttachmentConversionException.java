package no.nav.foreldrepenger.selvbetjening.vedlegg;

import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;

public class AttachmentConversionException extends AttachmentException {

    public AttachmentConversionException(String msg, Throwable e) {
        this(msg, null, e);
    }

    public AttachmentConversionException(String msg, MediaType mediaType, Throwable e) {
        super(msg, mediaType, e);
    }

    @Override
    public String getUserfacingErrorMessage(MessageSource messageSource) {
        return getMessage(messageSource, "vedlegg.konvertering.feilet");
    }
}
