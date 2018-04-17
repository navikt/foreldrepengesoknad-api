package no.nav.foreldrepenger.selvbetjening.rest.attachments.exceptions;

import org.springframework.http.MediaType;

public class AttachmentTypeUnsupportedException extends AttachmentException {

    public AttachmentTypeUnsupportedException(MediaType mediaType) {
        this(mediaType, null);
    }

    public AttachmentTypeUnsupportedException(Throwable e) {
        this(null, null);
    }

    public AttachmentTypeUnsupportedException(MediaType mediaType, Throwable e) {
        super(mediaType, e);
    }

}
