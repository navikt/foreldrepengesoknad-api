package no.nav.foreldrepenger.selvbetjening.felles.attachments.exceptions;

import org.springframework.http.MediaType;

public class AttachmentConversionException extends AttachmentException {

    public AttachmentConversionException(String msg, Throwable e) {
        this(msg, null, e);
    }

    public AttachmentConversionException(String msg, MediaType mediaType, Throwable e) {
        super(msg, mediaType, e);
    }
}
